package io.github.ecsoya.fabric.bean;

import java.util.Map;

/**
 * 
 * Fabric object interface.
 * 
 * @author ecsoya
 *
 */
public interface IFabricObject {

	/**
	 * The type of the object, the default is the class type.
	 */
	default String getType() {
		return getClass().getName();
	}

	/**
	 * Get the id of current object.
	 */
	String getId();

	/**
	 * Set new id for current object.
	 */
	void setId(String id);

	/**
	 * Set value maps for current object.
	 */
	void setValues(Map<String, Object> values);

	/**
	 * Get values of current object.
	 */
	Map<String, Object> getValues();

}
