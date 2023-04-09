package me.colingrimes.midnight.util;

import me.colingrimes.midnight.plugin.Midnight;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Provides various common utility methods.
 */
public final class Common {

	/**
	 * Registers all the events of the given class.
	 * @param listener the listener class
	 * @param <T> any type that extends listener
	 */
	public static <T extends Listener> void registerEvents(T listener) {
		Bukkit.getPluginManager().registerEvents(listener, Midnight.getInstance());
	}

	private Common() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
