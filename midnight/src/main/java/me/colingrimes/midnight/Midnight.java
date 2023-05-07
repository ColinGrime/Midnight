package me.colingrimes.midnight;

import me.colingrimes.midnight.annotation.AnnotationRegistry;
import me.colingrimes.midnight.command.annotation.processor.CommandProcessor;
import me.colingrimes.midnight.command.registry.CommandRegistry;
import me.colingrimes.midnight.command.registry.util.CommandPackageScanner;
import me.colingrimes.midnight.config.ConfigurationManager;
import me.colingrimes.midnight.config.annotation.processor.ConfigurationProcessor;
import me.colingrimes.midnight.dependency.Dependencies;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Timer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Midnight extends JavaPlugin {

	private Dependencies dependencies;
	private CommandRegistry commandRegistry;
	private ConfigurationManager configurationManager;

	// Override these methods to add functionality to your plugin.
	protected void load() {}
	protected void enable() {}
	protected void disable() {}
	protected void registerListeners(@Nonnull List<Listener> listeners) {}

	@Override
	public void onLoad() {
		dependencies = new Dependencies(this);
		commandRegistry = new CommandRegistry(this);
		configurationManager = new ConfigurationManager();
		load();
	}

	@Override
	public void onEnable() {
		loadCommands();
		loadAnnotations();
		registerListeners();
		enable();
	}

	@Override
	public void onDisable() {
		disable();
	}

	/**
	 * Loads all the package-based commands.
	 */
	private void loadCommands() {
		Timer.time(this, "Command Registration", () -> {
			CommandPackageScanner.scanAndRegister(this);
		});
	}

	/**
	 * Loads all the annotations.
	 * This includes @Configuration and @Command annotations.
	 */
	private void loadAnnotations() {
		Timer.time(this, "Annotation Registration", () -> {
			AnnotationRegistry annotationRegistry = new AnnotationRegistry(this);
			annotationRegistry.register(new ConfigurationProcessor(this));
			annotationRegistry.register(new CommandProcessor(this));
			annotationRegistry.process();
		});
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
	 * @return root package
	 */
	@Nonnull
	public String getRootPackage() {
		return getClass().getPackage().getName();
	}

	/**
	 * Gets the dependencies of the plugin.
	 * @return dependencies
	 */
	@Nonnull
	public Dependencies getDependencies() {
		return dependencies;
	}

	/**
	 * Loads a required dependency by its name.
	 * If the dependency is not found, the plugin will be disabled.
	 *
	 * @param name the name of the plugin
	 */
	public void depend(@Nonnull String name) {
		dependencies.depend(name);
	}

	/**
	 * Loads a soft dependency by its name.
	 * @param name the name of the plugin
	 */
	public void softDepend(@Nonnull String name) {
		dependencies.softDepend(name);
	}

	/**
	 * Gets a loaded plugin instance by its name.
	 * @param name the name of the plugin
	 * @return the loaded plugin instance, or null if not found or not enabled
	 */
	@Nullable
	public Plugin getDependency(@Nonnull String name) {
		return dependencies.getDependency(name);
	}

	/**
	 * Gets a loaded plugin instance by its name, and casts it to the given class.
	 * Warning: Should only be used if the plugin is required.
	 *
	 * @param name the name of the plugin
	 * @param clazz the class of the plugin
	 * @return the loaded plugin instance, or null if not found or not enabled
	 */
	@Nullable
	public <T extends Plugin> T getDependency(@Nonnull String name, @Nonnull Class<T> clazz) {
		return dependencies.getDependency(name, clazz);
	}

	/**
	 * Gets the command registry.
	 * @return command registry
	 */
	@Nonnull
	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}

	/**
	 * Gets the configuration manager.
	 * @return configuration manager
	 */
	@Nonnull
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
}
