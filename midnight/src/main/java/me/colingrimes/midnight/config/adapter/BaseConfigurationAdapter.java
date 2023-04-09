package me.colingrimes.midnight.config.adapter;

import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.List;

abstract class BaseConfigurationAdapter implements ConfigurationAdapter {

	FileConfiguration config;

	@Override
	public String getString(@Nonnull String path) {
		return config.getString(path);
	}

	@Override
	public int getInteger(@Nonnull String path) {
		return config.getInt(path);
	}

	@Override
	public boolean getBoolean(@Nonnull String path) {
		return config.getBoolean(path);
	}

	@Override
	public List<String> getStringList(@Nonnull String path) {
		return config.getStringList(path);
	}
}
