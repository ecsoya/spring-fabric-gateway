package io.ecsoya.fabric.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.gateway.impl.ContractImpl;
import org.hyperledger.fabric.gateway.impl.WalletIdentity;
import org.hyperledger.fabric.protos.peer.FabricTransaction.TxValidationCode;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo.IdentitiesInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.NetworkConfig.OrgInfo;
import org.hyperledger.fabric.sdk.NetworkConfig.UserInfo;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import io.ecsoya.fabric.FabricQueryRequest;
import io.ecsoya.fabric.FabricQueryResponse;
import io.ecsoya.fabric.FabricRequest;
import io.ecsoya.fabric.FabricResponse;
import io.ecsoya.fabric.bean.FabricBlock;
import io.ecsoya.fabric.bean.FabricHistory;
import io.ecsoya.fabric.bean.FabricLedger;
import io.ecsoya.fabric.bean.FabricTransaction;
import io.ecsoya.fabric.bean.FabricTransactionRWSet;
import io.ecsoya.fabric.bean.gateway.FabricContract;

public class FabricContext {
	private Logger logger = LoggerFactory.getLogger(FabricContext.class);

	private FabricProperties properties;

	private Gateway.Builder builder;
	private Network network;
	private FabricContract contract;

	private Timer timer = new Timer(true);;
	private TimerTask shutdownTask;

	public FabricContext(FabricProperties properties) {
		this.properties = properties;
		if (properties == null) {
			properties = new FabricProperties();
		}
	}

	private void aboutToShutdown() {

		// Always keep only 1 task;
		cancelShutdown();

		shutdownTask = new TimerTask() {

			@Override
			public void run() {
				performShutdown();
			}
		};

		// Schedule after 5 minutes.
		timer.schedule(shutdownTask, 300000);
	}

	private void cancelShutdown() {
		if (shutdownTask != null) {
			shutdownTask.cancel();
		}
		timer.purge();
	}

