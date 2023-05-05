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
    private final Map<String, Plugin> dependencies = new HashMap<>();

    public Dependencies(@Nonnull MidnightPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads a required dependency by its name.
     * If the dependency is not found, the plugin will be disabled.
     *
     * @param name the name of the plugin
     */
    public void depend(@Nonnull String name) {
        Optional<Plugin> dependency = Dependency.getPlugin(name);
        if (dependency.isEmpty()) {
            Logger.severe(plugin, "Failed to load required dependency: " + name);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        // Plugin already loaded.
        if (dependencies.containsKey(name)) {
            Logger.warn(plugin, "Dependency already loaded: " + name);
            return;
        }

        dependencies.put(name, dependency.get());
        Logger.log(plugin, "Loaded dependency: " + name);
    }

    /**
     * Loads a soft dependency by its name.
     * @param name the name of the plugin
     */
    public void softDepend(@Nonnull String name) {
        Optional<Plugin> dependency = Dependency.getPlugin(name);
        if (dependency.isEmpty()) {
            return;
        }

        // Plugin already loaded.
        if (dependencies.containsKey(name)) {
            Logger.warn(plugin, "Dependency already loaded: " + name);
            return;
        }

        dependencies.put(name, dependency.get());
        Logger.log("Loaded soft dependency: " + name);
    }

    /**
     * Gets a loaded plugin instance by its name.
     * @param name the name of the plugin
     * @return the loaded plugin instance, or null if not found or not enabled
     */
    @Nullable
    public Plugin getDependency(@Nonnull String name) {
        return dependencies.get(name);
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
        Plugin plugin = dependencies.get(name);
        return plugin == null ? null : clazz.cast(plugin);
    }
}
