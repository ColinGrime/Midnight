package me.colingrimes.midnight.cache.expiring;

import me.colingrimes.midnight.cache.ExpiringCache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleExpiringCache<K, V> implements ExpiringCache<K, V> {

    private final ConcurrentHashMap<K, CacheEntry<V>> cache;
    private Duration expiration;

    public SimpleExpiringCache(@Nonnull Duration expiration) {
        this.cache = new ConcurrentHashMap<>();
        this.expiration = expiration;
    }

    @Override
    public @Nonnull Duration getExpiration() {
        return expiration;
    }

    @Override
    public void setExpiration(@Nonnull Duration duration) {
        this.expiration = duration;
    }

    @Nullable
    @Override
    public V get(@Nonnull Object key) {
        try {
            @SuppressWarnings("unchecked")
            K k = (K) key;
            CacheEntry<V> entry = cache.get(k);
            if (entry != null && !isExpired(entry)) {
                return entry.getValue();
            } else {
                cache.remove(k);
                return null;
            }
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    @Override
    public V put(@Nonnull K key, @Nonnull V value) {
        CacheEntry<V> oldEntry = cache.put(key, new CacheEntry<>(value, Instant.now(), expiration));
        return oldEntry == null ? null : oldEntry.getValue();
    }

    @Nullable
    @Override
    public V remove(@Nonnull Object key) {
        CacheEntry<V> oldEntry = cache.remove(key);
        return oldEntry == null ? null : oldEntry.getValue();
    }

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public boolean containsKey(@Nonnull Object key) {
        try {
            @SuppressWarnings("unchecked")
            K k = (K) key;
            return cache.containsKey(k) && !isExpired(cache.get(k));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public boolean containsValue(@Nonnull Object value) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    private boolean isExpired(@Nonnull CacheEntry<V> entry) {
        return entry.getExpirationTime().isBefore(Instant.now());
    }

    private static class CacheEntry<V> {
        private final V value;
        private final Instant expirationTime;

        public CacheEntry(@Nonnull V value, @Nonnull Instant creationTime, @Nonnull Duration expiration) {
            this.value = value;
            this.expirationTime = creationTime.plus(expiration);
        }

        @Nonnull
        public V getValue() {
            return value;
        }

        @Nonnull
        public Instant getExpirationTime() {
            return expirationTime;
        }
    }
}
