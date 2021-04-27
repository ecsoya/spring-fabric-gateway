package io.github.ecsoya.fabric.gateway;

import static org.hyperledger.fabric.sdk.Channel.DiscoveryOptions.createDiscoveryOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.annotation.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.GatewayRuntimeException;
import org.hyperledger.fabric.gateway.Transaction;
import org.hyperledger.fabric.gateway.impl.ContractImpl;
import org.hyperledger.fabric.gateway.impl.GatewayImpl;
import org.hyperledger.fabric.gateway.impl.NetworkImpl;
import org.hyperledger.fabric.gateway.impl.TimePeriod;
import org.hyperledger.fabric.gateway.impl.TransactionImpl;
import org.hyperledger.fabric.gateway.spi.CommitHandler;
import org.hyperledger.fabric.gateway.spi.CommitHandlerFactory;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.ServiceDiscovery;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.ServiceDiscoveryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * Override the default {@link Contract} implementation.
 * 
 * @see FabricContract
 * 
 * @author Jin Liu (jin.liu@soyatec.com)
 */
@Slf4j
public class FabricTransaction implements Transaction {
	private static final Logger logger = LoggerFactory.getLogger(FabricTransaction.class);

	/**
	 * @since 1.0.6
	 */
	private long ordererTimeout = 60; // seconds

	/**
	 * @since 1.0.6
	 */
	private long proposalTimeout = 5; // seconds

	private TransactionImpl delegate;
	private TimePeriod commitTimeout;
	private CommitHandlerFactory commitHandlerFactory;
	private NetworkImpl network;
	private Channel channel;
	private GatewayImpl gateway;
	private ContractImpl contract;

	private Map<String, byte[]> transientData;
	private Collection<Peer> endorsingPeers = null;

	public FabricTransaction(TransactionImpl delegate, ContractImpl contract) {
		this.delegate = delegate;
		this.contract = contract;
		network = contract.getNetwork();
		channel = network.getChannel();
		gateway = network.getGateway();
		commitHandlerFactory = gateway.getCommitHandlerFactory();
		commitTimeout = gateway.getCommitTimeout();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public Transaction setTransient(Map<String, byte[]> transientData) {
		this.transientData = transientData;
		delegate.setTransient(transientData);
		return this;
	}

	@Override
	public Transaction setCommitTimeout(long timeout, TimeUnit timeUnit) {
		commitTimeout = new TimePeriod(timeout, timeUnit);
		delegate.setCommitTimeout(timeout, timeUnit);
		return this;
	}

	@Override
	public Transaction setEndorsingPeers(Collection<Peer> peers) {
		endorsingPeers = peers;
		delegate.setEndorsingPeers(peers);
		return this;
	}

	@Override
	public byte[] submit(String... args) throws ContractException, TimeoutException, InterruptedException {
		return delegate.submit(args);
	}

	@Override
	public byte[] evaluate(String... args) throws ContractException {
		return delegate.evaluate(args);
	}

	public String execute(String[] args) {
		try {
			TransactionProposalRequest request = newProposalRequest(args);

			Collection<ProposalResponse> proposalResponses = sendTransactionProposal(request);

			Collection<ProposalResponse> validResponses = validatePeerResponses(proposalResponses);
			ProposalResponse proposalResponse = validResponses.iterator().next();
			proposalResponse.getChaincodeActionResponsePayload();
			String transactionId = proposalResponse.getTransactionID();

			Channel.TransactionOptions transactionOptions = Channel.TransactionOptions.createTransactionOptions()
					.nOfEvents(Channel.NOfEvents.createNoEvents()); // Disable default commit wait behaviour

			CommitHandler commitHandler = commitHandlerFactory.create(transactionId, network);
			commitHandler.startListening();

			try {
				channel.sendTransaction(validResponses, transactionOptions).get(getOrdererTimeout(), TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				commitHandler.cancelListening();
				throw e;
			} catch (Exception e) {
				commitHandler.cancelListening();
				throw new ContractException("Failed to send transaction to the orderer", e);
			}

			commitHandler.waitForEvents(commitTimeout.getTime(), commitTimeout.getTimeUnit());

			return transactionId;
		} catch (Exception e1) {
			throw new GatewayRuntimeException(e1);
		}
	}

	private TransactionProposalRequest newProposalRequest(String[] args) {
		TransactionProposalRequest request = network.getGateway().getClient().newTransactionProposalRequest();
		configureRequest(request, args);
		if (transientData != null) {
			try {
				request.setTransientMap(transientData);
			} catch (InvalidArgumentException e) {
				// Only happens if transientData is null
				throw new IllegalStateException(e);
			}
		}
		return request;
	}

	private void configureRequest(TransactionRequest request, String[] args) {
		request.setChaincodeID(getChaincodeId());
		request.setFcn(getName());
		request.setArgs(args);
		request.setProposalWaitTime(getProposalTimeout() * 1000);
	}

	private ChaincodeID getChaincodeId() {
		return ChaincodeID.newBuilder().setName(contract.getChaincodeId()).build();
	}

	private Collection<ProposalResponse> sendTransactionProposal(TransactionProposalRequest request)
			throws InvalidArgumentException, ServiceDiscoveryException, ProposalException {
		if (endorsingPeers != null) {
			return channel.sendTransactionProposal(request, endorsingPeers);
		} else if (network.getGateway().isDiscoveryEnabled()) {
			Channel.DiscoveryOptions discoveryOptions = createDiscoveryOptions()
					.setEndorsementSelector(ServiceDiscovery.EndorsementSelector.ENDORSEMENT_SELECTION_RANDOM)
					.setForceDiscovery(true);
			return channel.sendTransactionProposalToEndorsers(request, discoveryOptions);
		} else {
			return channel.sendTransactionProposal(request);
		}
	}

	private Collection<ProposalResponse> validatePeerResponses(Collection<ProposalResponse> proposalResponses)
			throws ContractException {
		final Collection<ProposalResponse> validResponses = new ArrayList<>();
		final Collection<String> invalidResponseMsgs = new ArrayList<>();
		proposalResponses.forEach(response -> {
			String peerUrl = response.getPeer() != null ? response.getPeer().getUrl() : "<unknown>";
			if (response.getStatus().equals(ChaincodeResponse.Status.SUCCESS)) {
				log.debug(String.format("validatePeerResponses: valid response from peer %s", peerUrl));
				validResponses.add(response);
			} else {
				logger.warn(String.format("validatePeerResponses: invalid response from peer %s, message %s", peerUrl,
						response.getMessage()));
				invalidResponseMsgs.add(response.getMessage());
			}
		});

		if (validResponses.size() < 1) {
			String msg = String.format("No valid proposal responses received. %d peer error responses: %s",
					invalidResponseMsgs.size(), String.join("; ", invalidResponseMsgs));
			logger.error(msg);
			throw new ContractException(msg);
		}

		return validResponses;
	}

	public long getOrdererTimeout() {
		return ordererTimeout;
	}

	public void setOrdererTimeout(long ordererTimeout) {
		this.ordererTimeout = ordererTimeout;
	}

	public long getProposalTimeout() {
		return proposalTimeout;
	}

	public void setProposalTimeout(long proposalTimeout) {
		this.proposalTimeout = proposalTimeout;
	}

	@Override
	public String getTransactionId() {
		return delegate.getTransactionId();
	}

	@Override
	public Transaction setCommitHandler(CommitHandlerFactory commitHandler) {
		return delegate.setCommitHandler(commitHandler);
	}

}
