package io.github.ecsoya.fabric.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Ignore to save fabric properties.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface JsonIgnore {

	/**
	 * Ignore to write, default is true
	 * 
	 * @return
	 */
	public boolean serialize() default true;

	/**
	 * Ignore to read, default is true.
	 * 
	 * @return
	 */
	public boolean deserialize() default true;
}
