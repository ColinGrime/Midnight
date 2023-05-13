package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface OptionFactory<T> {

	OptionFactory<ConfigurationSection> SECTION = ConfigurationAdapter::getSection;
	OptionFactory<String> STRING = ConfigurationAdapter::getString;
	OptionFactory<List<String>> STRING_LIST = ConfigurationAdapter::getStringList;
	OptionFactory<Integer> INTEGER = ConfigurationAdapter::getInteger;
	OptionFactory<Double> DOUBLE = ConfigurationAdapter::getDouble;
	OptionFactory<Boolean> BOOL = ConfigurationAdapter::getBoolean;
	OptionFactory<ItemStack> ITEM_STACK = ConfigurationAdapter::getItemStack;
	OptionFactory<ConfigurableInventory> INVENTORY = ConfigurationAdapter::getInventory;

	/**
	 * Creates a {@link ConfigurationSection} option.
	 *
	 * @param path the path to the configuration section
	 * @return the option for the configuration section
	 */
	@Nonnull
	static Option<ConfigurationSection> option(@Nonnull String path) {
		return Option.of(new Bound<>(SECTION, path, null));
	}

	/**
	 * Creates a {@link String} option.
	 *
	 * @param path the path to the string
	 * @param def the default value
	 * @return the option for the string
	 */
	@Nonnull
	static Option<String> option(@Nonnull String path, @Nonnull String def) {
		return Option.of(new Bound<>(STRING, path, def));
	}

	/**
	 * Creates a {@link List} of {@link String}s option.
	 *
	 * @param path the path to the list
	 * @param def the default value
	 * @return the option for the list
	 */
	@Nonnull
	static Option<List<String>> option(@Nonnull String path, @Nonnull List<String> def) {
		return Option.of(new Bound<>(STRING_LIST, path, def));
	}

	/**
	 * Creates an {@link Integer} option.
	 *
	 * @param path the path to the integer
	 * @param def the default value
	 * @return the option for the integer
	 */
	@Nonnull
	static Option<Integer> option(@Nonnull String path, int def) {
		return Option.of(new Bound<>(INTEGER, path, def));
	}

	/**
	 * Creates a {@link Double} option.
	 *
	 * @param path the path to the double
	 * @param def the default value
	 * @return the option for the double
	 */
	@Nonnull
	static Option<Double> option(@Nonnull String path, double def) {
		return Option.of(new Bound<>(DOUBLE, path, def));
	}

	/**
	 * Creates a {@link Boolean} option.
	 *
	 * @param path the path to the boolean
	 * @param def the default value
	 * @return the option for the boolean
	 */
	@Nonnull
	static Option<Boolean> option(@Nonnull String path, boolean def) {
		return Option.of(new Bound<>(BOOL, path, def));
	}

	/**
	 * Creates an {@link ItemStack} option.
	 *
	 * @param path the path to the item stack
	 * @param def the default value
	 * @return the option for the item stack
	 */
	@Nonnull
	static Option<ItemStack> option(@Nonnull String path, @Nonnull ItemStack def) {
		return Option.of(new Bound<>(ITEM_STACK, path, def));
	}

	/**
	 * Creates a {@link ConfigurableInventory} option.
	 *
	 * @param path the path to the inventory
	 * @param def the default value
	 * @return the option for the inventory
	 */
	@Nonnull
	static Option<ConfigurableInventory> option(@Nonnull String path, @Nonnull ConfigurableInventory def) {
		return Option.of(new Bound<>(INVENTORY, path, def));
	}

	/**
	 * Creates a {@link String> message.
	 * }
	 * @param path the path to the message
	 * @param def the default value
	 * @return the message option
	 */
	@Nonnull
	static MessageOption<String> message(@Nonnull String path, @Nonnull String def) {
		return new MessageOption<>(new Bound<>(STRING, path, def));
	}

	/**
	 * Creates a {@link List} of {@link String}s message.
	 *
	 * @param path the path to the message
	 * @param def the default value
	 * @return the message option
	 */
	@Nonnull
	static MessageOption<List<String>> message(@Nonnull String path, @Nonnull List<String> def) {
		return new MessageOption<>(new Bound<>(STRING_LIST, path, def));
	}

	/**
	 * Creates a {@link List} of {@link String}s message.
	 *
	 * @param path the path to the message
	 * @param def the default value
	 * @return the message option
	 */
	@Nonnull
	static MessageOption<List<String>> message(@Nonnull String path, @Nonnull String...def) {
		return new MessageOption<>(new Bound<>(STRING_LIST, path, List.of(def)));
	}

	/**
	 * Gets the value of the option.
	 *
	 * @param adapter the configuration adapter
	 * @param path    the path
	 * @return the value
	 */
	@Nonnull
	Optional<T> getValue(@Nonnull ConfigurationAdapter adapter, @Nonnull String path);

	/**
	 * Bounds an option to a specific path.
	 *
	 * @param <T> the type of the option
	 */
	class Bound<T> implements Function<ConfigurationAdapter, T> {

		private final OptionFactory<T> factory;
		private final String path;
		private final Optional<T> def;

		Bound(@Nonnull OptionFactory<T> factory, @Nonnull String path, @Nullable T def) {
			this.factory = factory;
			this.path = path;
			this.def = Optional.ofNullable(def);
		}

		@Nonnull
		@Override
		public T apply(@Nullable ConfigurationAdapter configurationAdapter) {
			if (configurationAdapter == null) {
				if (def.isPresent()) {
					return def.get();
				} else {
					throw new IllegalStateException("Configuration adapter is null and no default value was provided.");
				}
			}

			return factory.getValue(configurationAdapter, path).orElse(def.orElseThrow(() -> new IllegalStateException("No default value was provided.")));
		}
	}
}
