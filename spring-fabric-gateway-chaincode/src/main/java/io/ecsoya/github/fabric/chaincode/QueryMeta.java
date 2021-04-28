package io.ecsoya.github.fabric.chaincode;

import java.util.Objects;

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
	public int hashCode() {
		return Objects.hash(getRecordsCount(), getBookmark());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		QueryMeta other = (QueryMeta) obj;

		return Objects.deepEquals(new Object[] { getRecordsCount(), getBookmark() },
				new Object[] { other.getRecordsCount(), other.getBookmark() });
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [recordsCount="
				+ recordsCount + ", bookmark=" + bookmark + "]";
	}
}
