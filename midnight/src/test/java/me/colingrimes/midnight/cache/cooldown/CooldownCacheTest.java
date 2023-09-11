package me.colingrimes.midnight.cache.cooldown;

import me.colingrimes.midnight.cache.CooldownCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class CooldownCacheTest {

	private static final String KEY = "testKey";
	private CooldownCache<String> cache;
	private Duration defaultDuration;

	@BeforeEach
	void setUp() {
		defaultDuration = Duration.ofMillis(100);
		cache = new SimpleCooldownCache<>(defaultDuration);
	}

	@Test
	void testAdd() {
		assertFalse(cache.onCooldown(KEY));

		cache.add(KEY);
		assertTrue(cache.onCooldown(KEY));
		assertEquals(defaultDuration, cache.getCooldown());

		cache.add(KEY, Duration.ofMillis(50));
		assertTrue(cache.onCooldown(KEY));
		assertTrue(cache.onCooldown(KEY));
	}

	@Test
	void testOnCooldown() throws InterruptedException {
		cache.add(KEY);
		assertTrue(cache.onCooldown(KEY));
		Thread.sleep(defaultDuration.toMillis() + 10);
		assertFalse(cache.onCooldown(KEY));
	}

	@Test
	void setCooldown() {
		cache.setCooldown(Duration.ofMillis(200));
		assertEquals(Duration.ofMillis(200), cache.getCooldown());
	}
}
