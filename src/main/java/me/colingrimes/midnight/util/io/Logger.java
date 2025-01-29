package me.colingrimes.midnight.util.io;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.util.Common;

import javax.annotation.Nonnull;
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
	public static void log(@Nonnull Midnight plugin, @Nonnull String msg) {
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
	public static void warn(@Nonnull Midnight plugin, @Nonnull String msg) {
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
	public static void severe(@Nonnull Midnight plugin, @Nonnull String msg) {
		severe("[" + plugin.getName() + "] " + msg);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param msg the severe message to log
	 */
	public static void severe(@Nonnull String msg) {
		Common.logger().log(Level.SEVERE, msg);
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