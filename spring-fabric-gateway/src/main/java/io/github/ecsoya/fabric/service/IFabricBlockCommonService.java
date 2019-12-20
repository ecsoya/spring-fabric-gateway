package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.IFabricObject;

public interface IFabricBlockCommonService<T extends IFabricObject> {

	/**
	 * 
	 * Get the history of object with key from ledger.
	 * 
	 * @param type TODO
	 * 
	 */
	public FabricQueryResponse<List<FabricHistory>> history(String key);

	/**
	 * 
	 * Query the block info
	 * 
	 * @param type TODO
	 */
	public FabricQueryResponse<FabricBlock> block(String key);

}
