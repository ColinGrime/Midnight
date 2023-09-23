package me.colingrimes.midnight.util.misc;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateTest {

	@Test
	public void testCheckMap() {
		Map<String, Object> validMap = Map.of("key1", new Object(), "key2", new Object());
		Map<String, Object> invalidMap = Collections.emptyMap();

		assertDoesNotThrow(() -> Validate.checkMap(validMap, "key1", "key2"));
		assertThrows(IllegalArgumentException.class, () -> Validate.checkMap(validMap, "key3"));
		assertThrows(IllegalArgumentException.class, () -> Validate.checkMap(invalidMap, "key1"));
		assertThrows(IllegalArgumentException.class, () -> Validate.checkMap(invalidMap, "key2"));
	}
}
