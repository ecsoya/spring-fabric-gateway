package io.github.ecsoya.fabric.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * Common fabric object bean.
 * 
 * Using the CompositeKey with the id and type to identify a specific object.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricObject implements IFabricObject {

	private String id;

	private String type;

	private Map<String, Object> values;

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
