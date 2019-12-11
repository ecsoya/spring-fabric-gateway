package io.github.ecsoya.fabric.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonIgnoreDeserialization implements ExclusionStrategy {

	public static final JsonIgnoreDeserialization INSTANCE = new JsonIgnoreDeserialization();

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		JsonIgnore ignore = f.getAnnotation(JsonIgnore.class);
		return ignore != null && ignore.deserialize();
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		JsonIgnore ignore = clazz.getAnnotation(JsonIgnore.class);
		return ignore != null && ignore.deserialize();
	}

}
