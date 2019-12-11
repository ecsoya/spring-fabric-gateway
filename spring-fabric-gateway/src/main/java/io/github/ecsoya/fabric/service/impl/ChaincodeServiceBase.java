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
import io.github.ecsoya.fabric.bean.IFabricObject;
import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.json.JsonUtils;
import io.github.ecsoya.fabric.service.IChaincodeService;
import io.github.ecsoya.fabric.utils.TypeResolver;

public abstract class ChaincodeServiceBase<T extends IFabricObject> extends FabricBaseServiceImpl
		implements IChaincodeService<T> {

	private Class<T> genericType;

	public ChaincodeServiceBase(FabricContext fabricContext) {
		super(fabricContext);
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getGenericType() {
		if (genericType == null) {
			genericType = (Class<T>) TypeResolver.resolveRawArgument(IChaincodeService.class, getClass());
		}
		return genericType;
	}

	protected abstract FabricRequest newRequest(String function, String... arguments);

	protected abstract <X> FabricQueryRequest<X> newQueryRequest(Class<X> type, String function, String... arguments);

	@Override
	public FabricResponse create(T object) {
		if (object == null || object.getId() == null || object.getType() == null) {
			return FabricResponse.fail("Invalid arguments");
		}
		String key = object.getId();
		String value = JsonUtils.toJson(object);
		return execute(newRequest(FUNCTION_CREATE, object.getType(), key, value));
	}

	@Override
	public FabricResponse update(T object) {
		if (object == null || object.getId() == null || object.getType() == null) {
			return FabricResponse.fail("Invalid arguments");
		}
		String key = object.getId();
		String value = JsonUtils.toJson(object);
		return execute(newRequest(FUNCTION_UPDATE, object.getType(), key, value));
	}

	@Override
	public FabricResponse delete(String key, String type) {
		if (key == null) {
			return FabricResponse.fail("Invalid key for deleting");
		}
		if (type == null && getGenericType() != null) {
			type = getGenericType().getName();
		}
		if (type == null) {
			return FabricResponse.fail("Invalid type for deleting");
		}
		return execute(newRequest(FUNCTION_DELETE, type, key));
	}

	@Override
	public FabricQueryResponse<T> get(String key, String type) {
		if (key == null) {
			return FabricQueryResponse.failure("Invalid key of query.");
		}
		if (type == null && getGenericType() != null) {
			type = getGenericType().getName();
		}
		if (type == null) {
			return FabricQueryResponse.failure("Invalid type of query");
		}
		return query(newQueryRequest(getGenericType(), FUNCTION_GET, type, key));
	}

	@Override
	public FabricQueryResponse<List<T>> query(FabricQuery query) {
		if (query == null) {
			return FabricQueryResponse.failure("Invalid arguments");
		}
		String json = query.toJson();
		return queryMany(newQueryRequest(getGenericType(), FUNCTION_QUERY, json));
	}

	@Override
	public FabricPagination<T> pagination(FabricPaginationQuery<T> query) {
		FabricPagination<T> pagination = FabricPagination.create(query);
		if (query == null) {
			return pagination;
		}
		String json = query.toJson();
		FabricQueryResponse<List<T>> response = queryMany(newQueryRequest(getGenericType(), FUNCTION_QUERY, json,
				Integer.toString(query.getPageSize()), query.getBookmark()));
		if (response.isOk(true)) {
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
		return queryMany(newQueryRequest(getGenericType(), FUNCTION_LIST));
	}

	@Override
	public FabricQueryResponse<List<FabricHistory>> history(String key, String type) {
		if (key == null) {
			return FabricQueryResponse.failure("Invalid key of history");
		}
		if (type == null && getGenericType() != null) {
			type = getGenericType().getName();
		}
		if (type == null) {
			return FabricQueryResponse.failure("Invalid type of history");
		}
		return queryMany(newQueryRequest(FabricHistory.class, FUNCTION_HISTORY, type, key));
	}

	@Override
	public FabricQueryResponse<Number> count(FabricQuery query) {
		if (query == null) {
			return FabricQueryResponse.failure("Invalid arguments");
		}
		String json = query.toJson();
		return query(newQueryRequest(Number.class, FUNCTION_COUNT, json));
	}

	@Override
	public FabricQueryResponse<Boolean> exists(FabricQuery query) {
		if (query == null) {
			return FabricQueryResponse.failure("Invalid arguments");
		}
		String json = query.toJson();
		return query(newQueryRequest(Boolean.class, FUNCTION_EXISTS, json));
	}

	@Override
	public FabricQueryResponse<FabricBlock> block(String key, String type) {
		FabricQueryResponse<List<FabricHistory>> historyRes = history(key, type);
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
