package io.ecsoya.github.fabric.chaincode;

import java.util.Objects;

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
	public int hashCode() {
		return Objects.hash(getTxId(), getTimestamp(), getIsDelete(), getValue());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		History other = (History) obj;

		return Objects.deepEquals(new Object[] { getTxId(), getTimestamp(), getIsDelete(), getValue() },
				new Object[] { other.getTxId(), other.getTimestamp(), other.getIsDelete(), other.getValue() });
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [txId=" + txId
				+ ", timestamp=" + timestamp + ", isDelete=" + isDelete + ", value=" + value + "]";
	}

}
