package io.github.ecsoya.fabric.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import io.github.ecsoya.fabric.annotation.FabricIgnore;

/**
 * @author Jin Liu (jin.liu@soyatec.com)
 * @see FabricIgnore
 * @see FabricGson
 */
public class FabricGsonSerializeExclusionStrategy implements ExclusionStrategy {

	public static final FabricGsonSerializeExclusionStrategy INSTANCE = new FabricGsonSerializeExclusionStrategy();

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		FabricIgnore ignore = f.getAnnotation(FabricIgnore.class);
		return ignore != null && ignore.serialize();
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		FabricIgnore ignore = clazz.getAnnotation(FabricIgnore.class);
		return ignore != null && ignore.serialize();
	}

}
