package me.colingrimes.midnight.config.adapter;

import me.colingrimes.midnight.MidnightPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Configuration adapter for custom configuration files.
 */
public class CustomConfigurationAdapter extends BaseConfigurationAdapter {

	private final File file;

	public CustomConfigurationAdapter(@Nonnull MidnightPlugin plugin, @Nonnull String configName) {
		this.file = new File(plugin.getDataFolder(), configName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(configName, false);
		}
	}

	@Override
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
	}
}
