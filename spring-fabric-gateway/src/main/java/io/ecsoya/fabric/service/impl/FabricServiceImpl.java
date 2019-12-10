package io.ecsoya.fabric.service.impl;

import io.ecsoya.fabric.bean.FabricObject;
import io.ecsoya.fabric.config.FabricContext;
import io.ecsoya.fabric.service.IFabricService;

public class FabricServiceImpl extends ContextChaincodeService<FabricObject> implements IFabricService {

	public FabricServiceImpl(FabricContext fabricContext) {
		super(fabricContext);
	}

}
