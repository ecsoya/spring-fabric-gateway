package io.ecsoya.fabric.service;

import java.util.List;

import io.ecsoya.fabric.FabricQueryResponse;
import io.ecsoya.fabric.bean.FabricBlock;
import io.ecsoya.fabric.bean.FabricHistory;

public interface IFabricBlockService {

	/**
	 * 
	 * Get the history of object with key from ledger.
	 * 
	 * @param type TODO
	 * 
	 */
	public FabricQueryResponse<List<FabricHistory>> history(String key, String type);

	/**
	 * 
	 * Query the block info
	 * 
	 * @param type TODO
	 */
	public FabricQueryResponse<FabricBlock> block(String key, String type);

}
