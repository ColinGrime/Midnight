package me.colingrimes.midnight.util;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides various string operations.
 */
public final class Text {

	private static final Pattern specialChars = Pattern.compile("\\s+|:+|-+|_+");

	/**
	 * Turns the string into a color-coded string.
	 * @param str the string to be colored
	 * @return color-coded string
	 */
	@Nonnull
	public static String color(@Nullable String str) {
		if (str == null || str.isEmpty()) return "";
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	/**
	 * Turns the list of strings into a color-coded list of strings.
	 * @param strList the list of strings
	 * @return color-coded list of messages
	 */
	@Nonnull
	public static List<String> color(@Nullable List<String> strList) {
		if (strList == null || strList.isEmpty()) return new ArrayList<>();
		return strList.stream().map(Text::color).collect(Collectors.toList());
	}

	/**
	 * Formats a String object.
	 *
	 * In doing so, the following actions are performed:
	 * - Replaces all colons, dashes, and underscores with spaces.
	 * - Capitalizes the first letter of each word.
	 *
	 * @param str the string to be formatted
	 * @return formatted string
	 */
	@Nonnull
	public static String format(@Nullable String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}

		// Replaces all colons, dashes, and underscores with spaces.
		str = replaceSpecialCharacters(str, " ");

		StringBuilder builder = new StringBuilder(str.substring(0, 1).toUpperCase());
		for (int i=1; i<str.length(); i++) {
			if (i < str.length() - 1 && str.charAt(i) == ' ') {
				builder.append(" ").append(str.substring(++i, i+1).toUpperCase());
			} else {
				builder.append(str.substring(i, i+1).toLowerCase());
			}
		}

		return builder.toString();
	}

	/**
	 * Unformats a String object.
	 * In doing so, all capital letters become lowercase with an underscore behind them.
	 *
	 * @param str the string to be unformatted
	 * @return unformatted string
	 */
	@Nonnull
	public static String unformat(@Nullable String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder(str.substring(0, 1).toLowerCase());
		for (char c : str.substring(1).toCharArray()) {
			if (c == ' ') continue;
			if (Character.isUpperCase(c)) {
				builder.append("_").append(Character.toLowerCase(c));
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	/**
	 * Replaces special characters (spaces, colons, dashes, underscores) with a new character.
	 *
	 * @param str String to be replaced
	 * @param replacement String to replace special chars to
	 * @return replaced String
	 */
	@Nonnull
	private static String replaceSpecialCharacters(@Nullable String str, @Nonnull String replacement) {
		if (str == null) return "";
		return specialChars.matcher(str).replaceAll(replacement);
	}

	private Text() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}