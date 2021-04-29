package io.ecsoya.github.fabric.chaincode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.Logger;
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
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.QueryResultsIteratorWithMetadata;

@Contract(name = "CommonContract", info = @Info(title = "Spring-Fabric-Gateway Common Contract", description = "The common contract with CRUD actions...", version = "1.0.0", license = @License(name = "Apache 2.0 License", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), contact = @Contact(email = "angryred@qq.com", name = "angryred", url = "https://ecsoya.github.io/fabric/")))
@Default
public class CommonContract implements ContractInterface {

	private static final Logger log = Logger.getLogger(CommonContract.class);
	private static final String OBJECT_TYPE = "type~key";

	/**
	 * 
	 * Initialize
	 * 
	 * @param context
	 * @return Chaincode.Response
	 */
	@Transaction
	public String init(Context context) {
		return "Chaincode say 'hi' to you";
	}

	/**
	 * 
	 * Create new object with type, key and value.
	 * 
	 * @param context
	 * @return Chaincode.Response
	 */
	@Transaction
	public Boolean create(Context context, String type, String key, String value) {
		log.info("CommonContract.create: type=" + type + ", key=" + key + ", value=" + value);
		if (type == null || key == null) {
			throw new ContractRuntimeException("Incorrect number of arguments. Expecting 3 [type, key, value]");
		}
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, type, key);
		if (value == null) {
			return Boolean.FALSE;
		}
		stub.putStringState(compositeKey.toString(), value);
		return Boolean.TRUE;
	}

	private String getCompositeKey(ChaincodeStub stub, String type, String key) {
		if (type == null || key == null) {
			throw new ContractRuntimeException("Incorrect number of arguments. At least 2 [type, key, ...]");
		}
		CompositeKey compositeKey = stub.createCompositeKey(OBJECT_TYPE, type, key);
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
	 * @return Chaincode.Response
	 */
	@Transaction
	public byte[] get(Context context, String type, String key) {
		log.info("CommonContract.get: type=" + type + ", key=" + key);
		if (type == null || key == null) {
			throw new ContractRuntimeException("Incorrect number of arguments. At least 2 [type, key, ...]");
		}
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, type, key);
		return stub.getState(compositeKey);
	}

	/**
	 * Update object with [type, key, value]
	 * 
	 * @param context
	 * @return Chaincode.Response
	 */
	@Transaction
	public Boolean update(Context context, String type, String key, String value) {
		log.info("CommonContract.update: type=" + type + ", key=" + key + ", value=" + value);
		if (type == null || key == null) {
			throw new ContractRuntimeException("Incorrect number of arguments. Expecting 3 [type, key, value]");
		}
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, type, key);
		stub.delState(compositeKey);
		if (value == null) {
			return Boolean.FALSE;
		}
		stub.putStringState(compositeKey.toString(), value);
		return Boolean.TRUE;
	}

	/**
	 * Delete objects
	 * 
	 * @param context
	 * @return Chaincode.Response
	 */
	@Transaction
	public Boolean delete(Context context, String type, String key) {
		log.info("CommonContract.delete: type=" + type + ", key=" + key);
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, type, key);
		if (compositeKey == null) {
			return Boolean.FALSE;
		}
		stub.delState(compositeKey);
		return Boolean.TRUE;

	}

	/**
	 * List all
	 * 
	 * @param context
	 * @return Records
	 */
	@Transaction
	public Record[] list(Context context, String startKey, String endKey) {
		log.info("CommonContract.list: startKey=" + startKey + ", endKey=" + endKey);
		if (startKey == null) {
			startKey = "";
		}
		if (endKey == null) {
			endKey = "";
		}
		ChaincodeStub stub = context.getStub();
		List<Record> records = new ArrayList<Record>();
		QueryResultsIterator<KeyValue> stateByRange = stub.getStateByRange(startKey, endKey);
		stateByRange.forEach(value -> {
			records.add(new Record(value.getKey(), value.getStringValue()));
		});
		log.info("CommonContract.list: " + JSON.stringify(records));
		return records.toArray(new Record[records.size()]);
	}

	/**
	 * Query objects, support couchdb.
	 * 
	 * @param context
	 * @return {@link Query}
	 */
	@Transaction
	public Query query(Context context, String query, Integer pageSize, String bookmark) {
		log.info("CommonContract.query: query=" + query + ", pageSize=" + pageSize + ", bookmark=" + bookmark);
		if (query == null) {
			throw new ContractRuntimeException(
					"Incorrect number of arguments. At least 1 argument with query string should be set.");
		}
		ChaincodeStub stub = context.getStub();
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
		log.info("CommonContract.query: response=" + response);
		return response;
	}

	/**
	 * 
	 * Get count of object [query]
	 * 
	 * @param context
	 * @return the count of query
	 */
	@Transaction
	public Integer count(Context context, String query) {
		log.info("CommonContract.count: query=" + query);
		if (query == null) {
			throw new ContractRuntimeException(
					"Incorrect number of arguments. At least 1 argument with query string should be set.");
		}
		ChaincodeStub stub = context.getStub();

		QueryResultsIterator<KeyValue> queryResult = stub.getQueryResult(query);
		if (queryResult == null) {
			return 0;
		}
		int count = 0;
		Iterator<KeyValue> it = queryResult.iterator();
		while (it.hasNext()) {
			it.next();
			count++;
		}
		log.info("CommonContract.count: " + count);
		return count;
	}

	/**
	 * Check exists for query
	 * 
	 * @param context
	 * @return boolean
	 */
	@Transaction
	public Boolean exists(Context context, String query) {
		log.info("CommonContract.exist: query=" + query);
		if (query == null) {
			throw new ContractRuntimeException(
					"Incorrect number of arguments. At least 1 argument with query string should be set.");
		}
		ChaincodeStub stub = context.getStub();

		QueryResultsIterator<KeyValue> queryResult = stub.getQueryResult(query);
		if (queryResult == null) {
			return Boolean.FALSE;
		}
		Iterator<KeyValue> it = queryResult.iterator();
		boolean exists = it.hasNext();
		log.info("CommonContract.exists: " + exists);
		return exists;
	}

	/**
	 * 
	 * Get History of object [type, key]
	 * 
	 * @param context
	 * @return Chaincode.Response
	 */
	@Transaction
	public History[] history(Context context, String type, String key) {
		log.info("CommonContract.history: type=" + type + ", key=" + key);
		ChaincodeStub stub = context.getStub();
		String compositeKey = getCompositeKey(stub, type, key);
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
				histories.add(his);
			});
		}
		log.info("CommonContract.history: " + JSON.stringify(histories));
		return histories.toArray(new History[histories.size()]);
	}
}
