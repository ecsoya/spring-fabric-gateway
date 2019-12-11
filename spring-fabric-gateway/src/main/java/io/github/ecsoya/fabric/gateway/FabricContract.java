package io.github.ecsoya.fabric.gateway;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Transaction;
import org.hyperledger.fabric.gateway.impl.ContractImpl;
import org.hyperledger.fabric.gateway.impl.TransactionImpl;
import org.hyperledger.fabric.gateway.spi.Checkpointer;

/**
 * 
 * Overrite the default {@link Contract} to provided transactionId after
 * execution.
 * 
 * @author ecsoya
 *
 */
public class FabricContract implements Contract {

	private ContractImpl delegate;

	public FabricContract(ContractImpl delegate) {
		this.delegate = delegate;
	}

	@Override
	public Transaction createTransaction(String name) {
		return delegate.createTransaction(name);
	}

	public String executeTransaction(String name, String... args)
			throws ContractException, TimeoutException, InterruptedException {
		TransactionImpl tx = (TransactionImpl) delegate.createTransaction(name);
		return new FabricTransaction(tx, delegate).execute(args);
	}

	@Override
	public byte[] submitTransaction(String name, String... args)
			throws ContractException, TimeoutException, InterruptedException {
		return delegate.submitTransaction(name, args);
	}

	@Override
	public byte[] evaluateTransaction(String name, String... args) throws ContractException {
		return delegate.evaluateTransaction(name, args);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(Consumer<ContractEvent> listener) {
		return delegate.addContractListener(listener);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(Consumer<ContractEvent> listener, String eventName) {
		return delegate.addContractListener(listener, eventName);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(Consumer<ContractEvent> listener, Pattern eventNamePattern) {
		return delegate.addContractListener(listener, eventNamePattern);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(Checkpointer checkpointer, Consumer<ContractEvent> listener)
			throws IOException {
		return delegate.addContractListener(checkpointer, listener);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(Checkpointer checkpointer, Consumer<ContractEvent> listener,
			String eventName) throws IOException {
		return delegate.addContractListener(checkpointer, listener, eventName);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(Checkpointer checkpointer, Consumer<ContractEvent> listener,
			Pattern eventNamePattern) throws IOException {
		return delegate.addContractListener(checkpointer, listener, eventNamePattern);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(long startBlock, Consumer<ContractEvent> listener) {
		return delegate.addContractListener(startBlock, listener);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(long startBlock, Consumer<ContractEvent> listener,
			String eventName) {
		return delegate.addContractListener(startBlock, listener, eventName);
	}

	@Override
	public Consumer<ContractEvent> addContractListener(long startBlock, Consumer<ContractEvent> listener,
			Pattern eventNamePattern) {
		return delegate.addContractListener(startBlock, listener, eventNamePattern);
	}

	@Override
	public void removeContractListener(Consumer<ContractEvent> listener) {
		delegate.removeContractListener(listener);
	}

}
