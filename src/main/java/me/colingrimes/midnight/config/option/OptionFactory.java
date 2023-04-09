package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface OptionFactory<T> {

	OptionFactory<String> STRING = ConfigurationAdapter::getString;
	OptionFactory<List<String>> STRING_LIST = ConfigurationAdapter::getStringList;
	OptionFactory<Integer> INTEGER = ConfigurationAdapter::getInteger;
	OptionFactory<Boolean> BOOL = ConfigurationAdapter::getBoolean;

	@Nonnull
	static Option<String> string(String path, String def) {
		return Option.of(new Bound<>(STRING, path, def));
	}

	@Nonnull
	static Option<List<String>> stringList(String path, List<String> def) {
		return Option.of(new Bound<>(STRING_LIST, path, def));
	}

	@Nonnull
	static Option<Integer> integer(String path, int def) {
		return Option.of(new Bound<>(INTEGER, path, def));
	}

	@Nonnull
	static Option<Boolean> bool(String path, boolean def) {
		return Option.of(new Bound<>(BOOL, path, def));
	}

	/**
	 * Gets the value of the option.
	 * @param adapter the configuration adapter
	 * @param path the path
	 * @return the value
	 */
	T getValue(ConfigurationAdapter adapter, String path);

	/**
	 * Bounds an option to a specific path.
	 * @param <T> the type of the option
	 */
	class Bound<T> implements Function<ConfigurationAdapter, T> {

		private final OptionFactory<T> factory;
		private final String path;
		private final T def;

		Bound(OptionFactory<T> factory, String path, T def) {
			this.factory = factory;
			this.path = path;
			this.def = def;
		}

		@Override
		public T apply(ConfigurationAdapter configurationAdapter) {
			if (configurationAdapter == null) {
				return def;
			}

			T value = factory.getValue(configurationAdapter, path);
			return Objects.requireNonNullElse(value, def);
		}
	}
}
