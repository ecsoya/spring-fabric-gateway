package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQuery;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricResponse;
import io.github.ecsoya.fabric.bean.IFabricObject;
import io.github.ecsoya.fabric.chaincode.ChaincodeConstants;

public interface IChaincodeService<T extends IFabricObject> extends ChaincodeConstants, IFabricBlockService {

	/**
	 * Create object and send to ledger.
	 * 
	 * @param object
	 */
	public FabricResponse create(T object);

	/**
	 * 
	 * Update give object at ledger.
	 */
	public FabricResponse update(T object);

	/**
	 * Delete the object with given key from ledger.
	 * 
	 * @param type TODO
	 */
	public FabricResponse delete(String key, String type);

	/**
	 * 
	 * Get the object with key from ledger.
	 * 
	 * @param type TODO
	 * 
	 */
	public FabricQueryResponse<T> get(String key, String type);

	/**
	 * 
	 * Query objects from ledger, the query will be build to JSON selector for
	 * CouchDB.
	 * 
	 */
	public FabricQueryResponse<List<T>> query(FabricQuery query);

	/**
	 * 
	 * Query objects from ledger with pagination, the query will be build to JSON
	 * selector for CouchDB.
	 * 
	 */
	public FabricPagination<T> pagination(FabricPaginationQuery<T> query);

	/**
	 * 
	 * Query objects from ledger, the query will be build to JSON selector for
	 * CouchDB.
	 * 
	 */
	public FabricQueryResponse<Number> count(FabricQuery query);

	/**
	 * 
	 * Query objects from ledger, the query will be build to JSON selector for
	 * CouchDB.
	 * 
	 */
	public FabricQueryResponse<Boolean> exists(FabricQuery query);

	/**
	 * List all objects from ledger, this method should never be used.
	 */
	public FabricQueryResponse<List<T>> list();

}
