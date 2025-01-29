package me.colingrimes.midnight.util.misc;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTest {

	@Test
	void testRandomness() {
		for (int i=0; i<1000; i++) {
			int whole = Random.number(10);
			assertTrue(whole >= 0 && whole < 10);

			int range = Random.number(5, 20);
			assertTrue(range >= 5 && range <= 20);

			double decimal = Random.decimal(1.0, 10.0);
			assertTrue(decimal >= 1.0 && decimal <= 10.0);
		}
	}

	@Test
	void testChance() {
		long whole = IntStream.rangeClosed(0, 1000).filter(__ -> Random.chance(50)).count();
		long decimal = IntStream.rangeClosed(0, 1000).filter(__ -> Random.chance(50.0)).count();

		assertTrue(whole > 1 && whole < 999);
		assertTrue(decimal > 1 && decimal < 999);
	}
}
