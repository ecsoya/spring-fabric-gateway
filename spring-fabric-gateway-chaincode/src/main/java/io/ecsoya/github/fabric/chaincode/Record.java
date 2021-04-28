package io.ecsoya.github.fabric.chaincode;

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
	public String toString() {
		return JSON.stringify(this);
	}

}
