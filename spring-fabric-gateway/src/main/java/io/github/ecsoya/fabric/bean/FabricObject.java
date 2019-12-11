package io.github.ecsoya.fabric.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class FabricObject implements IFabricObject {

	private String id;

	private String type;

	private List<FabricQueryHistory> queryHistories;

	private Map<String, Object> values;

	@Override
	public void addQueryHistory(FabricQueryHistory history) {
		if (queryHistories == null) {
			queryHistories = new ArrayList<FabricQueryHistory>();
		}
		queryHistories.add(history);
	}

	@Override
	public String getType() {
		return type;
	}

	public void put(String key, Object value) {
		if (values == null) {
			values = new HashMap<String, Object>();
		}
		values.put(key, value);
	}
}
