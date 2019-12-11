package io.github.ecsoya.fabric.bean;

import org.hyperledger.fabric.sdk.BlockInfo;

import lombok.Data;

@Data
public class FabricHistory {

	private String txId;

//	private JsonElement value;

	private String timestamp;

	private boolean isDelete;

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
