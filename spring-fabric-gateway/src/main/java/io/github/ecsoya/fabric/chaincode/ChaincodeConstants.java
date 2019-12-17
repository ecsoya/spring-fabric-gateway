package io.github.ecsoya.fabric.chaincode;

/**
 * 
 * Default chaincode function names.
 * 
 * Default chaincode path: src/chaincode/common/chaincode.go
 * 
 *
 * {@link https://ecsoya.github.io/fabric/pages/gateway.html#%E9%93%BE%E7%A0%81%E5%87%BD%E6%95%B0%E5%8F%8A%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E}
 * 
 * @author ecsoya
 *
 */
public interface ChaincodeConstants {

	/**
	 * Function for create.
	 */
	String FUNCTION_CREATE = "create";

	/**
	 * Single element query by id and type.
	 */
	String FUNCTION_GET = "get";

	/**
	 * Delete function with id and type.
	 */
	String FUNCTION_DELETE = "delete";

	/**
	 * Update function.
	 */
	String FUNCTION_UPDATE = "update";

	/**
	 * Query(Page query) function name.
	 */
	String FUNCTION_QUERY = "query";

	/**
	 * Query all (limit 100 records by fabric chaincodes defaultly.)
	 */
	String FUNCTION_LIST = "list";

	/**
	 * Query history of element function.
	 */
	String FUNCTION_HISTORY = "history";

	/**
	 * Query count of element function
	 */
	String FUNCTION_COUNT = "count";

	/**
	 * Check if exists of element function
	 */
	String FUNCTION_EXISTS = "exists";

}
