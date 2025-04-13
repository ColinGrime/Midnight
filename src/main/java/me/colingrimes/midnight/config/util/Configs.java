package me.colingrimes.midnight.config.util;

import me.colingrimes.midnight.util.misc.Types;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Configs {

	/**
	 * Parses a {@link Map} from a {@link ConfigurationSection} where keys are strings.
	 * <p>
	 * The values are produced using the given mapping function.
	 *
	 * @param section the configuration section
	 * @param valueMapper a function that maps the configuration section to a value
	 * @param <T> the value in the map
	 * @return a map with string keys and mapped values
	 */
	@Nonnull
	public static <T> Map<String, T> mapKeys(@Nullable ConfigurationSection section, @Nonnull Function<ConfigurationSection, T> valueMapper) {
		if (section == null) {
			return new HashMap<>();
		}

		Map<String, T> result = new HashMap<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection sec = section.getConfigurationSection(key);
			if (sec == null) {
				continue;
			}

			T value = valueMapper.apply(sec);
			if (value != null) {
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * Parses a {@link Map} from a {@link ConfigurationSection} where keys are strings.
	 * <p>
	 * The values are produced using the given mapping function.
	 *
	 * @param section the configuration section
	 * @param valueMapper a function that maps the configuration section and string key to a value
	 * @param <T> the value in the map
	 * @return a map with string keys and mapped values
	 */
	@Nonnull
	public static <T> Map<String, T> mapKeys(@Nullable ConfigurationSection section, @Nonnull BiFunction<ConfigurationSection, String, T> valueMapper) {
		if (section == null) {
			return new HashMap<>();
		}

		Map<String, T> result = new HashMap<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection sec = section.getConfigurationSection(key);
			if (sec == null) {
				continue;
			}

			T value = valueMapper.apply(sec, key);
			if (value != null) {
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * Parses a {@link Map} from a {@link ConfigurationSection} where keys are numeric strings.
	 * <p>
	 * The keys are parsed to integers, and values are produced using the given mapping function.
	 *
	 * @param section the configuration section
	 * @param valueMapper a function that maps the configuration section to a value
	 * @param <T> the value in the map
	 * @return a map with integer keys and mapped values
	 */
	@Nonnull
	public static <T> Map<Integer, T> mapIntegerKeys(@Nullable ConfigurationSection section, @Nonnull Function<ConfigurationSection, T> valueMapper) {
		if (section == null) {
			return new HashMap<>();
		}

		Map<Integer, T> result = new HashMap<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection sec = section.getConfigurationSection(key);
			if (sec == null) {
				continue;
			}

			T value = valueMapper.apply(sec);
			if (value != null && Types.isInteger(key)) {
				result.put(Integer.parseInt(key), value);
			}
		}
		return result;
	}

	/**
	 * Parses a {@link Map} from a {@link ConfigurationSection} where keys are numeric strings.
	 * <p>
	 * The keys are parsed to integers, and values are produced using the given mapping function.
	 *
	 * @param section the configuration section
	 * @param valueMapper a function that maps the configuration section and string key to a value
	 * @param <T> the value in the map
	 * @return a map with integer keys and mapped values
	 */
	@Nonnull
	public static <T> Map<Integer, T> mapIntegerKeys(@Nullable ConfigurationSection section, @Nonnull BiFunction<ConfigurationSection, String, T> valueMapper) {
		if (section == null) {
			return new HashMap<>();
		}

		Map<Integer, T> result = new HashMap<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection sec = section.getConfigurationSection(key);
			if (sec == null) {
				continue;
			}

			T value = valueMapper.apply(sec, key);
			if (value != null && Types.isInteger(key)) {
				result.put(Integer.parseInt(key), value);
			}
		}
		return result;
	}

	/**
	 * Parses a {@link Map} from a {@link ConfigurationSection} where keys are numeric strings.
	 * <p>
	 * The keys are parsed to integer slots, and values are produced using the given mapping function.
	 *
	 * @param section the configuration section
	 * @param valueMapper a function that maps the configuration section to a value
	 * @param <T> the value in the map
	 * @return a map with integer slots and mapped values
	 */
	@Nonnull
	public static <T> Map<Integer, T> mapSlotKeys(@Nullable ConfigurationSection section, @Nonnull Function<ConfigurationSection, T> valueMapper) {
		if (section == null) {
			return new HashMap<>();
		}

		Map<Integer, T> result = new HashMap<>();
		for (String key : section.getKeys(false)) {
			ConfigurationSection sec = section.getConfigurationSection(key);
			if (sec == null) {
				continue;
			}

			T value = valueMapper.apply(sec);
			if (value != null && Types.isInteger(key)) {
				result.put(Integer.parseInt(key)-1, value);
			}
		}
		return result;
	}

	/**
	 * Parses a {@link Set} from a {@link ConfigurationSection} where keys are numeric strings.
	 *
	 * @param section the configuration section
	 * @return a set with integer keys
	 */
	@Nonnull
	public static Set<Integer> integerKeys(@Nullable ConfigurationSection section) {
		if (section == null) {
			return Collections.emptySet();
		}
		return section.getKeys(false).stream()
				.filter(Types::isInteger)
				.map(Integer::parseInt)
				.collect(Collectors.toSet());
	}

	private Configs() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
