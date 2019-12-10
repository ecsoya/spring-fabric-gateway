package io.ecsoya.fabric;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

public class FabricResponse {

	public static final int SUCCESS = 1;
	public static final int FAILURE = -505;

	public final int status;

	public final String errorMsg;

	private String transactionId;

	public FabricResponse(int status, String errorMsg) {
		this.status = status;
		this.errorMsg = errorMsg;
	}

	public boolean isOk() {
		return status == SUCCESS;
	}

	public boolean isOk(boolean all) {
		return isOk();
	}

	public FabricResponse setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public static FabricResponse fail(String errorMsg) {
		return new FabricResponse(FAILURE, errorMsg);
	}

	public static FabricResponse ok() {
		return new FabricResponse(SUCCESS, null);
	}

	public static FabricResponse create(TransactionEvent event) {
		if (event == null) {
			return fail("Invalid transaction event");
		}
		FabricResponse res = new FabricResponse(SUCCESS, null);
		res.setTransactionId(event.getTransactionID());
		return res;
	}

}
