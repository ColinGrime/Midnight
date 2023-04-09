package me.colingrimes.midnight.config.adapter;

import me.colingrimes.midnight.plugin.Midnight;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Responsible for providing a simple interface for accessing configuration values.
 */
public interface ConfigurationAdapter {

	@Nonnull
	static ConfigurationAdapter of(@Nonnull Midnight plugin, @Nonnull String name) {
		String fileName = name.endsWith(".yml") ? name.toLowerCase() : name.toLowerCase() + ".yml";

		if (fileName.equals("config.yml")) {
			return new DefaultConfigurationAdapter(plugin);
		} else {
			return new CustomConfigurationAdapter(plugin, name);
		}
	}

	void reload();

	String getString(@Nonnull String path);

	int getInteger(@Nonnull String path);

	boolean getBoolean(@Nonnull String path);

	List<String> getStringList(@Nonnull String path);
}
