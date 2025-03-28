package me.colingrimes.midnight.util.text;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides various string operations.
 */
public final class Text {

	private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("\\s+|:+|-+|_+");

	/**
	 * Turns the string into a color-coded string.
	 *
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
	 *
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
	 * <p>In doing so, the following actions are performed:</p>
	 * <ul>
	 *   <li>Replaces all colons, dashes, and underscores with spaces.</li>
	 *   <li>Capitalizes the first letter of each word.</li>
	 * </ul>
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
		str = SPECIAL_CHARACTERS.matcher(str).replaceAll(" ");

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
	 * Formats the specified Duration into a "m:ss" time format.
	 *
	 * @param duration the duration
	 * @return formatted time
	 */
	@Nonnull
	public static String formatTime(@Nullable Duration duration) {
		if (duration == null) {
			return "";
		}

		long minutes = duration.toMinutes();
		long seconds = duration.minusMinutes(minutes).getSeconds();
		return String.format("%d:%02d", minutes, seconds);
	}

	/**
	 * Strips the specified message.
	 *
	 * @param message the message
	 * @return new message with spaces, dashes, and colons removed
	 */
	@Nonnull
	public static String strip(@Nullable String message) {
		return message == null ? "" : message.replace("\\s+", "").replace("-", "").replace(":", "");
	}

	private Text() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}