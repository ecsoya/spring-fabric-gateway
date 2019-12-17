package io.github.ecsoya.fabric.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * @author Jin Liu (jin.liu@soyatec.com)
 * @see JsonIgnore
 * @see JsonUtils
 */
public class JsonIgnoreSerialization implements ExclusionStrategy {

	public static final JsonIgnoreSerialization INSTANCE = new JsonIgnoreSerialization();

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		JsonIgnore ignore = f.getAnnotation(JsonIgnore.class);
		return ignore != null && ignore.serialize();
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		JsonIgnore ignore = clazz.getAnnotation(JsonIgnore.class);
		return ignore != null && ignore.serialize();
	}

}
