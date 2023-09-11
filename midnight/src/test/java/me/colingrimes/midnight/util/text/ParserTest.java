package me.colingrimes.midnight.util.text;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

	@Test
	void testParser() {
		assertNull(Parser.parseParticle("INVALID_PARTICLE"));
		assertEquals(Particle.REDSTONE, Parser.parseParticle("REDSTONE"));

		assertNull(Parser.parseVector("1.0,2.0"));
		assertEquals(new Vector(1.0, 2.0, 3.0), Parser.parseVector("1.0,2.0,3.0"));

		assertNull(Parser.parseColor("256,0,0"));
		assertEquals(Parser.parseColor("255,0,0"), Color.RED);
	}
}
