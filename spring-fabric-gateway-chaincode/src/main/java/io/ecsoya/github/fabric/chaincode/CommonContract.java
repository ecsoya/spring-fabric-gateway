package io.ecsoya.github.fabric.chaincode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.ContractRuntimeException;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.protos.peer.ChaincodeShim.QueryResponseMetadata;
import org.hyperledger.fabric.shim.Chaincode;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.QueryResultsIteratorWithMetadata;

@Contract(name = "CommonContract", info = @Info(title = "Spring-Fabric-Gateway Common Contract", description = "The common contract with CRUD actions...", version = "1.0.0-SNAPSHOT", license = @License(name = "Apache 2.0 License", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), contact = @Contact(email = "angryred@qq.com", name = "angryred", url = "https://ecsoya.github.io/fabric/")))
@Default
public class CommonContract implements ContractInterface {

	private static final String OBJECT_TYPE = "type~key";

	/**
	 * 
	 * Initialize
	 * 
	 * @param context
	 * @return Chaincode.Response
	 */
	@Transaction
	public Chaincode.Response init(Context context) {
		return ResponseUtils.newSuccessResponse("Chaincode say 'hi' to you");
	}

	/**
	 * 
	 * Create new object with type, key and value.
	 * 
	 * @param context
	 * @param args
	 * @return Chaincode.Response
	 */
	@Transaction
	public Chaincode.Response create(Context context, String[] args) {
		if (args == null || args.length < 3) {
			throw new ContractRuntimeException("Incorrect number of arguments. Expecting 3 [type, key, value]");
		}
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, args);
		String value = args[2];
		if (value == null) {
			return ResponseUtils.newErrorResponse("Value is empty");
		}
		stub.putStringState(compositeKey.toString(), value);
		return ResponseUtils.newSuccessResponse("Created success");
	}

	private String getCompositeKey(ChaincodeStub stub, String[] args) {
		if (args == null || args.length < 2) {
			throw new ContractRuntimeException("Incorrect number of arguments. At least 2 [type, key, ...]");
		}
		String clazz = args[0];
		String key = args[1];
		CompositeKey compositeKey = stub.createCompositeKey(OBJECT_TYPE, clazz, key);
		if (compositeKey != null) {
			return compositeKey.toString();
		}
		throw new ContractRuntimeException("Create compositeKey failed");
	}

	/**
	 * 
	 * Get object with [type, key]
	 * 
	 * @param context
	 * @param args
	 * @return Chaincode.Response
	 */
	@Transaction
	public Chaincode.Response get(Context context, String[] args) {
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, args);
		byte[] state = stub.getState(compositeKey);
		return ResponseUtils.newSuccessResponse(state);
	}

	/**
	 * Update object with [type, key, value]
	 * 
	 * @param context
	 * @param args
	 * @return Chaincode.Response
	 */
	@Transaction
	public Chaincode.Response update(Context context, String[] args) {
		if (args == null || args.length < 3) {
			throw new ContractRuntimeException("Incorrect number of arguments. Expecting 3 [type, key, value]");
		}
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, args);
		stub.delState(compositeKey);
		String value = args[2];
		if (value == null) {
			return ResponseUtils.newErrorResponse("Value is empty");
		}
		stub.putStringState(compositeKey.toString(), value);
		return ResponseUtils.newSuccessResponse("Updated success");
	}

	/**
	 * Delete objects
	 * 
	 * @param context
	 * @param args
	 * @return Chaincode.Response
	 */
	@Transaction
	public Chaincode.Response delete(Context context, String[] args) {
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, args);
		if (compositeKey == null) {
			return ResponseUtils.newErrorResponse("delete failed");
		}
		stub.delState(compositeKey);
		return ResponseUtils.newSuccessResponse("Deleted success");

	}

	/**
	 * @param context
	 * @param args
	 * @return
	 */
	@Transaction
	public Chaincode.Response list(Context context, String[] args) {
		String startKey = "";
		String endKey = "";
		if (args != null && args.length > 0 && args[0] != null) {
			startKey = args[0];
		}
		if (args != null && args.length > 1 && args[1] != null) {
			endKey = args[1];
		}
		ChaincodeStub stub = context.getStub();
		List<Record> records = new ArrayList<Record>();
		QueryResultsIterator<KeyValue> stateByRange = stub.getStateByRange(startKey, endKey);
		stateByRange.forEach(value -> {
			records.add(new Record(value.getKey(), value.getStringValue()));
		});

		return ResponseUtils.newSuccessResponse("success", JSON.payload(records));
	}

	/**
	 * @param context
	 * @param args
	 * @return
	 */
	@Transaction
	public Chaincode.Response query(Context context, String[] args) {
		if (args == null || args.length < 1) {
			throw new ContractRuntimeException(
					"Incorrect number of arguments. At least 1 argument with query string should be set.");
		}
		ChaincodeStub stub = context.getStub();
		String query = args[0];
		Integer pageSize = null;
		String bookmark = null;
		if (args.length > 1) {
			try {
				pageSize = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
			}
		}
		if (args.length > 2) {
			bookmark = args[2];
		}
		Query response = new Query();
		if (pageSize != null && pageSize.intValue() > -1) {
			QueryResultsIteratorWithMetadata<KeyValue> queryResultWithPagination = stub
					.getQueryResultWithPagination(query, pageSize, bookmark);
			List<String> values = new ArrayList<String>();
			queryResultWithPagination.forEach(keyValue -> {
				values.add(keyValue.getStringValue());
			});
			response.setData(values.toArray(new String[values.size()]));
			QueryResponseMetadata metadata = queryResultWithPagination.getMetadata();
			if (metadata != null) {
				QueryMeta meta = new QueryMeta();
				meta.setRecordsCount(metadata.getFetchedRecordsCount());
				meta.setBookmark(metadata.getBookmark());
				response.setMeta(meta);
			}
		} else {
			QueryResultsIterator<KeyValue> queryResult = stub.getQueryResult(query);
			List<String> values = new ArrayList<String>();
			queryResult.forEach(keyValue -> {
				values.add(keyValue.getStringValue());
			});
			response.setData(values.toArray(new String[values.size()]));
		}
		return ResponseUtils.newSuccessResponse("success", JSON.payload(response));
	}

	/**
	 * 
	 * Get count of object [query]
	 * 
	 * @param context
	 * @param args
	 * @return
	 */
	@Transaction
	public Chaincode.Response count(Context context, String[] args) {
		if (args == null || args.length < 1 || args[0] == null) {
			throw new ContractRuntimeException(
					"Incorrect number of arguments. At least 1 argument with query string should be set.");
		}
		ChaincodeStub stub = context.getStub();
		String query = args[0];

		QueryResultsIterator<KeyValue> queryResult = stub.getQueryResult(query);
		if (queryResult == null) {
			return ResponseUtils.newSuccessResponse("0");
		}
		int count = 0;
		Iterator<KeyValue> it = queryResult.iterator();
		while (it.hasNext()) {
			it.next();
			count++;
		}
		return ResponseUtils.newSuccessResponse(Integer.toString(count));
	}

	/**
	 * Check exists for query
	 * 
	 * @param context
	 * @param args
	 * @return
	 */
	@Transaction
	public Chaincode.Response exists(Context context, String[] args) {
		if (args == null || args.length < 1 || args[0] == null) {
			throw new ContractRuntimeException(
					"Incorrect number of arguments. At least 1 argument with query string should be set.");
		}
		ChaincodeStub stub = context.getStub();
		String query = args[0];

		QueryResultsIterator<KeyValue> queryResult = stub.getQueryResult(query);
		if (queryResult == null) {
			return ResponseUtils.newSuccessResponse(Boolean.FALSE.toString());
		}
		Iterator<KeyValue> it = queryResult.iterator();
		return ResponseUtils.newSuccessResponse(Boolean.toString(it.hasNext()));
	}

	/**
	 * 
	 * Get History of object [type, key]
	 * 
	 * @param context
	 * @param args
	 * @return Chaincode.Response
	 */
	@Transaction
	public Chaincode.Response history(Context context, String[] args) {
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, args);
		List<History> histories = new ArrayList<>();
		QueryResultsIterator<KeyModification> historyIterator = stub.getHistoryForKey(compositeKey);
		if (historyIterator != null) {
			historyIterator.forEach(mod -> {
				History his = new History();
				Instant timestamp = mod.getTimestamp();
				if (timestamp != null) {
					his.setTimestamp(timestamp.toEpochMilli());
				}
				his.setTxId(mod.getTxId());
				if (!mod.isDeleted()) {
					his.setValue(mod.getStringValue());
				}
				his.setIsDelete(mod.isDeleted());
			});
		}
		return ResponseUtils.newSuccessResponse("success", JSON.payload(histories));
	}
}
