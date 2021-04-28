package io.ecsoya.github.fabric.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class History {

	@Property
	private String txId;

	@Property
	private String value;

	@Property
	private Long timestamp;

	@Property
	private Boolean isDelete;

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return JSON.stringify(this);
	}

}
