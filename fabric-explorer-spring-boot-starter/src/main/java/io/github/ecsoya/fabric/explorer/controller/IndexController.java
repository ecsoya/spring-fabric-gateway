package io.github.ecsoya.fabric.explorer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping("/query/ledger")
	public FabricQueryResponse<FabricLedger> queryFabricLedger() {
		return fabricService.queryFabricLedger();
	}

	@RequestMapping("/query/blockpage")
	public FabricPagination<FabricBlock> queryBlocks(@RequestBody FabricPaginationQuery<FabricBlock> query) {
		return fabricService.queryBlocks(query);
	}

	@RequestMapping("/query/block/{number}")
	public FabricQueryResponse<FabricBlock> queryBlockInfo(@PathVariable("number") long blockNumber) {
		return fabricService.queryBlockByNumber(blockNumber);
	}

	@RequestMapping("/query/tx/{id}")
	public FabricQueryResponse<FabricTransaction> queryTransactionInfo(@PathVariable("id") String txid) {
		return fabricService.queryTransactionInfo(txid);
	}

	@RequestMapping("/query/transactions/{number}")
	public FabricQueryResponse<List<FabricTransaction>> queryTransactions(@PathVariable("number") long blockNumber) {
		return fabricService.queryTransactions(blockNumber);
	}

	@RequestMapping("/query/txrw/{id}")
	public FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(@PathVariable("id") String txid) {
		return fabricService.queryTransactionRWSet(txid);
	}

	@RequestMapping("/query/history")
	public FabricQueryResponse<List<FabricHistory>> queryHistories(String type, String key) {
		return fabricService.queryHistory(type, key);
	}
}
