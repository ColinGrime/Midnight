package me.colingrimes.midnight;

import me.colingrimes.midnight.command.registry.CommandRegistry;
import me.colingrimes.midnight.config.ConfigurationManager;
import me.colingrimes.midnight.config.annotation.ConfigurationProcessor;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class Midnight extends JavaPlugin {

	public static final boolean DEBUG = false;
	private final ConfigurationManager configurationManager = new ConfigurationManager();

	// Override these methods to add functionality to your plugin.
	protected void load() {}
	protected void enable() {}
	protected void disable() {}
	protected void registerListeners(@Nonnull List<Listener> listeners) {}

	@Override
	public void onLoad() {
		load();
	}

	@Override
	public void onEnable() {
		MidnightPlugin.get();

		// Register all package-based commands.
		CommandRegistry commandRegistry = new CommandRegistry(this);
		commandRegistry.scanPackages();

		// Register all configuration classes.
		ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(this);
		configurationProcessor.process();

		registerListeners();
		enable();
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
		disable();
	}

	/**
	 * Registers all listeners for the plugin.
	 * This includes listeners from the {@link #registerListeners(List)} method.
	 */
	private void registerListeners() {
		List<Listener> listeners = new ArrayList<>();
		registerListeners(listeners);
		listeners.forEach(listener -> Common.register(this, listener));
	}

	/**
	 * Gets the root package of the plugin.
	 * Override this package if you put the plugin in a subpackage.
	 *
	 * @return root package
	 */
	@Nonnull
	public String getRootPackage() {
		return getClass().getPackage().getName();
	}

	/**
	 * Gets the configuration manager.
	 *
	 * @return configuration manager
	 */
	@Nonnull
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
}
