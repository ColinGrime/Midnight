package me.colingrimes.midnight.config.adapter;

import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

abstract class BaseConfigurationAdapter implements ConfigurationAdapter {

	FileConfiguration config;

	@Nonnull
	@Override
	public Optional<String> getString(@Nonnull String path) {
		return Optional.ofNullable(config.getString(path));
	}

	@Nonnull
	@Override
	public Optional<Integer> getInteger(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Integer.class));
	}

	@Nonnull
	@Override
	public Optional<Boolean> getBoolean(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Boolean.class));
	}

	@Nonnull
	@Override
	public Optional<List<String>> getStringList(@Nonnull String path) {
		List<String> list = config.getStringList(path);
		return list.isEmpty() ? Optional.empty() : Optional.of(list);
	}
}
