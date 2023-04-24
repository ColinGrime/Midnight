package me.colingrimes.midnight.util.player;

import me.colingrimes.midnight.util.text.Text;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Provides a quick way to debug on the server.
 * {@link Debug#DEBUG} must be true for debug messages to appear.
 */
public final class Debug {

	// Toggles debug messages on/off.
	private static boolean DEBUG = false;

	/**
	 * Sends a debug message to the specified player.
	 * @param player the player to send the debug message to
	 * @param msg the message to send
	 */
	public static void send(@Nonnull Player player, @Nonnull String msg) {
		Objects.requireNonNull(player, "Player is null.");
		Objects.requireNonNull(msg, "Message is null.");

		if (DEBUG) {
			player.sendMessage(Text.color("&c[Debug] &e" + msg));
		}
	}

	/**
	 * Sends a debug key/value to the specified player.
	 * @param player the player to send the debug message to
	 * @param key any key
	 * @param value the value of the key
	 */
	public static <T> void send(@Nonnull Player player, @Nonnull String key, @Nonnull T value) {
		Objects.requireNonNull(player, "Player is null.");
		Objects.requireNonNull(key, "Key is null.");
		Objects.requireNonNull(value, "Value is null.");

		if (DEBUG) {
			String valueStr = value instanceof String ? Text.format((String) value) : String.valueOf(value);
			player.sendMessage(Text.color(String.format("&c[Debug] &e%s: &a%s", key, valueStr)));
		}
	}

	private Debug() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
