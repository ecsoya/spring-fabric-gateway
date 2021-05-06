package io.ecsoya.github.fabric.chaincode;

import java.util.Arrays;
import java.util.Objects;

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
	public int hashCode() {
		return Objects.hash(getData(), getMeta());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		Query other = (Query) obj;

		return Objects.deepEquals(new Object[] { getData(), getMeta() },
				new Object[] { other.getData(), other.getMeta() });
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [data="
				+ Arrays.toString(data) + ", meta=" + meta + "]";
	}
}