	private void performShutdown() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (network != null) {
			Thread.currentThread().setContextClassLoader(network.getClass().getClassLoader());
			try {
				network.getGateway().close();
			} finally {
				Thread.currentThread().setContextClassLoader(contextClassLoader);
			}
		}
		network = null;
		contract = null;
	}

	@PostConstruct
	private void initialize() throws Exception {

		logger.debug("Initialize Fabric Context");

		String channel = properties.getChannel();
		if (channel == null || channel.equals("")) {
			throw new RuntimeException(
					"Initialize fabric gateway failed with invalid 'channel' name. Please make sure the channel is configured corrected by 'spring.fabric.channel'.");
		}

		String chaincode = properties.getChaincode();
		if (chaincode == null || chaincode.equals("")) {
			throw new RuntimeException(
					"Initialize fabric gateway failed with invalid 'chaincode' name. Please make sure the chaincode is configured corrected by 'spring.fabric.chaincode'.");
		}

		Wallet wallet = createWallet();

		InputStream configFile = properties.getNetworkContents();
		if (configFile == null) {
			throw new RuntimeException(
					"Network config file can not be loaded. Please make sure 'spring.fabric.network.path' is configured.");
		}

		NetworkConfig config = NetworkConfig.fromYamlStream(configFile);
		if (config == null) {
			throw new RuntimeException(
					"Network config can not be loaded. Please make sure 'spring.fabric.network.path' is correct.");
		}

		FabricWalletProperties walletProps = properties.getWallet();

		String identifyName = walletProps.getIdentify();
		if (identifyName == null || identifyName.equals("")) {
			identifyName = "admin";
			logger.debug("Initialize Fabric Context: missing identify for wallet, and using default value: admin");
		}

		OrgInfo client = config.getClientOrganization();
		if (client != null) {
			UserInfo peerAdmin = client.getPeerAdmin();
			if (peerAdmin != null) {
				Enrollment enrollment = peerAdmin.getEnrollment();
				if (enrollment != null) {
					Identity admin = new WalletIdentity(peerAdmin.getMspId(), enrollment.getCert(),
							enrollment.getKey());
					wallet.put(identifyName, admin);
					logger.debug("Initialize Wallet " + identifyName);
				}
			}
		}

		logger.debug("Initialize Gateway... ");
		configFile = properties.getNetworkContents();
		builder = Gateway.createBuilder().identity(wallet, identifyName).networkConfig(configFile);
		Gateway gateway = builder.connect();

		logger.debug("Initialize Network... ");

		network = gateway.getNetwork(channel);
	}

	private Wallet createWallet() {
		return Wallet.createInMemoryWallet();
	}

	private FabricContract getContract() {
		if (contract == null) {
			if (network == null) {
				Gateway gateway = builder.connect();
				network = gateway.getNetwork(properties.getChannel());
			}
			contract = new FabricContract((ContractImpl) network.getContract(properties.getChaincode()));
		}
		return contract;
	}

	public <T> FabricQueryResponse<T> query(FabricQueryRequest<T> queryRequest) {
		if (queryRequest == null) {
			return FabricQueryResponse.failure("Query request can not be null.");
		}
		Contract contract = null;
		try {
			queryRequest.checkValidate();

			contract = getContract();

			byte[] payload = contract.evaluateTransaction(queryRequest.function, queryRequest.arguments);

			return FabricQueryResponse.create(payload, queryRequest.type);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public <T> FabricQueryResponse<List<T>> queryArray(Class<T> type, String function, String... arguments) {
		try {
			FabricQueryRequest<T> request = new FabricQueryRequest<T>(type, function, arguments);
			return queryMany(request);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getMessage());
		}

	}

	public <T> FabricQueryResponse<List<T>> queryMany(FabricQueryRequest<T> queryRequest) {
		if (queryRequest == null) {
			return FabricQueryResponse.failure("Query request can not be null.");
		}

		Contract contract = null;

		try {
			queryRequest.checkValidate();

			contract = getContract();

			byte[] payloads = contract.evaluateTransaction(queryRequest.function, queryRequest.arguments);
			FabricQueryResponse<List<T>> results = FabricQueryResponse.many(payloads, queryRequest.type);

			if (results.isOk(true) && FabricHistory.class == queryRequest.type) {
				results.data.stream().forEach(t -> {
					String txid = ((FabricHistory) t).getTxId();
					BlockInfo block = queryBlockInfo(txid);
					((FabricHistory) t).setBlockInfo(block);
				});
			}

			return results;
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getMessage());
		} finally {
			aboutToShutdown();
		}
	}

	private BlockInfo queryBlockInfo(String txid) {
		if (txid == null) {
			return null;
		}
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();
			return channel.queryBlockByTransactionID(txid);
		} catch (Exception e) {
			return null;
		}
	}

	public FabricResponse execute(FabricRequest request) {
		if (request == null) {
			return FabricResponse.fail("Request can not be null");
		}
		try {
			FabricContract contract = getContract();
			String result = contract.executeTransaction(request.function, request.arguments);
			return FabricResponse.ok().setTransactionId(result);
		} catch (Exception e) {
			e.printStackTrace();
			return FabricResponse.fail(e.getMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<FabricLedger> queryBlockchainInfo() {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();
			BlockchainInfo info = channel.queryBlockchainInfo();

			FabricLedger ledger = new FabricLedger();
			ledger.setChannel(channel.getName());
			ledger.setHeight(info.getHeight());
			ledger.setCurrentHash(Hex.encodeHexString(info.getCurrentBlockHash()));
			ledger.setPreviousHash(Hex.encodeHexString(info.getPreviousBlockHash()));
			return FabricQueryResponse.success(ledger);
		} catch (Exception e) {
			e.printStackTrace();
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<FabricBlock> queryBlockByNumber(long blockNumber) {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();

			BlockInfo block = channel.queryBlockByNumber(blockNumber);
			return FabricQueryResponse.success(FabricBlock.create(block));

		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<FabricBlock> queryBlockByTransactionID(String txId) {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();

			BlockInfo block = channel.queryBlockByTransactionID(txId);
			return FabricQueryResponse.success(FabricBlock.create(block));
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<FabricBlock> queryBlockByHash(byte[] blockHash) {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();

			BlockInfo block = channel.queryBlockByHash(blockHash);

			return FabricQueryResponse.success(FabricBlock.create(block));
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<List<FabricTransaction>> queryTransactions(long blockNumber) {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();

			BlockInfo block = channel.queryBlockByNumber(blockNumber);
			List<FabricTransaction> transactions = new ArrayList<>();
			if (block != null) {
				int envelopeCount = block.getEnvelopeCount();
				for (int i = 0; i < envelopeCount; i++) {
					EnvelopeInfo envelope = block.getEnvelopeInfo(i);
					String txId = envelope.getTransactionID();
					FabricTransaction tx = new FabricTransaction();
					tx.setIndex(i);
					tx.setTxId(txId);
					tx.setChannel(envelope.getChannelId());
					IdentitiesInfo creator = envelope.getCreator();
					if (creator != null) {
						tx.setCreator(creator.getMspid());
					}
					tx.setDate(envelope.getTimestamp());
					EnvelopeType type = envelope.getType();
					if (type != null) {
						tx.setType(type.name());
					}
					tx.setValidationCode(envelope.getValidationCode());

					TransactionInfo transaction = channel.queryTransactionByID(txId);
					TxValidationCode validationCode = transaction.getValidationCode();
					if (validationCode != null) {
						tx.setValidationCode(validationCode.getNumber());
					}

					transactions.add(tx);
				}
			}
			return FabricQueryResponse.success(transactions);

		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(String txId) {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();

			BlockInfo block = channel.queryBlockByTransactionID(txId);

			if (block == null) {
				return FabricQueryResponse.failure("Unable to query block from txId=" + txId);
			}
			TransactionEnvelopeInfo envelopeInfo = null;
			Iterator<EnvelopeInfo> iterator = block.getEnvelopeInfos().iterator();
			while (iterator.hasNext()) {
				BlockInfo.EnvelopeInfo info = iterator.next();
				if (info.getTransactionID().equals(txId) && info instanceof TransactionEnvelopeInfo) {
					envelopeInfo = (TransactionEnvelopeInfo) info;
					break;
				}
			}
			FabricTransactionRWSet rwSet = new FabricTransactionRWSet();
			if (envelopeInfo != null) {
				envelopeInfo.getTransactionActionInfos().forEach(action -> {
					TxReadWriteSetInfo txReadWriteSet = action.getTxReadWriteSet();
					txReadWriteSet.getNsRwsetInfos().forEach(info -> {
						String namespace = info.getNamespace();
						if ("lscc".equals(namespace)) {
							return;
						}
						try {
							rwSet.setWrites(info.getRwset().getWritesList());
							rwSet.setReads(info.getRwset().getReadsList());
						} catch (InvalidProtocolBufferException e) {
						}
					});
				});
			}
			return FabricQueryResponse.success(rwSet);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}

	public FabricQueryResponse<FabricTransaction> queryTransactionInfo(String txId) {
		try {
			if (network == null) {
				getContract();
			}
			Channel channel = network.getChannel();

			BlockInfo block = channel.queryBlockByTransactionID(txId);

			if (block == null) {
				return FabricQueryResponse.failure("Unable to query block from txId=" + txId);
			}
			TransactionEnvelopeInfo envelopeInfo = null;
			Iterator<EnvelopeInfo> iterator = block.getEnvelopeInfos().iterator();
			while (iterator.hasNext()) {
				BlockInfo.EnvelopeInfo info = iterator.next();
				if (info.getTransactionID().equals(txId) && info instanceof TransactionEnvelopeInfo) {
					envelopeInfo = (TransactionEnvelopeInfo) info;
					break;
				}
			}
			FabricTransaction tx = new FabricTransaction();
			tx.setTxId(txId);
			tx.setChannel(envelopeInfo.getChannelId());
			IdentitiesInfo creator = envelopeInfo.getCreator();
			if (creator != null) {
				String mspid = creator.getMspid();
				tx.setCreator(mspid);
			}
			tx.setDate(envelopeInfo.getTimestamp());
			EnvelopeType type = envelopeInfo.getType();
			if (type != null) {
				tx.setType(type.name());
			}
			tx.setValidationCode(envelopeInfo.getValidationCode());

			TransactionInfo transaction = channel.queryTransactionByID(txId);
			TxValidationCode validationCode = transaction.getValidationCode();
			if (validationCode != null) {
				tx.setValidationCode(validationCode.getNumber());
			}
			return FabricQueryResponse.success(tx);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		} finally {
			aboutToShutdown();
		}
	}
}
