package me.colingrimes.midnight.config.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

	/**
	 * Gets the configuration file name.
	 *
	 * @return the configuration file name
	 */
	@Nonnull
	String value() default "config.yml";
}