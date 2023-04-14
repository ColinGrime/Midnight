package me.colingrimes.midnight.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface AnnotationProcessor {

	/**
	 * Gets the main annotation that this processor is for.
	 * @return the annotation
	 */
	@Nonnull
	Class<? extends Annotation> getAnnotation();

	/**
	 * Process annotations on the class level.
	 * @param clazz the class to process
	 */
	default void process(@Nonnull Class<?> clazz) {}

	/**
	 * Process annotations on the method level.
	 * @param method the method to process
	 */
	default void process(@Nonnull Method method) {}

	/**
	 * Process annotations on the field level.
	 * @param field the field to process
	 */
	default void process(@Nonnull Field field) {}
}
