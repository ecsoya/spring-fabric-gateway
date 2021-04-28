package io.ecsoya.github.fabric.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class Query {

	@Property
	private String[] data;

	@Property
	private QueryMeta meta;

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public QueryMeta getMeta() {
		return meta;
	}

	public void setMeta(QueryMeta meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return JSON.stringify(this);
	}
}
