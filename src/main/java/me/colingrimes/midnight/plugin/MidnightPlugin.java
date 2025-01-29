package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.Midnight;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides methods to retrieve the loading {@link Midnight} plugin and a list of all Midnight plugins.
 */
public final class MidnightPlugin {

	private static Midnight plugin = null;

	/**
	 * Gets the loading plugin that extends {@link Midnight}.
	 * The loading plugin should be the actual Midnight jar that is loaded by the server.
	 *
	 * @return the loading Midnight plugin
	 * @throws IllegalStateException if the plugin does not extend Midnight
	 */
	@Nonnull
	public static synchronized Midnight get() {
		if (plugin != null) {
			return plugin;
		}

		JavaPlugin javaPlugin = JavaPlugin.getProvidingPlugin(MidnightPlugin.class);
		if (!(javaPlugin instanceof Midnight midnight)) {
			throw new IllegalStateException("Loading plugin does not use the Midnight library.");
		}

		plugin = midnight;
		return plugin;
	}

	/**
	 * Gets a list of all plugins that extend {@link Midnight}.
	 *
	 * @return a list of all Midnight plugins
	 */
	@Nonnull
	public static List<Midnight> getAll() {
		return Arrays.stream(Bukkit.getPluginManager().getPlugins())
				.filter(p -> p instanceof Midnight)
				.map(p -> (Midnight) p)
				.collect(Collectors.toList());
	}

	private MidnightPlugin() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
