package io.ecsoya.fabric.service;

import java.util.List;

import io.ecsoya.fabric.FabricQuery;
import io.ecsoya.fabric.bean.IFabricObject;

public interface IChaincodeService2<T extends IFabricObject> extends IChaincode, IFabricBlockService2 {

	/**
	 * Create object and send to ledger.
	 * 
	 * @param object
	 */
	public int extCreate(T object);

	/**
	 * 
	 * Update give object at ledger.
	 */
	public int extUpdate(T object);

	/**
	 * Delete the object with given key from ledger.
	 * 
	 * @param type TODO
	 */
	public int extDelete(String key, String type);

	/**
	 * 
	 * Get the object with key from ledger.
	 * 
	 * @param type TODO
	 * 
	 */
	public T extGet(String key, String type);

	/**
	 * 
	 * Query objects from ledger, the query will be build to JSON selector for
	 * CouchDB.
	 * 
	 */
	public List<T> extQuery(FabricQuery query);

	/**
	 * List all objects from ledger, this method should never be used.
	 */
	public List<T> extList();

	/**
	 * 
	 * Query objects from ledger, the query will be build to JSON selector for
	 * CouchDB.
	 * 
	 */
	public int extCount(FabricQuery query);

	/**
	 * 
	 * Query objects from ledger, the query will be build to JSON selector for
	 * CouchDB.
	 * 
	 */
	public boolean extExists(FabricQuery query);
}
