package me.colingrimes.midnight.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Provides various common utility methods.
 */
public final class Common {

	/**
	 * Gets the server instance.
	 *
	 * @return the server instance
	 */
	public static Server server() {
		return Bukkit.getServer();
	}

	/**
	 * Gets the specified plugin instance.
	 *
	 * @param name the name of the plugin
	 * @return the plugin instance, if it exists
	 */
	@Nullable
	public static Plugin plugin(@Nonnull String name) {
		return Bukkit.getPluginManager().getPlugin(name);
	}

	/**
	 * Disables the specified plugin.
	 *
	 * @param plugin the plugin to disable
	 */
	public static void disable(@Nonnull Plugin plugin) {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}

	/**
	 * Registers all the events of the given class.
	 *
	 * @param plugin   the plugin instance
	 * @param listener the listener class
	 * @param <T>      any type that extends listener
	 */
	public static <T extends Listener> void register(@Nonnull Plugin plugin, @Nonnull T listener) {
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}

	/**
	 * Calls the specified event.
	 *
	 * @param event the event class
	 * @param <T>   any type that extends event
	 * @return      the event that was called
	 */
	@Nonnull
	public static <T extends Event> T call(@Nonnull T event) {
		Bukkit.getPluginManager().callEvent(event);
		return event;
	}

	/**
	 * Loads the specified service, if available.
	 *
	 * @param clazz the service class to load
	 * @param <T>   the type of the service
	 * @return an Optional containing the service provider if registered, otherwise an empty Optional
	 */
	@Nonnull
	public static <T> Optional<T> service(@Nonnull Class<T> clazz) {
		RegisteredServiceProvider<T> rsp = Bukkit.getServicesManager().getRegistration(clazz);
		if (rsp == null) {
			return Optional.empty();
		} else {
			return Optional.of(rsp.getProvider());
		}
	}

	/**
	 * Checks if there is a valid Vault Economy provider.
	 *
	 * @return true if vault exists and an economy plugin is registered
	 */
	public static boolean hasEconomy() {
		return plugin("Vault") != null && service(Economy.class).isPresent();
	}

	/**
	 * Returns the registered Vault Economy provider.
	 * <p>
	 * It is recommended to fail early with {@link Common#hasEconomy()} in your plugin enable method,
	 * as this will throw an error if no economy provider is present.
	 *
	 * @return the economy provider
	 * @throws NoSuchElementException if vault is not present or if no economy plugin is registered
	 */
	@Nonnull
	public static Economy economy() {
		if (plugin("Vault") == null) {
			throw new NoSuchElementException("Vault is not present.");
		} else {
			return service(Economy.class).orElseThrow(() -> new NoSuchElementException("Vault is present, but no Economy provider is registered."));
		}
	}

	/**
	 * Broadcasts a message to all players.
	 *
	 * @param msg the message to broadcast
	 */
	public static void broadcast(@Nonnull String msg) {
		Bukkit.broadcastMessage(msg);
	}

	/**
	 * Gets the server logger.
	 *
	 * @return the server logger
	 */
	public static Logger logger() {
		return server().getLogger();
	}

	private Common() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
