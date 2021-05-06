package io.github.ecsoya.fabric.explorer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.FabricLedger;
import io.github.ecsoya.fabric.bean.FabricTransaction;
import io.github.ecsoya.fabric.bean.FabricTransactionRWSet;
import io.github.ecsoya.fabric.explorer.FabricExplorerProperties;
import io.github.ecsoya.fabric.service.IFabricInfoService;

public class FabricExplorerController {

	@Autowired
	private IFabricInfoService fabricService;

	@Autowired
	private FabricExplorerProperties properties;

	private String baseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String path = request.getContextPath();
		return scheme + "://" + serverName + ":" + port + path;
	}

	@GetMapping("/")
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("explorer/index");
		model.addAllObjects(properties.toMap());
		model.addObject("baseURL", baseUrl(request));
		return model;
	}

	@GetMapping("/block")
	public ModelAndView block(HttpServletRequest request, long height) {
		ModelAndView model = new ModelAndView("explorer/block");
		model.addAllObjects(properties.toMap());
		model.addObject("height", height);
		model.addObject("baseURL", baseUrl(request));
		return model;
	}

	@GetMapping("/tx")
	public ModelAndView tx(HttpServletRequest request, String txid) {
		ModelAndView model = new ModelAndView("explorer/tx");
		model.addAllObjects(properties.toMap());
		model.addObject("txid", txid);
		model.addObject("baseURL", baseUrl(request));
		return model;
	}

	@RequestMapping("/history")
	public ModelAndView history(HttpServletRequest request, String key, String type) {
		ModelAndView model = new ModelAndView("explorer/history");
		model.addAllObjects(properties.toMap());
		model.addObject("key", key);
		model.addObject("type", type);
		model.addObject("baseURL", baseUrl(request));
		return model;
	}

	@PostMapping("/query/ledger")
	@ResponseBody
	public FabricQueryResponse<FabricLedger> queryFabricLedger() {
		return fabricService.queryFabricLedger();
	}

	@PostMapping("/query/blockpage")
	@ResponseBody
	public FabricPagination<FabricBlock> queryBlocks(@RequestBody FabricPaginationQuery<FabricBlock> query) {
		return fabricService.queryBlocks(query);
	}

	@GetMapping("/query/block/{number}")
	@ResponseBody
	public FabricQueryResponse<FabricBlock> queryBlockInfo(@PathVariable("number") long blockNumber) {
		return fabricService.queryBlockByNumber(blockNumber);
	}

	@GetMapping("/query/tx/{id}")
	@ResponseBody
	public FabricQueryResponse<FabricTransaction> queryTransactionInfo(@PathVariable("id") String txid) {
		return fabricService.queryTransaction(txid);
	}

	@GetMapping("/query/transactions/{number}")
	@ResponseBody
	public FabricQueryResponse<List<FabricTransaction>> queryTransactions(@PathVariable("number") long blockNumber) {
		return fabricService.queryTransactions(blockNumber);
	}

	@GetMapping("/query/txrw/{id}")
	@ResponseBody
	public FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(@PathVariable("id") String txid) {
		return fabricService.queryTransactionRWSet(txid);
	}

	@GetMapping("/query/history")
	@ResponseBody
	public FabricQueryResponse<List<FabricHistory>> queryHistories(String type, String key) {
		return fabricService.queryHistory(type, key);
	}
}
