package io.github.ecsoya.fabric.chaincode;

/**
 * 
 * Default chaincode function names.
 * 
 * Default chaincode path: src/chaincode/common/chaincode.go
 * 
 * @author ecsoya
 *
 */
public interface ChaincodeConstants {

	String FUNCTION_CREATE = "create";
	String FUNCTION_GET = "get";
	String FUNCTION_DELETE = "delete";
	String FUNCTION_UPDATE = "update";
	String FUNCTION_QUERY = "query";
	String FUNCTION_LIST = "list";
	String FUNCTION_HISTORY = "history";

	String FUNCTION_COUNT = "count";
	String FUNCTION_EXISTS = "exists";

}
