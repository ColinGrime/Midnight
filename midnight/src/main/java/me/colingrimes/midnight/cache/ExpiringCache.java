package me.colingrimes.midnight.cache;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Map;

/**
 * ExpiringCache is an interface for caches with entries that expire after a specified duration.
 * @param <K> the type of the keys in this cache
 * @param <V> the type of the values in this cache
 */
public interface ExpiringCache<K, V> extends Map<K, V> {

    /**
     * Returns the expiration duration for entries in the cache.
     *
     * @return the duration after which entries expire
     */
    @Nonnull
    Duration getExpiration();

    /**
     * Sets the expiration duration for entries in the cache.
     *
     * @param duration the duration after which entries should expire
     */
    void setExpiration(@Nonnull Duration duration);
}

