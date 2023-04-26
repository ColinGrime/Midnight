package me.colingrimes.midnight.config.adapter;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Responsible for providing a simple interface for accessing configuration values.
 */
public interface ConfigurationAdapter {

	@Nonnull
	static ConfigurationAdapter of(@Nonnull MidnightPlugin plugin, @Nonnull String name) {
		String fileName = name.endsWith(".yml") ? name.toLowerCase() : name.toLowerCase() + ".yml";

		if (fileName.equals("config.yml")) {
			return new DefaultConfigurationAdapter(plugin);
		} else {
			return new CustomConfigurationAdapter(plugin, fileName);
		}
	}

	void reload();

	@Nonnull
	Optional<String> getString(@Nonnull String path);

	@Nonnull
	Optional<List<String>> getStringList(@Nonnull String path);

	@Nonnull
	Optional<Integer> getInteger(@Nonnull String path);

	@Nonnull
	Optional<Boolean> getBoolean(@Nonnull String path);

	@Nonnull
	Optional<ItemStack> getItemStack(@Nonnull String path);

	@Nonnull
	Optional<ConfigurableInventory> getInventory(@Nonnull String path);
}
