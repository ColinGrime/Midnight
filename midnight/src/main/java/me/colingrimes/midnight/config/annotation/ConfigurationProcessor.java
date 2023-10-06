package me.colingrimes.midnight.config.annotation;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.config.ConfigurationState;
import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.util.io.Introspector;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Responsible for processing the {@link Configuration} annotation.
 */
public class ConfigurationProcessor {

	private final Midnight plugin;

	public ConfigurationProcessor(@Nonnull Midnight plugin) {
		this.plugin = plugin;
	}

	/**
	 * Processes all classes with the {@link Configuration} annotation.
	 * <p>
	 * Only classes in the "config" package will be processed.
	 */
	public void process() {
		for (Class<?> clazz : Introspector.getClasses(plugin.getClass().getClassLoader(), plugin.getRootPackage() + ".config")) {
			if (clazz.isAnnotationPresent(Configuration.class)) {
				String configName = clazz.getAnnotation(Configuration.class).value();
				ConfigurationAdapter adapter = ConfigurationAdapter.of(plugin, configName);
				List<? extends Option<?>> options = Option.initialize(clazz);

				ConfigurationState state = new ConfigurationState(adapter, options);
				plugin.getConfigurationManager().addConfiguration(configName, state);
			}
		}
	}
}
