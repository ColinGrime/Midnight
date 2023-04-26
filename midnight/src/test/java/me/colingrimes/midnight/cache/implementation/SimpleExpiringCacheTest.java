package me.colingrimes.midnight.cache.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class SimpleExpiringCacheTest {

	private static final String KEY = "testKey";
	private SimpleExpiringCache<String, Integer> cache;

	@BeforeEach
	void setUp() {
		cache = new SimpleExpiringCache<>(Duration.of(1, ChronoUnit.SECONDS));
	}

	@Test
	void testPutAndGet() {
		cache.put(KEY, 1);
		assertEquals(1, cache.get(KEY), "Value retrieved should match the value put into cache");
	}

	@Test
	void testExpiration() throws InterruptedException {
		cache.put(KEY, 1);
		Thread.sleep(1100);
		assertNull(cache.get(KEY), "Value should be null after expiration");
	}

	@Test
	void testContainsKey() {
		cache.put(KEY, 1);
		assertTrue(cache.containsKey(KEY), "Cache should contain the key after putting a value");
	}

	@Test
	void testNotContainsKeyAfterExpiration() throws InterruptedException {
		cache.put(KEY, 1);
		Thread.sleep(1100);
		assertFalse(cache.containsKey(KEY), "Cache should not contain the key after expiration");
	}

	@Test
	void testRemove() {
		cache.put(KEY, 1);
		assertEquals(1, cache.remove(KEY), "Removed value should match the value put into cache");
		assertNull(cache.get(KEY), "Value should be null after removal");
	}

	@Test
	void testClear() {
		cache.put(KEY, 1);
		cache.clear();
		assertNull(cache.get(KEY), "Value should be null after clearing the cache");
	}

	@Test
	void testIsEmpty() {
		assertTrue(cache.isEmpty(), "Cache should be empty initially");
	}

	@Test
	void testNotEmptyAfterPut() {
		cache.put(KEY, 1);
		assertFalse(cache.isEmpty(), "Cache should not be empty after putting a value");
	}

	@Test
	void testSize() {
		assertEquals(0, cache.size(), "Cache size should be 0 initially");
		cache.put(KEY, 1);
		assertEquals(1, cache.size(), "Cache size should be 1 after putting a value");
	}

	@Test
	void testSetAndGetExpiration() {
		Duration newExpiration = Duration.of(2, ChronoUnit.SECONDS);
		cache.setExpiration(newExpiration);
		assertEquals(newExpiration, cache.getExpiration(), "Expiration should match the new value after setting it");
	}

	@Test
	void testUnsupportedOperationExceptions() {
		assertThrows(UnsupportedOperationException.class, () -> cache.putAll(null), "putAll should throw an UnsupportedOperationException");
		assertThrows(UnsupportedOperationException.class, () -> cache.entrySet(), "entrySet should throw an UnsupportedOperationException");
		assertThrows(UnsupportedOperationException.class, () -> cache.values(), "values should throw an UnsupportedOperationException");
		assertThrows(UnsupportedOperationException.class, () -> cache.containsValue(1), "containsValue should throw an UnsupportedOperationException");
	}
}
