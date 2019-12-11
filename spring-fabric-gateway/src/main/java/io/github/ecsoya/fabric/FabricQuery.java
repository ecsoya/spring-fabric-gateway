package io.github.ecsoya.fabric;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FabricQuery {

	private Map<String, Object> equalsParams = new HashMap<>();
	private Map<String, String> likeParams = new HashMap<>();

	private String type;

	public FabricQuery(String type) {
		this.type = type;
	}

	public FabricQuery() {
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public FabricQuery equals(String key, Object value) {
		if (key == null || value == null) {
			return this;
		}
		equalsParams.put(key, value);
		return this;
	}

	public FabricQuery like(String key, String value) {
		if (key == null || value == null) {
			return this;
		}
		likeParams.put(key, value);
		return this;
	}

	public String toJson() {
		JsonObject query = new JsonObject();
		JsonObject selector = new JsonObject();

		if (getType() != null) {
			selector.addProperty("type", type);
		}

		Set<Entry<String, Object>> euqals = equalsParams.entrySet();
		for (Entry<String, Object> entry : euqals) {
			Object value = entry.getValue();
			if (value instanceof Boolean) {
				selector.addProperty(entry.getKey(), (Boolean) value);
			} else if (value instanceof Number) {
				selector.addProperty(entry.getKey(), (Number) value);
			} else if (value instanceof String) {
				selector.addProperty(entry.getKey(), (String) value);
			} else if (value instanceof Character) {
				selector.addProperty(entry.getKey(), (Character) value);
			} else if (value instanceof JsonElement) {
				selector.add(entry.getKey(), (JsonElement) value);
			} else if (value != null) {
				selector.addProperty(entry.getKey(), value.toString());
			}
		}
		Set<Entry<String, String>> likes = likeParams.entrySet();
		for (Entry<String, String> entry : likes) {
			JsonObject regex = new JsonObject();
			regex.addProperty("$regex", ".*?" + entry.getValue() + ".*?");
			selector.add(entry.getKey(), regex);
		}

//		JsonArray index = new JsonArray();
//		index.add("id");
//		selector.add("use_index", index);

		query.add("selector", selector);
		Gson gson = new Gson();
		return gson.toJson(query);
	}

	public static FabricQuery build(String type, String key, Object value) {
		FabricQuery query = new FabricQuery(type);
		return query.equals(key, value);
	}
}
