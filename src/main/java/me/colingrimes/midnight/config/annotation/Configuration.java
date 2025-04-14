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

	/**
	 * Optional dependent configuration classes.
	 * <p>
	 * Must also be annotated with {@code @Configuration}.
	 * Guaranteed to be loaded first if present.
	 * If any are missing, this configuration will not be loaded.
	 *
	 * @return array of dependent classes
	 */
	@Nonnull
	Class<?>[] depend() default {};
}