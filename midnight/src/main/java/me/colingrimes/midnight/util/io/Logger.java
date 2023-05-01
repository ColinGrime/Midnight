package me.colingrimes.midnight.util.io;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Provides an easy way to log messages to the server.
 */
public final class Logger {

	/**
	 * Logs a normal message.
	 * @param msg the info message to log
	 */
	public static void log(@Nonnull String msg) {
		Objects.requireNonNull(msg);
		Bukkit.getLogger().log(Level.INFO, msg);
	}

	/**
	 * Logs a warning message.
	 * @param msg the warning message to log
	 */
	public static void warn(@Nonnull String msg) {
		Objects.requireNonNull(msg);
		Bukkit.getLogger().log(Level.WARNING, msg);
	}

	/**
	 * Logs a severe message.
	 * @param msg the severe message to log
	 */
	public static void severe(@Nonnull String msg) {
		Objects.requireNonNull(msg);
		Bukkit.getLogger().log(Level.SEVERE, msg);
	}

	private Logger() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}