package io.github.ecsoya.fabric.service.impl;

import java.util.Collections;
import java.util.List;

import io.github.ecsoya.fabric.FabricQuery;
import io.github.ecsoya.fabric.FabricQueryRequest;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricRequest;
import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.IFabricObject;
import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.service.IFabricService;

public abstract class AbstractFabricService<T extends IFabricObject> extends AbstractFabricCommonService<T>
		implements IFabricService<T> {

	public AbstractFabricService(FabricContext fabricContext) {
		super(fabricContext);
	}

	@Override
	protected FabricRequest newRequest(String function, String... arguments) {
		return new FabricRequest(function, arguments);
	}

	@Override
	protected <F> FabricQueryRequest<F> newQueryRequest(Class<F> type, String function, String... arguments) {
		return new FabricQueryRequest<>(type, function, arguments);
	}

	@Override
	public int extCreate(T object) {
		return create(object).status;
	}

	@Override
	public int extUpdate(T object) {
		return update(object).status;
	}

	@Override
	public int extDelete(String key) {
		return delete(key).status;
	}

	@Override
	public T extGet(String key) {
		return get(key).data;
	}

	@Override
	public List<FabricHistory> extHistory(String key) {
		return history(key).data;
	}

	@Override
	public List<T> extQuery(FabricQuery query) {
		FabricQueryResponse<List<T>> result = query(query);
		if (result.isOk(true)) {
			return result.data;
		}
		return Collections.emptyList();
	}

	@Override
	public List<T> extList() {
		return list().data;
	}

	@Override
	public int extCount(FabricQuery query) {
		Number count = count(query).data;
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}

	@Override
	public boolean extExists(FabricQuery query) {
		Boolean exists = exists(query).data;
		if (exists != null) {
			return exists.booleanValue();
		}
		return false;
	}

	@Override
	public FabricBlock extBlock(String key) {
		return block(key).data;
	}

}
