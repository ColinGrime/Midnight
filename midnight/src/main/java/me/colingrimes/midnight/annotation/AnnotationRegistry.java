package me.colingrimes.midnight.annotation;

import me.colingrimes.midnight.plugin.Midnight;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class AnnotationRegistry {

	private final String packageName;
	private final List<AnnotationProcessor> processors = new ArrayList<>();

	public AnnotationRegistry(@Nonnull Midnight plugin) {
		this.packageName = getTopLevelPackage(plugin.getClass());
	}

	/**
	 * Gets the top level package of the given class.
	 * @param clazz the class
	 * @return the top level package
	 */
	private String getTopLevelPackage(Class<?> clazz) {
		String currentPackageName = clazz.getPackage().getName();
		while (currentPackageName.contains(".")) {
			int lastDotIndex = currentPackageName.indexOf('.');
			currentPackageName = currentPackageName.substring(0, lastDotIndex);
		}
		return currentPackageName;
	}

	/**
	 * Registers an annotation processor.
	 * @param processor the processor to register
	 */
	public void register(AnnotationProcessor processor) {
		processors.add(processor);
	}

	/**
	 * Processes all registered annotation processors.
	 */
	public void process() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

		// For every class, perform all necessary annotation actions.
		for (Class<?> clazz : classes) {
			for (AnnotationProcessor processor : processors) {
				Class<?> taggerClass = processor.getTaggerClass();
				if (taggerClass != null && clazz.isAssignableFrom(taggerClass)) {
					break;
				}

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
			processor.process(method);
		}
	}

	private void processFields(@Nonnull AnnotationProcessor processor, @Nonnull Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			processor.process(field);
		}
	}
}

