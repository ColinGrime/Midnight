package me.colingrimes.midnight.annotation;

import me.colingrimes.midnight.util.clazz.ClassFinder;
import me.colingrimes.midnight.MidnightPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationRegistry {

	private final MidnightPlugin plugin;
	private final List<AnnotationProcessor> processors = new ArrayList<>();

	public AnnotationRegistry(@Nonnull MidnightPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Registers an annotation processor.
	 * @param processor the processor to register
	 */
	public void register(@Nonnull AnnotationProcessor processor) {
		processors.add(processor);
	}

	/**
	 * Processes all registered annotation processors.
	 */
	public void process() {
		for (Class<?> clazz : ClassFinder.getClasses(plugin, plugin.getRootPackage())) {
			for (AnnotationProcessor processor : processors) {
				// Perform all actions for this processor.
				processClass(processor, clazz);
				processMethods(processor, clazz);
				processFields(processor, clazz);
			}
		}

		processors.clear();
	}

	private void processClass(@Nonnull AnnotationProcessor processor, @Nonnull Class<?> clazz) {
		if (clazz.isAnnotationPresent(processor.getAnnotation())) {
			processor.process(clazz);
		}
	}

	private void processMethods(@Nonnull AnnotationProcessor processor, @Nonnull Class<?> clazz) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(processor.getAnnotation())) {
				processor.process(method);
			}
		}
	}

	private void processFields(@Nonnull AnnotationProcessor processor, @Nonnull Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(processor.getAnnotation())) {
				processor.process(field);
			}
		}
	}
}

