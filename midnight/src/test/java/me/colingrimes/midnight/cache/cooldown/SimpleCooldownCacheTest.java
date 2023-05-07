package me.colingrimes.midnight.cache.cooldown;

import me.colingrimes.midnight.cache.cooldown.SimpleCooldownCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCooldownCacheTest {

	private static final String KEY = "testKey";
	private SimpleCooldownCache<String> cache;
	private Duration defaultDuration;

	@BeforeEach
	void setUp() {
		defaultDuration = Duration.ofMillis(100);
		cache = new SimpleCooldownCache<>(defaultDuration);
	}

	@Test
	void addAndGetCooldown() {
		assertFalse(cache.onCooldown(KEY), "Key should not be on cooldown before adding.");
		cache.add(KEY);
		assertTrue(cache.onCooldown(KEY), "Key should be on cooldown after adding.");
		assertEquals(defaultDuration, cache.getCooldown(), "Default cooldown should match.");
	}

	@Test
	void addWithCustomDuration() {
		cache.add(KEY, Duration.ofMillis(50));
		assertTrue(cache.onCooldown(KEY), "Key should be on cooldown after adding.");
	}

	@Test
	void onCooldown() throws InterruptedException {
		cache.add(KEY);
		assertTrue(cache.onCooldown(KEY), "Key should be on cooldown after adding.");

		// Wait for the cooldown to expire.
		Thread.sleep(defaultDuration.toMillis() + 10);

		assertFalse(cache.onCooldown(KEY), "Key should not be on cooldown after expiration.");
	}

	@Test
	void setCooldown() {
		Duration newDuration = Duration.ofMillis(200);
		cache.setCooldown(Duration.ofMillis(200));
		assertEquals(newDuration, cache.getCooldown(), "New cooldown should match.");
	}
}
