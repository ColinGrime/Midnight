package me.colingrimes.midnight.util.misc;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Types {

    /**
     * Gets whether the specified string could be parsed into an integer.
     *
     * @param str the string to check
     * @return true if the string is an integer
     */
    public static boolean isInteger(@Nonnull String str) {
        return str.matches("\\d+");
    }

    /**
     * Gets whether the specified string could be parsed into a double.
     *
     * @param str the string to check
     * @return true if the string is a double
     */
    public static boolean isDouble(@Nonnull String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }

    /**
     * Gets whether the specificed object could be parsed into a string list.
     *
     * @param candidateList the object to check
     * @return true if the object is a string list
     */
    public static boolean isStringList(@Nonnull Object candidateList) {
        return candidateList instanceof List<?> list && !list.isEmpty() && list.stream().allMatch(item -> item instanceof String);
    }

    /**
     * Gets the object as a string list if the conversion is possible.
     *
     * @param candidateList the object to check
     * @return an optional containing a list of strings if available
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static Optional<List<String>> asStringList(@Nonnull Object candidateList) {
        if (candidateList instanceof List<?> list && !list.isEmpty() && list.stream().allMatch(item -> item instanceof String)) {
            return Optional.of((List<String>) list);
        } else {
            return Optional.empty();
        }
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

    /**
     * Parses a {@link Map} from a {@link ConfigurationSection} where keys are numeric strings.
     * <p>
     * The keys are parsed to integers, and values are produced using the given mapping function.
     *
     * @param section the configuration section
     * @param valueMapper a function that maps the string key to a value
     * @param <T> the value in the map
     * @return a map with integer keys and mapped values
     */
    @Nonnull
    public static <T> Map<Integer, T> mapIntegerKeys(@Nullable ConfigurationSection section, @Nonnull Function<String, T> valueMapper) {
        if (section == null) {
            return new HashMap<>();
        }

        Map<Integer, T> result = new HashMap<>();
        for (String key : section.getKeys(false)) {
            if (Types.isInteger(key)) {
                T value = valueMapper.apply(key);
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
     * @param valueMapper a function that maps the string key to a value
     * @param <T> the value in the map
     * @return a map with integer slots and mapped values
     */
    @Nonnull
    public static <T> Map<Integer, T> mapSlotKeys(@Nullable ConfigurationSection section, @Nonnull Function<String, T> valueMapper) {
        if (section == null) {
            return new HashMap<>();
        }

        Map<Integer, T> result = new HashMap<>();
        for (String key : section.getKeys(false)) {
            if (Types.isInteger(key)) {
                T value = valueMapper.apply(key);
                result.put(Integer.parseInt(key)-1, value);
            }
        }
        return result;
    }

    private Types() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
