package io.github.ecsoya.fabric.service;

import io.github.ecsoya.fabric.bean.FabricObject;

public interface IFabricService
		extends IFabricBaseService, IChaincodeService<FabricObject>, IChaincodeService2<FabricObject> {

}
