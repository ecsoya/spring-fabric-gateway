package io.github.ecsoya.fabric.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.ecsoya.fabric.json.DefaultFabricJsonConverter;
import io.github.ecsoya.fabric.json.IFabricJsonConverter;

@Retention(RUNTIME)
@Target({ ElementType.TYPE })
public @interface FabricJson {

	Class<? extends IFabricJsonConverter> converter() default DefaultFabricJsonConverter.class;

	/**
	 * The type value.
	 * 
	 * @see FabricType
	 */
	String type() default "";

}
