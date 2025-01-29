package me.colingrimes.midnight.cache;

import me.colingrimes.midnight.scheduler.Scheduler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Cooldown<K> {

    private final Map<K, CooldownEntry<K>> cooldowns = new ConcurrentHashMap<>();
    private final Duration defaultDuration;
    private final Consumer<K> defaultAction;

    /**
     * Creates a cooldown cache with a default duration.
     *
     * @param defaultDuration the default duration of the cooldown period
     * @return a new cooldown cache
     * @param <K> the type of the keys in this cache
     */
    public static <K> Cooldown<K> create(@Nonnull Duration defaultDuration) {
        return new Cooldown<>(defaultDuration, null);
    }

    /**
     * Creates a cooldown cache with a default duration and a default action to be executed when the cooldown period ends.
     *
     * @param defaultDuration the default duration of the cooldown period
     * @param defaultAction the default action to be executed when the cooldown period ends
     * @return a new cooldown cache
     * @param <K> the type of the keys in this cache
     */
    public static <K> Cooldown<K> create(@Nonnull Duration defaultDuration, @Nullable Consumer<K> defaultAction) {
        return new Cooldown<>(defaultDuration, defaultAction);
    }

    private Cooldown(@Nonnull Duration defaultDuration, @Nullable Consumer<K> defaultAction) {
        this.defaultDuration = defaultDuration;
        this.defaultAction = defaultAction;
        this.schedule();
    }

    /**
     * Checks for expired cooldowns and executes the action if necessary.
     */
    private void schedule() {
        Scheduler.sync().runRepeating(() -> {
            cooldowns.entrySet().stream().filter(e -> !onCooldown(e.getKey())).forEach(e -> {
                cancel(e.getKey());
                if (e.getValue().action != null) {
                    e.getValue().action.accept(e.getKey());
                }
            });
        }, 0, 20);
    }

    /**
     * Adds an entry to the cooldown cache with the default duration.
     *
     * @param key the key to be added
     */
    public void add(@Nonnull K key) {
        add(key, defaultDuration);
    }

    /**
     * Adds an entry to the cooldown cache with the specified duration.
     *
     * @param key the key to be added
     * @param duration the duration of the cooldown period
     */
    public void add(@Nonnull K key, @Nonnull Duration duration) {
        add(key, duration, defaultAction);
    }

    /**
     * Adds an entry to the cooldown cache with the specified duration and action.
     *
     * @param key the key to be added
     * @param duration the duration of the cooldown period
     * @param action the action to be executed when the cooldown period ends
     */
    public void add(@Nonnull K key, @Nonnull Duration duration, @Nullable Consumer<K> action) {
        CooldownEntry<K> entry = new CooldownEntry<>(Instant.now().plus(duration), action);
        cooldowns.put(key, entry);
    }

    /**
     * Cancels the cooldown for a given key.
     *
     * @param key the key to be cancelled
     */
    public void cancel(@Nonnull K key) {
        cooldowns.remove(key);
    }

    /**
     * Checks if the specified key is still on cooldown.
     *
     * @param key the key to check
     * @return true if the key is on cooldown, false otherwise
     */
    public boolean onCooldown(@Nonnull K key) {
        CooldownEntry<K> entry = cooldowns.get(key);
        if (entry == null || entry.expiration == null) return false;
        return entry.expiration.isAfter(Instant.now());
    }

    private static class CooldownEntry<K> {
        Instant expiration;
        Consumer<K> action;

        CooldownEntry(@Nonnull Instant expiration, @Nullable Consumer<K> action) {
            this.expiration = expiration;
            this.action = action;
        }
    }
}
