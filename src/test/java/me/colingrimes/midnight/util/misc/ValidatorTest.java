package me.colingrimes.midnight.util.misc;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

	@Test
	void testCheckMap() {
		Map<String, Object> validMap = Map.of("key1", new Object(), "key2", new Object());
		Map<String, Object> invalidMap = Collections.emptyMap();

		assertDoesNotThrow(() -> Validator.checkMap(validMap, "key1", "key2"));
		assertThrows(IllegalArgumentException.class, () -> Validator.checkMap(validMap, "key3"));
		assertThrows(IllegalArgumentException.class, () -> Validator.checkMap(invalidMap, "key1"));
		assertThrows(IllegalArgumentException.class, () -> Validator.checkMap(invalidMap, "key2"));
	}
}
