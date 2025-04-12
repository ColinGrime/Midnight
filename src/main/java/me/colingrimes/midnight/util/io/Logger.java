package me.colingrimes.midnight.util.io;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.util.Common;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;

/**
 * Provides an easy way to log messages to the server.
 */
public final class Logger {

	/**
	 * Logs a normal message.
	 *
	 * @param plugin the plugin to log for
	 * @param msg    the info message to log
	 */
	public static void log(@Nonnull Plugin plugin, @Nonnull String msg) {
		log("[" + plugin.getName() + "] " + msg);
	}

	/**
	 * Logs a normal message.
	 *
	 * @param msg the info message to log
	 */
	public static void log(@Nonnull String msg) {
		Common.logger().log(Level.INFO, msg);
	}

	/**
	 * Logs a warning message.
	 *
	 * @param plugin the plugin to log for
	 * @param msg    the warning message to log
	 */
	public static void warn(@Nonnull Plugin plugin, @Nonnull String msg) {
		warn("[" + plugin.getName() + "] " + msg);
	}

	/**
	 * Logs a warning message.
	 *
	 * @param msg the warning message to log
	 */
	public static void warn(@Nonnull String msg) {
		Common.logger().log(Level.WARNING, msg);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param plugin the plugin to log for
	 * @param msg    the severe message to log
	 */
	public static void severe(@Nonnull Plugin plugin, @Nonnull String msg) {
		severe(plugin, msg, null);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param msg the severe message to log
	 */
	public static void severe(@Nonnull String msg) {
		severe(msg, null);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param plugin the plugin to log for
	 * @param msg    the severe message to log
	 * @param thrown throwable associated with log message
	 */
	public static void severe(@Nonnull Plugin plugin, @Nonnull String msg, @Nullable Throwable thrown) {
		severe("[" + plugin.getName() + "] " + msg, thrown);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param msg the severe message to log
	 * @param thrown throwable associated with log message
	 */
	public static void severe(@Nonnull String msg, @Nullable Throwable thrown) {
		if (thrown == null) {
			Common.logger().log(Level.SEVERE, msg);
		} else {
			Common.logger().log(Level.SEVERE, msg, thrown);
		}
	}

	/**
	 * Logs a debug message.
	 *
	 * @param msg the debug message to log
	 */
	public static void debug(@Nonnull String msg) {
		if (Midnight.DEBUG) {
			log("[Midnight-DEBUG] " + msg);
		}
	}

	/**
	 * Logs a debug message with arguments.
	 *
	 * @param msg the debug message to log
	 * @param args the arguments to format the message with
	 */
	public static void debug(@Nonnull String msg, Object... args) {
		if (Midnight.DEBUG) {
			log("[Midnight-DEBUG] " + String.format(msg, args));
		}
	}

	private Logger() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}