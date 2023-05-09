package me.colingrimes.midnight.annotation;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Responsible for processing annotations on classes and methods.
 */
public interface AnnotationProcessor {

	/**
	 * Gets the main annotation that this processor is responsible for.
	 *
	 * @return the annotation class
	 */
	@Nonnull
	Class<? extends Annotation> getAnnotation();

	/**
	 * Gets the type of annotation that this processor is responsible for.
	 *
	 * @return the annotation type
	 */
	@Nonnull
	AnnotationType getAnnotationType();

	/**
	 * Processes annotations on the class level.
	 *
	 * @param clazz the class containing the annotation to be processed
	 */
	default void process(@Nonnull Class<?> clazz) {}

	/**
	 * Processes annotations on the method level.
	 *
	 * @param method the method containing the annotation to be processed
	 */
	default void process(@Nonnull Method method) {}
}
