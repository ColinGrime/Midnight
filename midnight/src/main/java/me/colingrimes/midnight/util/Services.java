package me.colingrimes.midnight.util;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Provides utility methods for working with the {@link ServicesManager}.
 */
public final class Services {

    /**
     * Registers a service with the default priority.
     *
     * @param clazz    the service class
     * @param provider the service provider
     * @param <T>      the type of the service
     */
    public static <T> void register(@Nonnull Class<T> clazz, @Nonnull T provider) {
        register(clazz, provider, ServicePriority.Normal);
    }

    /**
     * Registers a service with the specified priority.
     *
     * @param clazz    the service class
     * @param provider the service provider
     * @param priority the priority for the service
     * @param <T>      the type of the service
     */
    public static <T> void register(@Nonnull Class<T> clazz, @Nonnull T provider, @Nonnull ServicePriority priority) {
        register(clazz, provider, MidnightPlugin.get(), priority);
    }

    /**
     * Registers a service with the specified priority.
     *
     * @param clazz    the service class
     * @param provider the service provider
     * @param plugin   the plugin providing the service
     * @param priority the priority for the service
     * @param <T>      the type of the service
     */
    public static <T> void register(@Nonnull Class<T> clazz, @Nonnull T provider, @Nonnull Plugin plugin, @Nonnull ServicePriority priority) {
        Bukkit.getServicesManager().register(clazz, provider, plugin, priority);
    }

    /**
     * Loads the specified service, if available.
     *
     * @param clazz the service class
     * @param <T>   the type of the service
     * @return      the instance of the requested service
     * @throws IllegalStateException if the requested service is not available
     */
    @Nonnull
    public static <T> T load(@Nonnull Class<T> clazz) {
        return get(clazz).orElseThrow(() -> new IllegalStateException("Service not available: " + clazz.getName()));
    }

    /**
     * Gets the provider for the specified service, if available.
     *
     * @param clazz the service class
     * @param <T>   the type of the service
     * @return an Optional containing the service provider if registered, otherwise an empty Optional
     */
    @Nonnull
    public static <T> Optional<T> get(@Nonnull Class<T> clazz) {
        RegisteredServiceProvider<T> rsp = Bukkit.getServicesManager().getRegistration(clazz);
        if (rsp == null) {
            return Optional.empty();
        } else {
            return Optional.of(rsp.getProvider());
        }
    }

    private Services() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
