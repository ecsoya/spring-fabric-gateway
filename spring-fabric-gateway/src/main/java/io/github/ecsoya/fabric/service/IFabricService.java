package io.github.ecsoya.fabric.service;

import io.github.ecsoya.fabric.bean.FabricObject;

/**
 * Default service which holding the bean {@link FabricObject}
 * 
 * @author ecsoya
 *
 */
public interface IFabricService
		extends IFabricBaseService, IChaincodeService<FabricObject>, IChaincodeService2<FabricObject> {

}
