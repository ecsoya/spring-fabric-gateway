package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.FabricObject;

/**
 * Default service which holding the bean {@link FabricObject}
 * 
 * @author ecsoya
 *
 */
public interface IFabricObjectService
		extends IFabricBaseService, IFabricCommonService<FabricObject>, IFabricService<FabricObject> {
	/**
	 * Delete the object with given key from ledger.
	 * 
	 */
	public int extDelete(String key, String type);

	/**
	 * 
	 * Get the object with key from ledger.
	 * 
	 */
	public FabricObject extGet(String key, String type);

	/**
	 * Delete the object with given key from ledger.
	 * 
	 */
	public FabricResponse delete(String key, String type);

	/**
	 * 
	 * Get the object with key from ledger.
	 * 
	 * 
	 */
	public FabricQueryResponse<FabricObject> get(String key, String type);

	/**
	 * 
	 * Get the history of object with key from ledger.
	 * 
	 * 
	 */
	public FabricQueryResponse<List<FabricHistory>> history(String key, String type);

	/**
	 * 
	 * Query the block info
	 * 
	 */
	public FabricQueryResponse<FabricBlock> block(String key, String type);

	List<FabricHistory> extHistory(String key, String type);

	FabricBlock extBlock(String key, String type);
}
