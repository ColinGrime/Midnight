package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.annotation.AnnotationRegistry;
import me.colingrimes.midnight.config.ConfigurationManager;
import me.colingrimes.midnight.config.annotation.processor.ConfigurationProcessor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public abstract class Midnight extends JavaPlugin {

	private static Midnight instance;
	private ConfigurationManager configurationManager;

	public Midnight() {
		super();
	}

	protected Midnight(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	abstract void load();
	abstract void enable();
	abstract void disable();

	@Override
	public void onLoad() {
		instance = this;
		configurationManager = new ConfigurationManager();
		load();
	}

	@Override
	public void onEnable() {
		loadAnnotations();
		enable();
	}

	@Override
	public void onDisable() {
		disable();
	}

	private void loadAnnotations() {
		AnnotationRegistry annotationRegistry = new AnnotationRegistry(this);
		annotationRegistry.register(new ConfigurationProcessor(this));
//		annotationRegistry.register(new CommandProcessor(this));
		annotationRegistry.process();
	}

	public static Midnight getInstance() {
		return instance;
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
}
