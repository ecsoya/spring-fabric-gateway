package io.github.ecsoya.fabric.explorer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.FabricLedger;
import io.github.ecsoya.fabric.bean.FabricTransaction;
import io.github.ecsoya.fabric.bean.FabricTransactionRWSet;
import io.github.ecsoya.fabric.service.IFabricInfoService;

@RestController
public class IndexController {

	@Autowired
	private IFabricInfoService fabricService;

	@RequestMapping("/queryfabricledger")
	public FabricQueryResponse<FabricLedger> queryFabricLedger() {
		return fabricService.queryFabricLedger();
	}

	@RequestMapping("/queryblocks")
	public FabricPagination<FabricBlock> queryBlocks(@RequestBody FabricPaginationQuery<FabricBlock> query) {
		return fabricService.queryBlocks(query);
	}

	@RequestMapping("/queryblockinfo")
	public FabricQueryResponse<FabricBlock> queryBlockInfo(long blockNumber) {
		return fabricService.queryBlockByNumber(blockNumber);
	}

	@RequestMapping("/querytransactioninfo")
	public FabricQueryResponse<FabricTransaction> queryTransactionInfo(String txid) {
		return fabricService.queryTransactionInfo(txid);
	}

	@RequestMapping("/querytransactions")
	public FabricQueryResponse<List<FabricTransaction>> queryTransactions(long blockNumber) {
		return fabricService.queryTransactions(blockNumber);
	}

	@RequestMapping("/querytransactionrwset")
	public FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(String txid) {
		return fabricService.queryTransactionRWSet(txid);
	}

	@RequestMapping("/queryhistories")
	public FabricQueryResponse<List<FabricHistory>> queryHistories(String type, String key) {
		return fabricService.queryHistory(type, key);
	}
}
