package io.github.ecsoya.fabric.json;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtils {

	private static Gson gson;

	static {
		gson = new GsonBuilder().enableComplexMapKeySerialization().setDateFormat(DateFormat.LONG)
				.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setVersion(1.0)
				.addSerializationExclusionStrategy(JsonIgnoreSerialization.INSTANCE)
				.addDeserializationExclusionStrategy(JsonIgnoreDeserialization.INSTANCE).create();
	}

	private JsonUtils() {
	}

	public static String toJson(Object object) {
		if (object == null) {
			return null;
		}
		return gson.toJson(object);
	}

	public static JsonElement toJsonTree(Object object) {
		if (object == null) {
			return null;
		}
		return gson.toJsonTree(object);
	}

	public static <T> T fromJson(String json, Class<T> type) {
		if (json == null) {
			return null;
		}

		return gson.fromJson(json, type);
	}

	public static <T> T fromJson(JsonElement json, Class<T> type) {
		if (json == null) {
			return null;
		}

		return gson.fromJson(json, type);
	}

	public static <T> T[] fromJsonArray(String json, Class<T> type) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		return fromJsonArray(element, type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] fromJsonArray(JsonElement element, Class<T> type) {
		List<T> results = new ArrayList<>();
		if (element == null || !element.isJsonArray()) {
			return (T[]) Array.newInstance(type, 0);
		}

		JsonArray array = element.getAsJsonArray();
		for (JsonElement child : array) {
			T value = null;
			if (child.isJsonObject() && child.getAsJsonObject().has("Key") && child.getAsJsonObject().has("Record")) {
				value = gson.fromJson(child.getAsJsonObject().get("Record"), type);
			} else {
				value = gson.fromJson(child, type);
			}
			results.add(value);
		}
		return results.toArray((T[]) Array.newInstance(type, results.size()));
	}

}
