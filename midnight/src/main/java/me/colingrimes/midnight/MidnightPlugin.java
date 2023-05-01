package me.colingrimes.midnight;

import me.colingrimes.midnight.annotation.AnnotationRegistry;
import me.colingrimes.midnight.command.annotation.processor.CommandProcessor;
import me.colingrimes.midnight.command.registry.CommandRegistry;
import me.colingrimes.midnight.command.registry.util.CommandPackageScanner;
import me.colingrimes.midnight.config.ConfigurationManager;
import me.colingrimes.midnight.config.annotation.processor.ConfigurationProcessor;
import me.colingrimes.midnight.display.DisplayFactory;
import me.colingrimes.midnight.display.manager.DisplayManager;
import me.colingrimes.midnight.listener.ArmorEquipListeners;
import me.colingrimes.midnight.listener.InventoryListener;
import me.colingrimes.midnight.menu.listener.MenuListener;
import me.colingrimes.midnight.particle.ParticleManager;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Timer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class MidnightPlugin extends JavaPlugin {

	private static Plugin instance;
	private CommandRegistry commandRegistry;

	private ConfigurationManager configurationManager;
	private ParticleManager particleManager;
	private DisplayManager displayManager;
	private DisplayFactory displayFactory;

	// Override these methods to add functionality to your plugin.
	protected void load() {}
	protected void enable() {}
	protected void disable() {}
	protected void registerListeners(@Nonnull List<? super Listener> listeners) {}

	@Override
	public void onLoad() {
		instance = this;
		commandRegistry = new CommandRegistry(this);
		configurationManager = new ConfigurationManager();
		particleManager = new ParticleManager();
		displayManager = new DisplayManager();
		displayFactory = new DisplayFactory(this);
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

		// Register default listeners.
		listeners.add(new MenuListener(this));
		listeners.add(new InventoryListener());
		listeners.add(new ArmorEquipListeners());

		// Register additional listeners.
		registerListeners(listeners);
		listeners.forEach(Common::register);
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
	 * Gets the instance of the plugin.
	 * @return instance of the plugin
	 */
	@Nonnull
	public static Plugin getInstance() {
		return instance;
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

	/**
	 * Gets the particle manager.
	 * @return particle manager
	 */
	@Nonnull
	public ParticleManager getParticleManager() {
		return particleManager;
	}

	/**
	 * Gets the display manager.
	 * @return display manager
	 */
	@Nonnull
	public DisplayManager getDisplayManager() {
		return displayManager;
	}

	/**
	 * Gets the display factory.
	 * @return display factory
	 */
	@Nonnull
	public DisplayFactory display() {
		return displayFactory;
	}
}
