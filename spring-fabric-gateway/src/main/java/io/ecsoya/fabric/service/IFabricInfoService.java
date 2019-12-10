package io.ecsoya.fabric.service;

import java.util.List;

import io.ecsoya.fabric.FabricPagination;
import io.ecsoya.fabric.FabricPaginationQuery;
import io.ecsoya.fabric.FabricQueryResponse;
import io.ecsoya.fabric.bean.FabricBlock;
import io.ecsoya.fabric.bean.FabricHistory;
import io.ecsoya.fabric.bean.FabricLedger;
import io.ecsoya.fabric.bean.FabricTransaction;
import io.ecsoya.fabric.bean.FabricTransactionRWSet;

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
