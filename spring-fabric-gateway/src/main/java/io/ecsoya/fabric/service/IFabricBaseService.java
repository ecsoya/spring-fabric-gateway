package io.ecsoya.fabric.service;

import java.util.List;

import io.ecsoya.fabric.FabricQueryRequest;
import io.ecsoya.fabric.FabricQueryResponse;
import io.ecsoya.fabric.FabricRequest;
import io.ecsoya.fabric.FabricResponse;

public interface IFabricBaseService {

	FabricResponse execute(FabricRequest request);

	public <T> FabricQueryResponse<T> query(FabricQueryRequest<T> request);

	public <T> FabricQueryResponse<List<T>> queryMany(FabricQueryRequest<T> request);

}
