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

	FabricQueryResponse<FabricLedger> queryFabricLedger();

	FabricQueryResponse<FabricBlock> queryBlockByNumber(long blockNumber);

	FabricQueryResponse<FabricBlock> queryBlockByTransactionID(String txId);

	FabricQueryResponse<FabricBlock> queryBlockByHash(byte[] blockHash);

	FabricPagination<FabricBlock> queryBlocks(FabricPaginationQuery<FabricBlock> query);

	FabricQueryResponse<List<FabricTransaction>> queryTransactions(long blockNumber);

	FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(String txId);

	FabricQueryResponse<List<FabricHistory>> queryHistory(String type, String key);

	FabricQueryResponse<FabricTransaction> queryTransactionInfo(String txid);
}
