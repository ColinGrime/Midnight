package me.colingrimes.midnight.util.text;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

	@Test
	void testParseParticle() {
		String input = "REDSTONE";
		Particle expected = Particle.REDSTONE;

		Particle result = Parser.parseParticle(input);
		assertEquals(expected, result, "Parsed particle should match the expected particle");
	}

	@Test
	void testParseInvalidParticle() {
		String input = "INVALID_PARTICLE";
		Particle result = Parser.parseParticle(input);
		assertNull(result, "Invalid particle should return null");
	}

	@Test
	void testParseVector() {
		String input = "1.0,2.0,3.0";
		Vector expected = new Vector(1.0, 2.0, 3.0);

		Vector result = Parser.parseVector(input);
		assertEquals(expected, result, "Parsed vector should match the expected vector");
	}

	@Test
	void testParseInvalidVector() {
		String input = "1.0,2.0";
		Vector result = Parser.parseVector(input);
		assertNull(result, "Invalid vector should return null");
	}

	@Test
	void testParseColor() {
		String input = "255,0,0";
		Color expected = Color.RED;

		Color result = Parser.parseColor(input);
		assertEquals(expected, result, "Parsed color should match the expected color");
	}

	@Test
	void testParseInvalidColor() {
		String input = "256,0,0";
		Color result = Parser.parseColor(input);
		assertNull(result, "Invalid color should return null");
	}
}
