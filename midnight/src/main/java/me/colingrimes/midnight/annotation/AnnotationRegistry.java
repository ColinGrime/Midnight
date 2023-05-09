package me.colingrimes.midnight.annotation;

import me.colingrimes.midnight.util.io.Files;
import me.colingrimes.midnight.Midnight;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationRegistry {

	private final Midnight plugin;
	private final List<AnnotationProcessor> processors = new ArrayList<>();

	public AnnotationRegistry(@Nonnull Midnight plugin) {
		this.plugin = plugin;
	}

	/**
	 * Registers an annotation processor.
	 *
	 * @param processor the processor to register
	 */
	public void register(@Nonnull AnnotationProcessor processor) {
		processors.add(processor);
	}

	/**
	 * Processes all registered annotation processors.
	 */
	public void process() {
		for (Class<?> clazz : Files.getClasses(plugin, plugin.getRootPackage())) {
			for (AnnotationProcessor processor : processors) {
				// Perform all actions for this processor.
				if (processor.getAnnotationType() == AnnotationType.CLASS) {
					processClass(processor, clazz);
				} else if (processor.getAnnotationType() == AnnotationType.METHOD) {
					processMethods(processor, clazz);
				}
			}
		}

		processors.clear();
	}

	/**
	 * Processes a class for a given annotation processor.
	 *
	 * @param processor the processor to process
	 * @param clazz     the class to process
	 */
	private void processClass(@Nonnull AnnotationProcessor processor, @Nonnull Class<?> clazz) {
		if (clazz.isAnnotationPresent(processor.getAnnotation())) {
			processor.process(clazz);
		}
	}

	/**
	 * Processes all methods for a given annotation processor.
	 *
	 * @param processor the processor to process
	 * @param clazz     the class to process
	 */
	private void processMethods(@Nonnull AnnotationProcessor processor, @Nonnull Class<?> clazz) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(processor.getAnnotation())) {
				processor.process(method);
			}
		}
	}
}

