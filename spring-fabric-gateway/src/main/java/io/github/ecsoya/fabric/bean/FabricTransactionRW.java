package io.github.ecsoya.fabric.bean;

import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.KVRead;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.KVWrite;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.Version;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.ecsoya.fabric.json.FabricGson;
import lombok.Data;

/**
 * Fabric transaction reads/writes.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricTransactionRW {

	private int index;

	/**
	 * Type of {@link IFabricObject}
	 */
	private String type = "";

	/**
	 * Id of {@link IFabricObject}
	 */
	private String key = "";

	/**
	 * Stringify value of values for {@link IFabricObject}
	 */
	private String value = "";

	/**
	 * Is deleted or not for Writes.
	 */
	private String remarks = "";

	public static FabricTransactionRW fromRead(int index, KVRead read) {
		if (read == null) {
			return null;
		}
		FabricTransactionRW txRead = new FabricTransactionRW();
		txRead.setIndex(index);
		String key = read.getKey();
		String[] split = key.split(DELIMITER, 0);
		if (split.length >= 2) {
			txRead.setKey(split[split.length - 1]);
			txRead.setType(split[split.length - 2]);
		} else {
			txRead.setKey(key);
		}
		Version version = read.getVersion();
		if (version != null) {
			txRead.setValue("blockNum: " + version.getBlockNum() + ", txNum: " + version.getTxNum());
		}
		return txRead;
	}

	public static FabricTransactionRW fromWrite(int index, KVWrite write) {
		if (write == null) {
			return null;
		}
		FabricTransactionRW txWrite = new FabricTransactionRW();
		txWrite.setIndex(index);
		String compositeKey = write.getKey();
		String[] split = compositeKey.split(DELIMITER, 0);
		if (split.length >= 2) {
			txWrite.setKey(split[split.length - 1]);
			txWrite.setType(split[split.length - 2]);
		} else {
			txWrite.setKey(compositeKey);
		}
		txWrite.setValue(simplifier(write.getValue().toStringUtf8()));
		txWrite.setRemarks(Boolean.toString(write.getIsDelete()));
		return txWrite;
	}

	/**
	 * Try to extract 'values' from given value for {@link IFabricObject}.
	 */
	private static String simplifier(String value) {
		try {
			JsonElement tree = JsonParser.parseString(value);
			if (tree.isJsonObject()) {
				JsonObject obj = tree.getAsJsonObject();
				JsonElement valuesObj = obj.get("values");
				if (valuesObj != null) {
					return FabricGson.stringify(valuesObj);
				}
			}
		} catch (Exception e) {
			return value;
		}
		return value;

	}

	private static final String DELIMITER = new String(Character.toChars(Character.MIN_CODE_POINT));
}
