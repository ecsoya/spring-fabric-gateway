package io.github.ecsoya.fabric.bean;

import io.github.ecsoya.fabric.annotation.FabricIgnore;
import lombok.Data;

@Data
public class FabricIdentityObject implements IFabricIdentityObject {

	@FabricIgnore
	private String id;

	@FabricIgnore
	private FabricIdentity identity;
}
