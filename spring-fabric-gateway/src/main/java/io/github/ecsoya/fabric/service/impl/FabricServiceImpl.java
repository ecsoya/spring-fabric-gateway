package io.github.ecsoya.fabric.service.impl;

import io.github.ecsoya.fabric.bean.FabricObject;
import io.github.ecsoya.fabric.config.FabricContext;
import io.github.ecsoya.fabric.service.IFabricService;

public class FabricServiceImpl extends ContextChaincodeService<FabricObject> implements IFabricService {

	public FabricServiceImpl(FabricContext fabricContext) {
		super(fabricContext);
	}

}
