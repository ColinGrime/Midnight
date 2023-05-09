package me.colingrimes.midnight.cache.expiring;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RollingWindowCache is a specialized cache implementation that counts the number of occurrences of keys
 * within a specified time window.
 *
 * @param <K> the type of the keys in this cache
 */
public class RollingWindowCache<K> {

    private final ConcurrentHashMap<K, List<CacheEntry>> cache;
    private final Duration windowDuration;

    public RollingWindowCache(@Nonnull Duration windowDuration) {
        this.cache = new ConcurrentHashMap<>();
        this.windowDuration = windowDuration;
    }

    /**
     * Increments the count of the specified key in the cache.
     * If the key is not present, it will be added with a count of 1.
     *
     * @param key the key to increment the count for
     */
    public void increment(@Nonnull K key) {
        cache.compute(key, (k, entries) -> {
            if (entries == null) {
                entries = new ArrayList<>();
            }
            entries.add(new CacheEntry(Instant.now().plus(windowDuration)));
            return entries;
        });
    }

    /**
     * Gets the count of the specified key within the rolling time window.
     *
     * @param key the key to get the count for
     * @return the count of the key within the rolling time window
     */
    public int getCount(@Nonnull K key) {
        List<CacheEntry> entries = cache.get(key);
        if (entries == null) {
            return 0;
        }

        int count = 0;
        Iterator<CacheEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            CacheEntry entry = iterator.next();
            if (entry.isExpired()) {
                iterator.remove();
            } else {
                count++;
            }
        }

        return count;
    }

    private static class CacheEntry {
        private final Instant expirationTime;

        public CacheEntry(@Nonnull Instant expirationTime) {
            this.expirationTime = expirationTime;
        }

        public boolean isExpired() {
            return expirationTime.isBefore(Instant.now());
        }
    }
}
