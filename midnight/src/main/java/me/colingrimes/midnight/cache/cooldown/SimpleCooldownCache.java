package me.colingrimes.midnight.cache.cooldown;

import me.colingrimes.midnight.cache.CooldownCache;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCooldownCache<K> implements CooldownCache<K> {

    private final ConcurrentHashMap<K, Instant> cache;
    private Duration cooldown;

    public SimpleCooldownCache(@Nonnull Duration cooldown) {
        this.cache = new ConcurrentHashMap<>();
        this.cooldown = cooldown;
    }

    @Override
    public void add(@Nonnull K key) {
        cache.put(key, Instant.now().plus(cooldown));
    }

    @Override
    public void add(@Nonnull K key, @Nonnull Duration duration) {
        cache.put(key, Instant.now().plus(duration));
    }

    @Override
    public boolean onCooldown(@Nonnull K key) {
        Instant expirationTime = cache.get(key);
        if (expirationTime != null) {
            if (expirationTime.isAfter(Instant.now())) {
                return true;
            } else {
                cache.remove(key);
            }
        }
        return false;
    }

    @Override
    public @Nonnull Duration getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(@Nonnull Duration duration) {
        this.cooldown = duration;
    }
}
