package me.colingrimes.midnight.util;

import me.colingrimes.midnight.MidnightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Provides various common utility methods.
 */
public final class Common {

	/**
	 * Gets the server instance.
	 * @return the server instance
	 */
	public static Server server() {
		return Bukkit.getServer();
	}

	/**
	 * Registers all the events of the given class.
	 * @param listener the listener class
	 * @param <T> any type that extends listener
	 */
	public static <T extends Listener> void register(@Nonnull T listener) {
		Bukkit.getPluginManager().registerEvents(listener, MidnightPlugin.getInstance());
	}

	/**
	 * Calls the specified event.
	 * @param event the event class
	 * @param <T> any type that extends event
	 */
	public static <T extends Event> void call(@Nonnull T event) {
		Bukkit.getPluginManager().callEvent(event);
	}

	/**
	 * Broadcasts a message to all players.
	 * @param msg the message to broadcast
	 */
	public static void broadcast(@Nonnull String msg) {
		Bukkit.broadcastMessage(msg);
	}

	/**
	 * Gets a world by name.
	 * @param name the name of the world
	 * @return the world, if it exists
	 */
	@Nonnull
	public static Optional<World> world(@Nonnull String name) {
		return Optional.ofNullable(Bukkit.getWorld(name));
	}

	private Common() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
