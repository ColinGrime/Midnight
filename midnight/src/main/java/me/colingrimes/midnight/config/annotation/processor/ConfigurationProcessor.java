package me.colingrimes.midnight.config.annotation.processor;

import me.colingrimes.midnight.annotation.AnnotationProcessor;
import me.colingrimes.midnight.config.ConfigurationState;
import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.Midnight;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Responsible for processing the {@link Configuration} annotation.
 */
public class ConfigurationProcessor implements AnnotationProcessor {

	private final Midnight plugin;

	public ConfigurationProcessor(@Nonnull Midnight plugin) {
		this.plugin = plugin;
	}

	@Nonnull
	@Override
	public Class<? extends Annotation> getAnnotation() {
		return Configuration.class;
	}

	@Override
	public void process(@Nonnull Class<?> clazz) {
		String configName = clazz.getAnnotation(Configuration.class).value();
		ConfigurationAdapter adapter = ConfigurationAdapter.of(plugin, configName);
		List<? extends Option<?>> options = Option.initialize(clazz);

		ConfigurationState state = new ConfigurationState(adapter, options);
		plugin.getConfigurationManager().addConfiguration(configName, state);
	}
}
