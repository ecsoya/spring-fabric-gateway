package io.github.ecsoya.fabric.bean;

import org.hyperledger.fabric.sdk.BlockInfo;

import lombok.Data;

/**
 * 
 * Editing history for a key.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricHistory {

	/**
	 * Transaction id of current history.
	 */
	private String txId;

//	private JsonElement value;

	/**
	 * Modify timestamp of current history.
	 */
	private String timestamp;

	/**
	 * Mark is deleted or not.
	 */
	private boolean isDelete;

	/**
	 * Block object.
	 */
	private FabricBlock block;

	public void setBlockInfo(BlockInfo blockInfo) {
		if (blockInfo == null) {
			return;
		}
		block = FabricBlock.create(blockInfo);

		block.setCurrentTxId(txId);
		block.setCurrentTxTimestamp(timestamp);
	}

//	public <T> T extract(Class<T> type) {
//		if (value == null || type == null) {
//			return null;
//		}
//		Gson gson = new Gson();
//		return gson.fromJson(value, type);
//	}
}
