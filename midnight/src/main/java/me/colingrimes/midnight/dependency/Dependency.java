package me.colingrimes.midnight.dependency;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * A utility class to manage dependencies.
 */
public final class Dependency {

    /**
     * Retrieves a plugin instance by its name.
     * @param pluginName the name of the plugin
     * @return an Optional containing the plugin instance if it is enabled, otherwise an empty Optional
     */
    @Nonnull
    public static Optional<Plugin> getPlugin(@Nonnull String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null && plugin.isEnabled()) {
            return Optional.of(plugin);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieves a service registered by a plugin.
     * @param serviceClass the class of the service to retrieve
     * @param <T> the type of the service
     * @return an Optional containing the service if it is registered, otherwise an empty Optional
     */
    @Nonnull
    public static <T> Optional<T> getService(@Nonnull Class<T> serviceClass) {
        RegisteredServiceProvider<T> rsp = Bukkit.getServicesManager().getRegistration(serviceClass);
        if (rsp != null) {
            return Optional.of(rsp.getProvider());
        } else {
            return Optional.empty();
        }
    }

    private Dependency() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
