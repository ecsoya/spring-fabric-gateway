package io.ecsoya.github.fabric.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class QueryMeta {

	@Property
	private Integer recordsCount;

	@Property
	private String bookmark;

	public Integer getRecordsCount() {
		return recordsCount;
	}

	public void setRecordsCount(Integer recordsCount) {
		this.recordsCount = recordsCount;
	}

	public String getBookmark() {
		return bookmark;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	@Override
	public String toString() {
		return JSON.stringify(this);
	}
}
