package io.ecsoya.fabric.service;

import java.util.List;

import io.ecsoya.fabric.bean.FabricBlock;
import io.ecsoya.fabric.bean.FabricHistory;

public interface IFabricBlockService2 {

	List<FabricHistory> extHistory(String key, String type);

	FabricBlock extBlock(String key, String type);
}
