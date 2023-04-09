package me.colingrimes.midnight.config;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.config.option.Option;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Manages the state of a configuration.
 */
public class ConfigurationState {

	private final ConfigurationAdapter adapter;
	private final List<? extends Option<?>> options;

	public ConfigurationState(@Nonnull ConfigurationAdapter adapter, @Nonnull List<? extends Option<?>> options) {
		this.adapter = adapter;
		this.options = options;
		this.reload();
	}

	/**
	 * Reloads the options.
	 */
	public void reload() {
		adapter.reload();
		options.forEach(option -> option.reload(adapter));
	}
}
