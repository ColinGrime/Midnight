package me.colingrimes.midnight.config.annotation;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.config.ConfigurationState;
import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.io.Introspector;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Responsible for processing the {@link Configuration} annotation.
 */
public class ConfigurationProcessor {

	private final Midnight plugin;

	public ConfigurationProcessor(@Nonnull Midnight plugin) {
		this.plugin = plugin;
	}

	/**
	 * Processes all {@link Configuration}-annotated classes in the "config" package.
	 * <p>
	 * Ensures declared dependencies are loaded first. Skips any configuration with
	 * missing or cyclic dependencies.
	 */
	public void process() {
		Map<Class<?>, Configuration> annotations = new HashMap<>();
		for (Class<?> clazz : Introspector.getClassesRecursively(plugin.getClass().getClassLoader(), plugin.getRootPackage() + ".config")) {
			if (clazz.isAnnotationPresent(Configuration.class)) {
				annotations.put(clazz, clazz.getAnnotation(Configuration.class));
			}
		}

		Set<Class<?>> classes = annotations.keySet();

		// Build dependency graph
		Map<Class<?>, List<Class<?>>> graph = new HashMap<>();
		for (Class<?> clazz : classes) {
			graph.put(clazz, List.of(annotations.get(clazz).depend()));
		}

		// Add valid configurations to the manager and load its options.
		for (Class<?> clazz : sort(classes, graph)) {
			String configName = annotations.get(clazz).value();
			ConfigurationAdapter adapter = ConfigurationAdapter.of(plugin, configName);
			List<? extends Option<?>> options = Option.initialize(clazz);

			ConfigurationState state = new ConfigurationState(adapter, options);
			plugin.getConfigurationManager().addConfiguration(configName, state);
		}
	}

	/**
	 * Sorts the configuration classes based on their dependencies.
	 * Skips entries with missing or cyclic dependencies.
	 *
	 * @param classes set of all configuration classes
	 * @param graph dependency graph
	 * @return list of classes in topologically sorted order
	 */
	@Nonnull
	private List<Class<?>> sort(@Nonnull Set<Class<?>> classes, @Nonnull Map<Class<?>, List<Class<?>>> graph) {
		List<Class<?>> result = new ArrayList<>();
		Set<Class<?>> visited = new HashSet<>();
		for (Class<?> clazz : graph.keySet()) {
			if (!visited.contains(clazz) && !visit(classes, graph, clazz, visited, new HashSet<>(), result)) {
				Logger.severe(plugin, "Failed to load configuration: " + clazz.getName());
				Logger.severe(plugin, "Possible cause: missing dependency or circular reference.");
				Common.disable(plugin);
				throw new IllegalStateException(plugin.getName() + " disabled due to invalid configuration: " + clazz.getName());
			}
		}
		return result;
	}

	/**
	 * Depth-first traversal for topological sort.
	 *
	 * @param classes set of all configuration classes
	 * @param graph dependency graph
	 * @param clazz current class being visited
	 * @param visited set of successfully processed classes
	 * @param visiting set of currently visiting classes (for cycle detection)
	 * @param result sorted result list
	 * @return true if the class and its dependencies are valid and acyclic
	 */
	private boolean visit(@Nonnull Set<Class<?>> classes,
						  @Nonnull Map<Class<?>, List<Class<?>>> graph,
						  @Nonnull Class<?> clazz,
						  @Nonnull Set<Class<?>> visited,
						  @Nonnull Set<Class<?>> visiting,
						  @Nonnull List<Class<?>> result) {
		if (visited.contains(clazz)) return true;
		if (visiting.contains(clazz)) return false;

		visiting.add(clazz);
		for (Class<?> dependency : graph.getOrDefault(clazz, List.of())) {
			if (!classes.contains(dependency) || !visit(classes, graph, dependency, visited, visiting, result)) {
				return false;
			}
		}
		visiting.remove(clazz);

		// Class has been successfully processed.
		visited.add(clazz);
		result.add(clazz);
		return true;
	}
}
