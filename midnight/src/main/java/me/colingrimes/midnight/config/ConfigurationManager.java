package me.colingrimes.midnight.config;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the configurations of the current plugin.
 * Configurations are automatically added to the manager when they are registered.
 * <p>
 * To reload all configurations of the current plugin, run {@link #reload()}.
 * To reload a specific configuration of the current plugin, run {@link #reload(String)}.
 */
public class ConfigurationManager {

	private final Map<String, ConfigurationState> configurations = new HashMap<>();

	/**
	 * Adds a configuration to the manager.
	 * @param name the name of the configuration
	 * @param state the state of the configuration
	 */
	public void addConfiguration(@Nonnull String name, @Nonnull ConfigurationState state) {
		configurations.put(name, state);
	}

	/**
	 * Reloads all configurations.
	 */
	public void reload() {
		configurations.values().forEach(ConfigurationState::reload);
	}

	/**
	 * Reloads a specific configuration.
	 * @param name the name of the configuration
	 */
	public void reload(@Nonnull String name) {
		configurations.get(name).reload();
	}
}
