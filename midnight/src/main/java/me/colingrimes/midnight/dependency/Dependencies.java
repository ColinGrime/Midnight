package me.colingrimes.midnight.dependency;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A class to manage and load plugin dependencies.
 */
public class Dependencies {

    private final MidnightPlugin plugin;
    private final Map<Class<? extends Plugin>, Plugin> dependencies = new HashMap<>();

    public Dependencies(@Nonnull MidnightPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads a required dependency by its class.
     * If the dependency is not found, the plugin will be disabled.
     *
     * @param clazz the class of the plugin
     */
    public <T extends Plugin> void depend(@Nonnull Class<T> clazz) {
        depend(clazz, clazz.getSimpleName());
    }

    /**
     * Loads a required dependency by its class and name.
     * If the dependency is not found, the plugin will be disabled.
     *
     * @param clazz the class of the plugin
     * @param name the name of the plugin
     */
    public <T extends Plugin> void depend(@Nonnull Class<T> clazz, @Nonnull String name) {
        Optional<Plugin> plugin = Dependency.getPlugin(name);
        if (plugin.isEmpty()) {
            Logger.severe("Failed to load required dependency: " + name + " (" + clazz.getName() + ")");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
            return;
        }

        // Plugin already loaded.
        if (dependencies.containsKey(clazz)) {
            Logger.warn("Dependency already loaded: " + name + " (" + clazz.getName() + ")");
            return;
        }

        // Ensure the plugin is the correct type.
        if (clazz.isInstance(plugin.get())) {
            dependencies.put(clazz, plugin.get());
            Logger.log("Loaded dependency: " + name + " (" + clazz.getName() + ")");
        } else {
            Logger.severe("Failed to cast required dependency: " + name + " (" + clazz.getName() + ")");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }
    }

    /**
     * Loads a soft dependency by its class.
     * @param clazz the class of the plugin
     */
    public <T extends Plugin> void softDepend(@Nonnull Class<T> clazz) {
        softDepend(clazz, clazz.getSimpleName());
    }

    /**
     * Loads a soft dependency by its name and class.
     * @param clazz the class of the plugin
     * @param name the name of the plugin
     */
    public <T extends Plugin> void softDepend(@Nonnull Class<T> clazz, @Nonnull String name) {
        Optional<Plugin> plugin = Dependency.getPlugin(name);
        if (plugin.isEmpty()) {
            return;
        }

        // Plugin already loaded.
        if (dependencies.containsKey(clazz)) {
            Logger.warn("Dependency already loaded: " + name + " (" + clazz.getName() + ")");
            return;
        }

        // Ensure the plugin is the correct type.
        if (clazz.isInstance(plugin.get())) {
            dependencies.put(clazz, plugin.get());
            Logger.log("Loaded soft dependency: " + name + " (" + clazz.getName() + ")");
        } else {
            Logger.warn("Failed to cast soft dependency: " + name + " (" + clazz.getName() + ")");
        }
    }

    /**
     * Gets a loaded plugin instance by its class.
     * @param clazz the class of the plugin
     * @return the loaded plugin instance, or null if not found or not enabled
     */
    @Nullable
    public <T extends Plugin> T getDependency(@Nonnull Class<T> clazz) {
        Plugin plugin = dependencies.get(clazz);
        return plugin == null ? null : clazz.cast(plugin);
    }
}
