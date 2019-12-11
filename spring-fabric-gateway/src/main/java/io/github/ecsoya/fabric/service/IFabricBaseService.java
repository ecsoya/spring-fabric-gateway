package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.FabricQueryRequest;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricRequest;
import io.github.ecsoya.fabric.FabricResponse;

public interface IFabricBaseService {

	FabricResponse execute(FabricRequest request);

	public <T> FabricQueryResponse<T> query(FabricQueryRequest<T> request);

	public <T> FabricQueryResponse<List<T>> queryMany(FabricQueryRequest<T> request);

}
