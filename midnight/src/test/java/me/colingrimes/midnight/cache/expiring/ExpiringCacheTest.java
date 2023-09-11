package me.colingrimes.midnight.cache.expiring;

import me.colingrimes.midnight.cache.ExpiringCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ExpiringCacheTest {

	private static final String KEY = "testKey";
	private ExpiringCache<String, Integer> cache;

	@BeforeEach
	void setUp() {
		cache = new SimpleExpiringCache<>(Duration.of(1, ChronoUnit.SECONDS));
	}

	@Test
	void testExpiration() throws InterruptedException {
		cache.setExpiration(Duration.of(100, ChronoUnit.MILLIS));
		assertEquals(Duration.of(100, ChronoUnit.MILLIS), cache.getExpiration());

		cache.put(KEY, 1);
		assertTrue(cache.containsKey(KEY));
		Thread.sleep(cache.getExpiration().toMillis() + 10);
		assertFalse(cache.containsKey(KEY));
	}

	@Test
	void testUnsupportedOperationExceptions() {
		assertThrows(UnsupportedOperationException.class, () -> cache.putAll(new HashMap<>()));
		assertThrows(UnsupportedOperationException.class, () -> cache.entrySet());
		assertThrows(UnsupportedOperationException.class, () -> cache.values());
		assertThrows(UnsupportedOperationException.class, () -> cache.containsValue(1));
	}
}
