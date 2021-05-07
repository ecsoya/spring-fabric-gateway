package io.github.ecsoya.fabric.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.impl.ContractImpl;
import org.hyperledger.fabric.protos.peer.TransactionPackage.TxValidationCode;
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

import io.github.ecsoya.fabric.FabricException;
import io.github.ecsoya.fabric.FabricQueryRequest;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricRequest;
import io.github.ecsoya.fabric.FabricResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.FabricLedger;
import io.github.ecsoya.fabric.bean.FabricTransaction;
import io.github.ecsoya.fabric.bean.FabricTransactionRWSet;
import io.github.ecsoya.fabric.chaincode.FunctionType;
import io.github.ecsoya.fabric.gateway.FabricContract;

public class FabricContext {
	private Logger logger = LoggerFactory.getLogger(FabricContext.class);

	private final FabricProperties properties;

	private Gateway.Builder builder;
	private Network network;
	private FabricContract contract;

	public FabricContext(FabricProperties properties) {
		this.properties = properties;
		if (properties == null) {
			properties = new FabricProperties();
		}
	}

	public FabricProperties getProperties() {
		return properties;
	}

	public String getFunction(FunctionType type) {
		if (type == null) {
			return null;
		}
		if (properties == null) {
			return type.getFunctionName();
		}
		FabricChaincodeProperties chaincode = properties.getChaincode();
		if (chaincode == null) {
			return type.getFunctionName();
		}
		FabricChaincodeFunctionProperties functions = chaincode.getFunctions();
		if (functions == null) {
			return type.getFunctionName();
		}
		switch (type) {
		case FUNCTION_CREATE:
			return functions.getCreate();
		case FUNCTION_UPDATE:
			return functions.getUpdate();
		case FUNCTION_COUNT:
			return functions.getCount();
		case FUNCTION_DELETE:
			return functions.getDelete();
		case FUNCTION_EXISTS:
			return functions.getExists();
		case FUNCTION_GET:
			return functions.getGet();
		case FUNCTION_HISTORY:
			return functions.getHistory();
		case FUNCTION_QUERY:
			return functions.getQuery();
		default:
			break;
		}
		return null;
	}

	@PostConstruct
	private void initialize() throws FabricException {

		logger.debug("Initialize Fabric Context");

		logger.debug("Initialize Fabric Context with properties: " + properties);

		String channel = properties.getChannel();
		if (channel == null || channel.equals("")) {
			throw new FabricException(
					"Initialize fabric gateway failed with invalid 'channel' name. Please make sure the channel is configured corrected by 'spring.fabric.channel'.");
		}

		FabricChaincodeProperties chaincode = properties.getChaincode();
		if (chaincode == null || chaincode.getIdentify() == null || chaincode.getIdentify().equals("")) {
			throw new FabricException(
					"Initialize fabric gateway failed with invalid 'chaincode' name. Please make sure the chaincode is configured corrected by 'spring.fabric.chaincode'.");
		}

		InputStream configFile = properties.getNetworkContents();
		if (configFile == null) {
			throw new FabricException(
					"Network config file can not be loaded. Please make sure 'spring.fabric.network.path' is configured.");
		}

		NetworkConfig config = null;
		try {
			config = NetworkConfig.fromYamlStream(configFile);
		} catch (Exception e) {
			throw new FabricException("Network config can not be loaded.", e);
		} finally {
			IOUtils.closeQuietly(configFile, e -> {
				logger.warn("Close inputstream failed");
			});
		}
		if (config == null) {
			throw new FabricException(
					"Network config can not be loaded. Please make sure 'spring.fabric.network.path' is correct.");
		}

		FabricGatewayProperties gatewayProps = properties.getGateway();

		FabricWalletProperties walletProps = gatewayProps.getWallet();

		String identityName = walletProps.getIdentity();
		if (identityName == null || identityName.equals("")) {
			identityName = "admin";
			logger.debug("Initialize Fabric Context: missing identity for wallet, and using default value: admin");
		}

		Wallet wallet = createWallet(config, walletProps);
		try {
			if (wallet == null || wallet.get(identityName) == null) {
				throw new FabricException("Initialize Wallet failed, there's no identity = '" + identityName
						+ "' exists in wallet directory: " + walletProps.getFile());
			}
		} catch (IOException e1) {
			throw new FabricException("Initialize Wallet failed, there's no identity = '" + identityName
					+ "' exists in wallet directory: " + walletProps.getFile());
		}

		logger.debug("Initialize Gateway... ");
		configFile = properties.getNetworkContents();
		try {
			builder = Gateway.createBuilder().identity(wallet, identityName).networkConfig(configFile);
			builder.commitTimeout(gatewayProps.getCommitTimeout(), TimeUnit.SECONDS);
			builder.discovery(gatewayProps.isDiscovery());
		} catch (IOException e) {
			throw new FabricException("Initialize Gateway failed", e);
		} finally {
			IOUtils.closeQuietly(configFile, e -> {
				logger.warn("Close inputstream failed");
			});
		}
		Gateway gateway = builder.connect();

		logger.debug("Initialize Network... ");

		network = gateway.getNetwork(channel);

	}

