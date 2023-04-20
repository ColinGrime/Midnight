package me.colingrimes.midnight.locale;

import me.colingrimes.midnight.util.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides quick and efficient replacement of multiple placeholders within a string or list of strings.
 */
public final class Placeholders {

	private final Map<String, String> placeholders = new HashMap<>();

	public Placeholders() {}

	public <T> Placeholders(@Nonnull String placeholder, @Nonnull T replacement) {
		Objects.requireNonNull(placeholder, "Placeholder is null.");
		Objects.requireNonNull(replacement, "Replacement is null.");
		placeholders.put(placeholder, String.valueOf(replacement));
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 * @param placeholder the placeholder you want to add
	 * @param replacement the value you want to replace the placeholder with
	 * @param <T> any type
	 * @return the placeholders object
	 */
	public @Nonnull <T> Placeholders add(@Nonnull String placeholder, @Nonnull T replacement) {
		Objects.requireNonNull(placeholder, "Placeholder is null.");
		Objects.requireNonNull(replacement, "Replacement is null.");
		placeholders.put(placeholder, String.valueOf(replacement));
		return this;
	}

	/**
	 * Replaces all placeholders in a string with the replacement value.
	 * @param str the string to replace placeholders with
	 * @return the new string with replaced placeholders
	 */
	public @Nonnull String replace(@Nullable String str) {
		if (str == null) {
			return "";
		}

		// Replace the placeholders in the string.
		for (Map.Entry<String, String> replacement : placeholders.entrySet()) {
			str = str.replace(replacement.getKey(), replacement.getValue());
		}

		return Text.color(str);
	}

	/**
	 * Replaces all placeholders in all strings with the replacement value.
	 * @param strList the list of strings to replace placeholders with
	 * @return the new list of strings with replaced placeholders
	 */
	public @Nonnull List<String> replace(@Nullable List<String> strList) {
		if (strList == null) {
			return new ArrayList<>();
		}

		return strList.stream().map(this::replace).collect(Collectors.toList());
	}
}
