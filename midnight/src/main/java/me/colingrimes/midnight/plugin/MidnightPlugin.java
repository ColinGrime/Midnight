package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.annotation.AnnotationRegistry;
import me.colingrimes.midnight.command.CommandManager;
import me.colingrimes.midnight.command.annotation.processor.CommandProcessor;
import me.colingrimes.midnight.config.ConfigurationManager;
import me.colingrimes.midnight.config.annotation.processor.ConfigurationProcessor;
import me.colingrimes.midnight.util.Common;
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
	private ConfigurationManager configurationManager;
	private CommandManager commandManager;

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
		configurationManager = new ConfigurationManager();
		commandManager = new CommandManager(this);
		load();
	}

	@Override
	public void onEnable() {
		loadAnnotations();
		registerListeners();
		enable();
	}

	@Override
	public void onDisable() {
		disable();
	}

	private void loadAnnotations() {
		AnnotationRegistry annotationRegistry = new AnnotationRegistry(this);
		annotationRegistry.register(new ConfigurationProcessor(this));
		annotationRegistry.register(new CommandProcessor(this));
		annotationRegistry.process();
	}

	private void registerListeners() {
		List<Listener> listeners = new ArrayList<>();
		registerListeners(listeners);

		for (Listener listener : listeners) {
			Common.registerEvents(listener);
		}
	}

	/**
	 * Get the instance of the plugin.
	 * @return instance of the plugin
	 */
	@Nonnull
	public static Plugin getInstance() {
		return instance;
	}

	/**
	 * Get the configuration manager.
	 * @return configuration manager
	 */
	@Nonnull
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/**
	 * Get the command manager.
	 * @return command manager
	 */
	@Nonnull
	public CommandManager getCommandManager() {
		return commandManager;
	}
}
