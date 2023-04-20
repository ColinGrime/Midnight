package me.colingrimes.midnight;

import me.colingrimes.midnight.annotation.AnnotationRegistry;
import me.colingrimes.midnight.command.registry.CommandRegistry;
import me.colingrimes.midnight.command.annotation.processor.CommandProcessor;
import me.colingrimes.midnight.command.registry.util.CommandPackageScanner;
import me.colingrimes.midnight.config.ConfigurationManager;
import me.colingrimes.midnight.config.annotation.processor.ConfigurationProcessor;
import me.colingrimes.midnight.particle.ParticleManager;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.Timer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class MidnightPlugin extends JavaPlugin {

	private static Plugin instance;
	private CommandRegistry commandRegistry;
	private ConfigurationManager configurationManager;
	private ParticleManager particleManager;

	public MidnightPlugin() {
		super();
	}

	protected MidnightPlugin(@Nonnull JavaPluginLoader loader, @Nonnull PluginDescriptionFile description, @Nonnull File dataFolder, @Nonnull File file) {
		super(loader, description, dataFolder, file);
	}

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

	private void loadCommands() {
		Timer.time(this, "Command Registration", () -> {
			CommandPackageScanner.scanAndRegister(this);
		});
	}

	private void loadAnnotations() {
		Timer.time(this, "Annotation Registration", () -> {
			AnnotationRegistry annotationRegistry = new AnnotationRegistry(this);
			annotationRegistry.register(new ConfigurationProcessor(this));
			annotationRegistry.register(new CommandProcessor(this));
			annotationRegistry.process();
		});
	}

	private void registerListeners() {
		List<Listener> listeners = new ArrayList<>();
		registerListeners(listeners);

		for (Listener listener : listeners) {
			Common.register(listener);
		}
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
}
