package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.FabricLedger;
import io.github.ecsoya.fabric.bean.FabricTransaction;
import io.github.ecsoya.fabric.bean.FabricTransactionRWSet;

/**
 * Default service to provided fabric blockchain info, such as blocks,
 * transactions and ledger.
 * 
 * @author ecsoya
 *
 */
public interface IFabricInfoService {

	/**
	 * Query Fabric Info.
	 */
	FabricQueryResponse<FabricLedger> queryFabricLedger();

	/**
	 * Query fabric block by using block number.
	 */
	FabricQueryResponse<FabricBlock> queryBlockByNumber(long blockNumber);

	/**
	 * Query fabric block by using transaction id.
	 */
	FabricQueryResponse<FabricBlock> queryBlockByTransactionID(String txId);

	/**
	 * Query fabric block by using block hash.
	 */
	FabricQueryResponse<FabricBlock> queryBlockByHash(byte[] blockHash);

	/**
	 * Paging query fabric blocks.
	 * 
	 */
	FabricPagination<FabricBlock> queryBlocks(FabricPaginationQuery<FabricBlock> query);

	/**
	 * Query all transactions of a block number.
	 */
	FabricQueryResponse<List<FabricTransaction>> queryTransactions(long blockNumber);

	/**
	 * Query transaction reads and writes of a transaction id.
	 */
	FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(String txId);

	/**
	 * Query history of object with given key and type.
	 */
	FabricQueryResponse<List<FabricHistory>> queryHistory(String type, String key);

	/**
	 * Query transaction with id.
	 */
	FabricQueryResponse<FabricTransaction> queryTransaction(String txid);
}
