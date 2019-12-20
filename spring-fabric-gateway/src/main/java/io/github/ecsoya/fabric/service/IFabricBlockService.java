package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;
import io.github.ecsoya.fabric.bean.IFabricObject;

public interface IFabricBlockService<T extends IFabricObject> {

	List<FabricHistory> extHistory(String key);

	FabricBlock extBlock(String key);
}
