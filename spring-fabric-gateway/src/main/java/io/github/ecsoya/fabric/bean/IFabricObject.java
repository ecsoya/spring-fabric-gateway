package io.github.ecsoya.fabric.bean;

import java.util.List;
import java.util.Map;

/**
 * 
 * Fabric object interface.
 * 
 * @author ecsoya
 *
 */
public interface IFabricObject {

	default String getType() {
		return getClass().getName();
	}

	String getId();

	void setId(String id);

	void setValues(Map<String, Object> values);

	Map<String, Object> getValues();

	List<FabricQueryHistory> getQueryHistories();

	void addQueryHistory(FabricQueryHistory history);
}