	private Wallet createWallet(NetworkConfig networkConfig, FabricWalletProperties walletConfig) {
		Wallet wallet = newFileSystemWallet(walletConfig);
		if (wallet == null) {
			wallet = newInMemoryWallet(networkConfig, walletConfig);
		}
		return wallet;
	}

	private Wallet newFileSystemWallet(FabricWalletProperties config) {
		if (config == null || config.isMemory() || config.getFile() == null || config.getFile().equals("")) {
			return null;
		} else {
			try {
				return Wallets.newFileSystemWallet(new File(config.getFile()).toPath());
			} catch (IOException e) {
				return null;
			}
		}
	}

	private Wallet newInMemoryWallet(NetworkConfig network, FabricWalletProperties walletProps) {
		Wallet wallet = Wallets.newInMemoryWallet();
		if (network != null) {
			String identityName = walletProps.getIdentity();
			if (identityName == null || identityName.equals("")) {
				identityName = "admin";
				logger.debug("Initialize Fabric Context: missing identity for wallet, and using default value: admin");
			}
			OrgInfo client = network.getClientOrganization();
			if (client != null) {
				UserInfo peerAdmin = client.getPeerAdmin();
				if (peerAdmin != null) {
					Enrollment enrollment = peerAdmin.getEnrollment();
					if (enrollment != null) {
						try {
							Identity admin = Identities.newX509Identity(peerAdmin.getMspId(), enrollment);
							wallet.put(identityName, admin);
						} catch (Exception e) {
							throw new FabricException("Initialize Wallet failed", e);
						}
						logger.debug("Initialize Wallet " + identityName);
					}
				}
			}
		}
		return wallet;
	}

	private FabricContract getContract() {
		if (contract == null) {
			if (network == null) {
				Gateway gateway = builder.connect();
				network = gateway.getNetwork(properties.getChannel());
			}
			contract = new FabricContract((ContractImpl) network.getContract(properties.getChaincode().getIdentify()));
			FabricGatewayProperties gatewayProps = properties.getGateway();
			if (gatewayProps != null) {
				contract.setOrdererTimeout(gatewayProps.getOrdererTimeout());
				contract.setProposalTimeout(gatewayProps.getProposalTimeout());
			}
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
			logger.error("Execute failed: " + e.getLocalizedMessage());
			return FabricResponse.fail(e.getMessage());
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
			ledger.setName(properties.getName());
			ledger.setOrgs(properties.getOrganizations());
			ledger.setChaincode(properties.getChaincode().getIdentify());
			ledger.setChaincodeName(properties.getChaincode().getName());
			ledger.setPeers(properties.getPeers());
			return FabricQueryResponse.success(ledger);
		} catch (Exception e) {
			logger.error("Execute failed: " + e.getLocalizedMessage());
			return FabricQueryResponse.failure(e.getLocalizedMessage());
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
		}
	}
}
