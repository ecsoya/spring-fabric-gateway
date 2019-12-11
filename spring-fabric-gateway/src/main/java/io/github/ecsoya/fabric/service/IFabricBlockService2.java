package io.github.ecsoya.fabric.service;

import java.util.List;

import io.github.ecsoya.fabric.bean.FabricBlock;
import io.github.ecsoya.fabric.bean.FabricHistory;

public interface IFabricBlockService2 {

	List<FabricHistory> extHistory(String key, String type);

	FabricBlock extBlock(String key, String type);
}
