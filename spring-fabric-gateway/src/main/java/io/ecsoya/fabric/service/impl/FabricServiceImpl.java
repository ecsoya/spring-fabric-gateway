package io.ecsoya.fabric.service.impl;

import org.springframework.stereotype.Service;

import io.ecsoya.fabric.bean.FabricObject;
import io.ecsoya.fabric.service.IFabricService;

@Service
public class FabricServiceImpl extends ContextChaincodeService<FabricObject> implements IFabricService {

}
