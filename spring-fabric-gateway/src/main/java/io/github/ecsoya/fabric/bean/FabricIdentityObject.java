package io.github.ecsoya.fabric.bean;

import com.google.gson.annotations.Expose;

import io.github.ecsoya.fabric.annotation.FabricIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class FabricIdentityObject implements IFabricIdentityObject {

	@FabricIgnore
	private String id;

	@FabricIgnore
	private FabricIdentity identity;

	@Expose(serialize = true, deserialize = false)
	@Setter(AccessLevel.NONE)
	private String type = getType();
}
