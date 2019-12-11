package io.github.ecsoya.fabric.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQueryRequest;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.FabricLedger;
import io.github.ecsoya.fabric.bean.FabricTransaction;
import io.github.ecsoya.fabric.bean.FabricTransactionRWSet;
import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.service.IChaincode;
import io.github.ecsoya.fabric.service.IFabricInfoService;
import io.github.ecsoya.fabric.utils.FabricUtil;

public class FabricInfoServiceImpl implements IFabricInfoService {

	private FabricContext fabricContext;

	public FabricInfoServiceImpl(FabricContext fabricContext) {
		this.fabricContext = fabricContext;
	}

	@Override
	public FabricQueryResponse<FabricLedger> queryFabricLedger() {
		try {
			return fabricContext.queryBlockchainInfo();
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricQueryResponse<FabricBlock> queryBlockByNumber(long blockNumber) {
		try {
			return fabricContext.queryBlockByNumber(blockNumber);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricQueryResponse<FabricBlock> queryBlockByTransactionID(String txId) {
		try {
			return fabricContext.queryBlockByTransactionID(txId);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricQueryResponse<FabricBlock> queryBlockByHash(byte[] blockHash) {
		try {
			return fabricContext.queryBlockByHash(blockHash);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricPagination<FabricBlock> queryBlocks(FabricPaginationQuery<FabricBlock> query) {

		long total = 0;
		byte[] lastHash = null;
		FabricQueryResponse<FabricLedger> queryRes = queryFabricLedger();
		if (queryRes.isOk(true)) {
			lastHash = FabricUtil.stringToHash(queryRes.data.getCurrentHash());
			total = queryRes.data.getHeight();
		}

		FabricPagination<FabricBlock> pagination = FabricPagination.create(query);

		String bookmark = query.getBookmark();
		if ("-".equals(bookmark)) {
			pagination.setData(Collections.emptyList());
		} else {
			int pageSize = query.getPageSize();

			byte[] startHash = null;
			if (bookmark != null && !bookmark.equals("")) {
				startHash = FabricUtil.stringToHash(bookmark);
			}

			if (startHash == null) {
				startHash = lastHash;
			}
			List<FabricBlock> datas = new ArrayList<>(pageSize);
			boolean hasError = false;
			boolean noMore = false;
			if (startHash != null) {
				int index = 0;
				while (index < pageSize) {
					FabricQueryResponse<FabricBlock> queryResp = queryBlockByHash(startHash);
					if (!queryRes.isOk(true)) {
						hasError = true;
						break;
					}
					FabricBlock block = queryResp.data;
					if (block == null) {
						noMore = true; // no more
						break;
					}
					block.setCurrentHash(FabricUtil.hashToString(startHash));
					datas.add(block);

					index++;
					startHash = FabricUtil.stringToHash(block.getPreviousHash());
				}
			}

			if (hasError) {
				pagination.setData(Collections.emptyList());
			} else {
				if (noMore) {
					pagination.setBookmark("-");
				} else {
					pagination.setBookmark(FabricUtil.hashToString(startHash));
				}
				pagination.setData(datas);
				pagination.setRecordsCount((int) total);
				pagination.updateTotalRecords();
			}
		}
		return pagination;
	}

	@Override
	public FabricQueryResponse<List<FabricTransaction>> queryTransactions(long blockNumber) {
		try {
			return fabricContext.queryTransactions(blockNumber);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricQueryResponse<FabricTransactionRWSet> queryTransactionRWSet(String txId) {
		try {
			return fabricContext.queryTransactionRWSet(txId);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricQueryResponse<List<FabricHistory>> queryHistory(String type, String key) {
		try {
			return fabricContext
					.queryMany(new FabricQueryRequest<>(FabricHistory.class, IChaincode.FUNCTION_HISTORY, type, key));
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

	@Override
	public FabricQueryResponse<FabricTransaction> queryTransactionInfo(String txid) {
		try {
			return fabricContext.queryTransactionInfo(txid);
		} catch (Exception e) {
			return FabricQueryResponse.failure(e.getLocalizedMessage());
		}
	}

}
