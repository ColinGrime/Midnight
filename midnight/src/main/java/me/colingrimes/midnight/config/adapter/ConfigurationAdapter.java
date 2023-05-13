package me.colingrimes.midnight.config.adapter;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

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
			return new CustomConfigurationAdapter(plugin, fileName);
		}
	}

	void reload();

	/**
	 * Gets a configuration section from the configuration.
	 *
	 * @param path the path to the configuration section
	 * @return the configuration section, if present
	 */
	@Nonnull
	Optional<ConfigurationSection> getSection(@Nonnull String path);

	/**
	 * Gets a string from the configuration.
	 *
	 * @param path the path to the string
	 * @return the string, if present
	 */
	@Nonnull
	Optional<String> getString(@Nonnull String path);

	/**
	 * Gets a list of strings from the configuration.
	 *
	 * @param path the path to the list
	 * @return the list, if present
	 */
	@Nonnull
	Optional<List<String>> getStringList(@Nonnull String path);

	/**
	 * Gets an integer from the configuration.
	 *
	 * @param path the path to the integer
	 * @return the integer, if present
	 */
	@Nonnull
	Optional<Integer> getInteger(@Nonnull String path);

	/**
	 * Gets a double from the configuration.
	 *
	 * @param path the path to the double
	 * @return the double, if present
	 */
	@Nonnull
	Optional<Double> getDouble(@Nonnull String path);

	/**
	 * Gets a boolean from the configuration.
	 *
	 * @param path the path to the boolean
	 * @return the boolean, if present
	 */
	@Nonnull
	Optional<Boolean> getBoolean(@Nonnull String path);

	/**
	 * Gets an item stack from the configuration.
	 *
	 * @param path the path to the item stack
	 * @return the item stack, if present
	 */
	@Nonnull
	Optional<ItemStack> getItemStack(@Nonnull String path);

	/**
	 * Gets a configurable inventory from the configuration.
	 *
	 * @param path the path to the configurable inventory
	 * @return the configurable inventory, if present
	 */
	@Nonnull
	Optional<ConfigurableInventory> getInventory(@Nonnull String path);
}
