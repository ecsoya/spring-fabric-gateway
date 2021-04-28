package io.ecsoya.github.fabric.chaincode;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.google.gson.annotations.SerializedName;

@DataType
public class Record {

	@Property
	@SerializedName(value = "Key")
	private String key;

	@Property
	@SerializedName(value = "Record")
	private String record;

	public Record(String key, String record) {
		this.key = key;
		this.record = record;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getKey(), getRecord());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		Record other = (Record) obj;

		return Objects.deepEquals(new Object[] { getKey(), getRecord() },
				new Object[] { other.getKey(), other.getRecord() });
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [key=" + key + ", record="
				+ record + "]";
	}

}
