package io.ecsoya.fabric.service;

import io.ecsoya.fabric.bean.FabricObject;

public interface IFabricService
		extends IFabricBaseService, IChaincodeService<FabricObject>, IChaincodeService2<FabricObject> {

}
