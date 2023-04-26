package me.colingrimes.midnight.cache;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * CooldownCache is an interface for caches that store keys with a cooldown period.
 * @param <K> the type of the keys in this cache
 */
public interface CooldownCache<K> {

    /**
     * Adds the specified key to the cache with the current time as the start of the cooldown period.
     * @param key the key to add
     */
    void add(@Nonnull K key);

    /**
     * Adds the specified key to the cache with the specified duration as the cooldown period.
     * @param key the key to add
     * @param duration the duration of the cooldown
     */
    void add(@Nonnull K key, @Nonnull Duration duration);

    /**
     * Checks if the specified key is on cooldown.
     * @param key the key to check
     * @return true if the key is on cooldown, false otherwise
     */
    boolean onCooldown(@Nonnull K key);

    /**
     * Returns the cooldown duration for keys in the cache.
     * @return the duration of the cooldown
     */
    @Nonnull
    Duration getCooldown();

    /**
     * Sets the cooldown duration for keys in the cache.
     * @param duration the duration of the cooldown
     */
    void setCooldown(@Nonnull Duration duration);
}
