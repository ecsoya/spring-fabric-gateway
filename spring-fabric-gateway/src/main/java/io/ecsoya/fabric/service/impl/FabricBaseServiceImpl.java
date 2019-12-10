package io.ecsoya.fabric.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.ecsoya.fabric.FabricQueryRequest;
import io.ecsoya.fabric.FabricQueryResponse;
import io.ecsoya.fabric.FabricRequest;
import io.ecsoya.fabric.FabricResponse;
import io.ecsoya.fabric.config.FabricContext;
import io.ecsoya.fabric.service.IFabricBaseService;

public abstract class FabricBaseServiceImpl implements IFabricBaseService {

	private Logger logger = LoggerFactory.getLogger(FabricBaseServiceImpl.class);

	@Autowired
	protected FabricContext fabricContext;

	@Override
	public FabricResponse execute(FabricRequest request) {
		try {
			logger.debug("Fabric execute " + request.function + " ==>");
			FabricResponse response = fabricContext.execute(request);
			if (response.isOk()) {
				logger.debug("Fabric execute " + request.function + " <== OK");
			} else {
				logger.debug("Fabric execute " + request.function + " <== FAILED, " + response.errorMsg);
			}
			return response;
		} catch (Exception e) {
			logger.error("Fabric execute " + request.function + " <==", e);
			return FabricResponse.fail(e.getMessage());
		}
	}

	@Override
	public <T> FabricQueryResponse<T> query(FabricQueryRequest<T> request) {
		try {
			logger.debug("Fabric query " + request.function + " ==>");
			FabricQueryResponse<T> response = fabricContext.query(request);
			if (response.isOk()) {
				logger.debug("Fabric query " + request.function + " <== OK");
			} else {
				logger.debug("Fabric query " + request.function + " <== FAILED, " + response.errorMsg);
			}
			return response;
		} catch (Exception e) {
			logger.error("Fabric query " + request.function + " <==", e);
			return FabricQueryResponse.failure(e.getMessage());
		}
	}

	@Override
	public <T> FabricQueryResponse<List<T>> queryMany(FabricQueryRequest<T> request) {
		try {
			logger.debug("Fabric query many " + request.function + " ==>");
			FabricQueryResponse<List<T>> response = fabricContext.queryMany(request);
			if (response.isOk()) {
				logger.debug("Fabric query many " + request.function + " <== OK");
			} else {
				logger.debug("Fabric query many " + request.function + " <== FAILED, " + response.errorMsg);
			}
			return response;
		} catch (Exception e) {
			logger.error("Fabric query many " + request.function + " <==", e);
			return FabricQueryResponse.failure(e.getMessage());
		}
	}

}
