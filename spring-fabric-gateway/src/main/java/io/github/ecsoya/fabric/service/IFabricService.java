package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.FabricQuery;
import io.github.ecsoya.fabric.bean.IFabricObject;

public interface IFabricService<T extends IFabricObject> extends IFabricBlockService<T> {

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
	 */
	public int extDelete(String key);

	/**
	 * 
	 * Get the object with key from ledger.
	 * 
	 */
	public T extGet(String key);

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
