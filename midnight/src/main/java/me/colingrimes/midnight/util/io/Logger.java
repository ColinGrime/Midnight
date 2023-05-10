package me.colingrimes.midnight.util.io;

import me.colingrimes.midnight.Midnight;
import org.bukkit.Bukkit;

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
		Bukkit.getLogger().log(Level.INFO, msg);
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
		Bukkit.getLogger().log(Level.WARNING, msg);
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
		Bukkit.getLogger().log(Level.SEVERE, msg);
	}

	private Logger() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}