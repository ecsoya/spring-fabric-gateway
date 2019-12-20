package io.github.ecsoya.fabric.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ecsoya.fabric.FabricQueryRequest;
import io.github.ecsoya.fabric.FabricQueryResponse;
import io.github.ecsoya.fabric.FabricRequest;
import io.github.ecsoya.fabric.FabricResponse;
import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.service.IFabricBaseService;

public abstract class AbstractFabricBaseService implements IFabricBaseService {

	private Logger logger = LoggerFactory.getLogger(AbstractFabricBaseService.class);

	protected FabricContext fabricContext;

	public AbstractFabricBaseService(FabricContext fabricContext) {
		this.fabricContext = fabricContext;
	}

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
