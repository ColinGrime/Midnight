package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface OptionFactory<T> {

	OptionFactory<String> STRING = ConfigurationAdapter::getString;
	OptionFactory<List<String>> STRING_LIST = ConfigurationAdapter::getStringList;
	OptionFactory<Integer> INTEGER = ConfigurationAdapter::getInteger;
	OptionFactory<Boolean> BOOL = ConfigurationAdapter::getBoolean;
	OptionFactory<ItemStack> ITEM_STACK = ConfigurationAdapter::getItemStack;
	OptionFactory<ConfigurableInventory> INVENTORY = ConfigurationAdapter::getInventory;

	@Nonnull
	static Option<String> option(@Nonnull String path, @Nonnull String def) {
		return Option.of(new Bound<>(STRING, path, def));
	}

	@Nonnull
	static Option<List<String>> option(@Nonnull String path, @Nonnull List<String> def) {
		return Option.of(new Bound<>(STRING_LIST, path, def));
	}

	@Nonnull
	static Option<Integer> option(@Nonnull String path, int def) {
		return Option.of(new Bound<>(INTEGER, path, def));
	}

	@Nonnull
	static Option<Boolean> option(@Nonnull String path, boolean def) {
		return Option.of(new Bound<>(BOOL, path, def));
	}

	@Nonnull
	static Option<ItemStack> option(@Nonnull String path, @Nonnull ItemStack def) {
		return Option.of(new Bound<>(ITEM_STACK, path, def));
	}

	@Nonnull
	static Option<ConfigurableInventory> option(@Nonnull String path, @Nonnull ConfigurableInventory def) {
		return Option.of(new Bound<>(INVENTORY, path, def));
	}

	@Nonnull
	static Message<String> message(@Nonnull String path, @Nonnull String def) {
		return new Message<>(new Bound<>(STRING, path, def));
	}

	@Nonnull
	static Message<List<String>> message(@Nonnull String path, @Nonnull List<String> def) {
		return new Message<>(new Bound<>(STRING_LIST, path, def));
	}

	@Nonnull
	static Message<List<String>> message(@Nonnull String path, @Nonnull String...def) {
		return new Message<>(new Bound<>(STRING_LIST, path, List.of(def)));
	}

	/**
	 * Gets the value of the option.
	 * @param adapter the configuration adapter
	 * @param path the path
	 * @return the value
	 */
	@Nonnull
	Optional<T> getValue(@Nonnull ConfigurationAdapter adapter, @Nonnull String path);

	/**
	 * Bounds an option to a specific path.
	 * @param <T> the type of the option
	 */
	class Bound<T> implements Function<ConfigurationAdapter, T> {

		private final OptionFactory<T> factory;
		private final String path;
		private final T def;

		Bound(@Nonnull OptionFactory<T> factory, @Nonnull String path, @Nonnull T def) {
			this.factory = factory;
			this.path = path;
			this.def = def;
		}

		@Nonnull
		@Override
		public T apply(@Nullable ConfigurationAdapter configurationAdapter) {
			if (configurationAdapter == null) {
				return def;
			}

			return factory.getValue(configurationAdapter, path).orElse(def);
		}
	}
}
