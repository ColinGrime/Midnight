package me.colingrimes.midnight.util.text;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

	enum TestEnum {
		TEST,
		TEST2,
	}

	@Test
	void testParse() {
		assertEquals(Parser.parse(TestEnum.class, "TEST"), Optional.of(TestEnum.TEST));
		assertEquals(Parser.parse(TestEnum.class, "test2"), Optional.of(TestEnum.TEST2));
		assertEquals(Parser.parse(TestEnum.class, "INVALID"), Optional.empty());
	}

	@Test
	void testParseNullable() {
		assertEquals(Parser.parseNullable(TestEnum.class, "TEST"), TestEnum.TEST);
		assertEquals(Parser.parseNullable(TestEnum.class, "test2"), TestEnum.TEST2);
		assertNull(Parser.parseNullable(TestEnum.class, "INVALID"));
	}

	@Test
	void testParseDuration() {
		assertEquals(Parser.parseDuration("1s"), Duration.ofSeconds(1));
		assertEquals(Parser.parseDuration("2sec"), Duration.ofSeconds(2));
		assertEquals(Parser.parseDuration("3secs"), Duration.ofSeconds(3));
		assertEquals(Parser.parseDuration("4second"), Duration.ofSeconds(4));
		assertEquals(Parser.parseDuration("5seconds"), Duration.ofSeconds(5));
		assertEquals(Parser.parseDuration("6m"), Duration.ofMinutes(6));
		assertEquals(Parser.parseDuration("7min"), Duration.ofMinutes(7));
		assertEquals(Parser.parseDuration("8mins"), Duration.ofMinutes(8));
		assertEquals(Parser.parseDuration("9minute"), Duration.ofMinutes(9));
		assertEquals(Parser.parseDuration("10minutes"), Duration.ofMinutes(10));
		assertEquals(Parser.parseDuration("11h"), Duration.ofHours(11));
		assertEquals(Parser.parseDuration("12hr"), Duration.ofHours(12));
		assertEquals(Parser.parseDuration("13hrs"), Duration.ofHours(13));
		assertEquals(Parser.parseDuration("14hour"), Duration.ofHours(14));
		assertEquals(Parser.parseDuration("15hours"), Duration.ofHours(15));
		assertEquals(Parser.parseDuration("16d"), Duration.ofDays(16));
		assertEquals(Parser.parseDuration("17day"), Duration.ofDays(17));
		assertEquals(Parser.parseDuration("18days"), Duration.ofDays(18));
		assertEquals(Parser.parseDuration("19w"), Duration.ofDays(19 * 7));
		assertEquals(Parser.parseDuration("20wk"), Duration.ofDays(20 * 7));
		assertEquals(Parser.parseDuration("21wks"), Duration.ofDays(21 * 7));
		assertEquals(Parser.parseDuration("22week"), Duration.ofDays(22 * 7));
		assertEquals(Parser.parseDuration("23weeks"), Duration.ofDays(23 * 7));
		assertEquals(Parser.parseDuration("24mo"), Duration.ofDays(24 * 30));
		assertEquals(Parser.parseDuration("25mos"), Duration.ofDays(25 * 30));
		assertEquals(Parser.parseDuration("26month"), Duration.ofDays(26 * 30));
		assertEquals(Parser.parseDuration("27months"), Duration.ofDays(27 * 30));
		assertEquals(Parser.parseDuration("28y"), Duration.ofDays(28 * 365));
		assertEquals(Parser.parseDuration("29yr"), Duration.ofDays(29 * 365));
		assertEquals(Parser.parseDuration("30yrs"), Duration.ofDays(30 * 365));
		assertEquals(Parser.parseDuration("31year"), Duration.ofDays(31 * 365));
		assertEquals(Parser.parseDuration("32years"), Duration.ofDays(32 * 365));
		assertNull(Parser.parseDuration("33INVALID"));
		assertNull(Parser.parseDuration("INVALID"));
	}

	@Test
	void testParseParticle() {
		assertEquals(Parser.parseParticle("REDSTONE"), Particle.REDSTONE);
		assertEquals(Parser.parseParticle("redstone"), Particle.REDSTONE);
		assertNull(Parser.parseParticle("INVALID_PARTICLE"));
	}

	@Test
	void testParseVector() {
		assertEquals(Parser.parseVector("1.0,2.0,3.0"), new Vector(1.0, 2.0, 3.0));
		assertEquals(Parser.parseVector(("1,2,3")), new Vector(1, 2, 3));
		assertNull(Parser.parseVector("1.0,2.0"));
		assertNull(Parser.parseVector("1.0,2.0,3.0,abc"));
	}

	@Test
	void testParseColor() {
		assertEquals(Parser.parseColor("255,0,0"), Color.RED);
		assertEquals(Parser.parseColor("0,255,0"), Color.LIME);
		assertEquals(Parser.parseColor("0,0,255"), Color.BLUE);
		assertNull(Parser.parseColor("256,0,0"));
		assertNull(Parser.parseColor("0,256,0"));
		assertNull(Parser.parseColor("0,0,256"));
		assertNull(Parser.parseColor("0,0"));
	}
}
