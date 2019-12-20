package io.github.ecsoya.fabric.bean;

/**
 * 
 * Fabric object interface.
 * 
 * @author ecsoya
 *
 */
public interface IFabricObject {

	public static final String DEFAULT_TYPE = IFabricObject.class.getName();

	/**
	 * The type of the object, the default is the class type.
	 */
	default String getType() {
		return DEFAULT_TYPE;
	}

	/**
	 * Get the id of current object.
	 */
	String getId();

	/**
	 * Set new id for current object.
	 */
	void setId(String id);

}
