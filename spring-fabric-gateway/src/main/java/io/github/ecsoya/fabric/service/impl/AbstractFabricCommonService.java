package io.github.ecsoya.fabric.service.impl;

import java.util.List;

import io.github.ecsoya.fabric.FabricPagination;
import io.github.ecsoya.fabric.FabricPaginationQuery;
import io.github.ecsoya.fabric.FabricQuery;
import io.github.ecsoya.fabric.FabricQueryRequest;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricRequest;
import io.github.ecsoya.fabric.FabricResponse;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.IFabricIdentityObject;
import io.github.ecsoya.fabric.bean.IFabricObject;
import io.github.ecsoya.fabric.chaincode.FunctionType;
import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.json.FabricWrapper;
import io.github.ecsoya.fabric.service.IFabricCommonService;
import io.github.ecsoya.fabric.utils.FabricUtil;
import io.github.ecsoya.fabric.utils.TypeResolver;

public abstract class AbstractFabricCommonService<T extends IFabricObject> extends AbstractFabricBaseService
		implements IFabricCommonService<T> {

	private Class<T> genericType;

	private String fabricType;

	public AbstractFabricCommonService(FabricContext fabricContext) {
		super(fabricContext);
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getGenericType() {
		if (genericType == null) {
			genericType = (Class<T>) TypeResolver.resolveRawArgument(IFabricCommonService.class, getClass());
		}
		return genericType;
	}

	protected String getFabricType() {
		if (fabricType == null) {
			fabricType = FabricUtil.resolveFabricType(getGenericType());
		}
		return fabricType;
	}

	protected abstract FabricRequest newRequest(String function, String... arguments);

	protected abstract <X> FabricQueryRequest<X> newQueryRequest(Class<X> type, String function, String... arguments);

	protected String getFunction(FunctionType type) {
		return fabricContext.getFunction(type);
	}

	@Override
	public FabricResponse create(T object) {
		FabricWrapper wrapper = new FabricWrapper(object);
		if (!wrapper.isValid()) {
			return FabricResponse.fail("Invalid arguments");
		}
		String key = wrapper.getKey();
		String value = wrapper.getValue();
		String type = wrapper.getType();
		return execute(newRequest(getFunction(FunctionType.FUNCTION_CREATE), type, key, value));
	}

	@Override
	public FabricResponse update(T object) {
		FabricWrapper wrapper = new FabricWrapper(object);
		if (!wrapper.isValid()) {
			return FabricResponse.fail("Invalid arguments");
		}
		String key = wrapper.getKey();
		String value = wrapper.getValue();
		String type = wrapper.getType();
		return execute(newRequest(getFunction(FunctionType.FUNCTION_UPDATE), type, key, value));
	}

	@Override
	public FabricResponse delete(String key) {
		if (key == null) {
			return FabricResponse.fail("Invalid key for deleting");
		}
		return execute(newRequest(getFunction(FunctionType.FUNCTION_DELETE), getFabricType(), key));
	}

	@Override
	public FabricQueryResponse<T> get(String key) {
		if (key == null) {
			return FabricQueryResponse.failure("Invalid key of query.");
		}
		FabricQueryResponse<T> response = query(
				newQueryRequest(getGenericType(), getFunction(FunctionType.FUNCTION_GET), getFabricType(), key));
		if (response.isOk(true)) {
			T t = response.data;
			if (t instanceof IFabricIdentityObject) {
				FabricQueryResponse<FabricBlock> blockRes = block(t.getId());
				if (blockRes.isOk(true)) {
					((IFabricIdentityObject) t).setIdentity(blockRes.data);
				}
			}
		}
		return response;
	}

	@Override
	public FabricQueryResponse<List<T>> query(FabricQuery query) {
		if (query == null) {
			return FabricQueryResponse.failure("Invalid arguments");
		}
		String selector = query.selector();
		FabricQueryResponse<List<T>> result = queryMany(
				newQueryRequest(getGenericType(), getFunction(FunctionType.FUNCTION_QUERY), selector));
		if (result.isOk(true)) {
			result.data.forEach(t -> {
				if (t instanceof IFabricIdentityObject) {
					FabricQueryResponse<FabricBlock> blockRes = block(t.getId());
					if (blockRes.isOk(true)) {
						((IFabricIdentityObject) t).setIdentity(blockRes.data);
					}
				}
			});
		}
		return result;
	}

	@Override
	public FabricPagination<T> pagination(FabricPaginationQuery<T> query) {
		FabricPagination<T> pagination = FabricPagination.create(query);
		if (query == null) {
			return pagination;
		}
		String selector = query.selector();
		FabricQueryResponse<List<T>> response = queryMany(
				newQueryRequest(getGenericType(), getFunction(FunctionType.FUNCTION_QUERY), selector,
						Integer.toString(query.getPageSize()), query.getBookmark()));
		if (response.isOk(true)) {
			response.data.forEach(t -> {
				if (t instanceof IFabricIdentityObject) {
					FabricQueryResponse<FabricBlock> blockRes = block(t.getId());
					if (blockRes.isOk(true)) {
						((IFabricIdentityObject) t).setIdentity(blockRes.data);
					}
				}
			});

			pagination.setData(response.data);
		}
		if (response.metadata != null) {
			pagination.setBookmark(response.metadata.getBookmark());
			pagination.setRecordsCount(response.metadata.getRecordsCount());
			pagination.updateTotalRecords();
		}
		return pagination;
	}

	@Override
	public FabricQueryResponse<List<T>> list() {
		return queryMany(newQueryRequest(getGenericType(), getFunction(FunctionType.FUNCTION_LIST)));
	}

	@Override
	public FabricQueryResponse<List<FabricHistory>> history(String key) {
		if (key == null) {
			return FabricQueryResponse.failure("Invalid key of history");
		}
		return queryMany(
				newQueryRequest(FabricHistory.class, getFunction(FunctionType.FUNCTION_HISTORY), getFabricType(), key));
	}

	@Override
	public FabricQueryResponse<Number> count(FabricQuery query) {
		if (query == null) {
			return FabricQueryResponse.failure("Invalid arguments");
		}
		String selector = query.selector();
		return query(newQueryRequest(Number.class, getFunction(FunctionType.FUNCTION_COUNT), selector));
	}

	@Override
	public FabricQueryResponse<Boolean> exists(FabricQuery query) {
		if (query == null) {
			return FabricQueryResponse.failure("Invalid arguments");
		}
		String selector = query.selector();
		return query(newQueryRequest(Boolean.class, getFunction(FunctionType.FUNCTION_EXISTS), selector));
	}

	@Override
	public FabricQueryResponse<FabricBlock> block(String key) {
		FabricQueryResponse<List<FabricHistory>> historyRes = history(key);
		if (historyRes.isOk(true)) {
			List<FabricHistory> histories = historyRes.data;
			long blockHeight = 0;
			FabricBlock result = null;
			for (FabricHistory fabricHistory : histories) {
				FabricBlock block = fabricHistory.getBlock();
				if (block == null) {
					continue;
				}
				if (result == null) {
					result = block;
					blockHeight = block.getBlockNumber();
				} else if (block.getBlockNumber() < blockHeight) {
					result = block;
				}
			}
			if (result != null) {
				return FabricQueryResponse.success(result);
			} else {
				return FabricQueryResponse.failure("No block found for key: " + key);
			}
		}
		return FabricQueryResponse
				.failure("Could not get block for key: " + key + " with error: " + historyRes.errorMsg);
	}

}
