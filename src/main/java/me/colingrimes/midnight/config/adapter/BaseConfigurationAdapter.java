package me.colingrimes.midnight.config.adapter;

import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.util.bukkit.Items;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

abstract class BaseConfigurationAdapter implements ConfigurationAdapter {

	FileConfiguration config;

	@Nonnull
	@Override
	public Optional<ConfigurationSection> getSection(@Nonnull String path) {
		return Optional.ofNullable(config.getConfigurationSection(path));
	}

	@Nonnull
	@Override
	public Optional<String> getString(@Nonnull String path) {
		return Optional.ofNullable(config.getString(path));
	}

	@Nonnull
	@Override
	public Optional<List<String>> getStringList(@Nonnull String path) {
		List<String> list = config.getStringList(path);
		if (!list.isEmpty()) {
			return Optional.of(list);
		}

		Optional<String> value = getString(path);
		return value.map(List::of).or(() -> Optional.of(list));
	}

	@Nonnull
	@Override
	public Optional<Integer> getInteger(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Integer.class));
	}

	@Nonnull
	@Override
	public Optional<Long> getLong(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Long.class));
	}

	@Nonnull
	@Override
	public Optional<Double> getDouble(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Double.class));
	}

	@Nonnull
	@Override
	public Optional<Boolean> getBoolean(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Boolean.class));
	}

	@Nonnull
	@Override
	public Optional<ItemStack> getItemStack(@Nonnull String path) {
		return Optional.of(Items.config(config.getConfigurationSection(path)));
	}

	@Nonnull
	@Override
	public Optional<ConfigurableInventory> getInventory(@Nonnull String path) {
		return ConfigurableInventory.of(config.getConfigurationSection(path));
	}
}
