package me.colingrimes.midnight.command.handler.util;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentListTest extends MockSetup {

	@Test
	void testStringArgument() {
		ArgumentList args = new ArgumentList(new String[] { "test" });

		Optional<String> arg1 = args.getOptional(0);
		assertTrue(arg1.isPresent());
		assertEquals("test", arg1.get());

		Optional<String> arg2 = args.getOptional(1);
		assertTrue(arg2.isEmpty());

		assertEquals("test", args.getLowercase(0));
		assertThrows(IndexOutOfBoundsException.class, () -> args.getLowercase(1));
	}

	@Test
	void testIntegerArgument() {
		ArgumentList args = new ArgumentList(new String[] { "123", "abc" });

		Optional<Integer> arg1 = args.getInt(0);
		assertTrue(arg1.isPresent());
		assertEquals(123, arg1.get());

		Optional<Integer> arg2 = args.getInt(1);
		assertTrue(arg2.isEmpty());

		Optional<Integer> arg3 = args.getInt(2);
		assertTrue(arg3.isEmpty());

		assertEquals(123, args.getIntOrDefault(0, 456));
		assertEquals(456, args.getIntOrDefault(1, 456));
	}

	@Test
	void testDoubleArgument() {
		ArgumentList args = new ArgumentList(new String[] { "123.45", "abc" });

		Optional<Double> arg1 = args.getDouble(0);
		assertTrue(arg1.isPresent());
		assertEquals(123.45, arg1.get());

		Optional<Double> arg2 = args.getDouble(1);
		assertTrue(arg2.isEmpty());

		Optional<Double> arg3 = args.getDouble(2);
		assertTrue(arg3.isEmpty());

		assertEquals(123.45, args.getDoubleOrDefault(0, 456.78));
		assertEquals(456.78, args.getDoubleOrDefault(1, 456.78));
	}

	@Test
	void testBooleanArgument() {
		ArgumentList args = new ArgumentList(new String[] { "true", "false", "abc" });

		Optional<Boolean> arg1 = args.getBoolean(0);
		assertTrue(arg1.isPresent());
		assertTrue(arg1.get());

		Optional<Boolean> arg2 = args.getBoolean(1);
		assertTrue(arg2.isPresent());
		assertFalse(arg2.get());

		Optional<Boolean> arg3 = args.getBoolean(2);
		assertTrue(arg3.isEmpty());

		Optional<Boolean> arg4 = args.getBoolean(3);
		assertTrue(arg4.isEmpty());

		assertTrue(args.getBooleanOrDefault(0, false));
		assertFalse(args.getBooleanOrDefault(1, true));
	}

	@Test
	void testPlayerArgument() {
		ArgumentList args = new ArgumentList(new String[] { "Colin", "Unknown" });

		Optional<Player> arg1 = args.getPlayer(0);
		assertTrue(arg1.isPresent());
		assertEquals(bukkit.player, arg1.get());

		Optional<Player> arg2 = args.getPlayer(1);
		assertTrue(arg2.isEmpty());

		Optional<Player> arg3 = args.getPlayer(2);
		assertTrue(arg3.isEmpty());
	}

	@Test
	void testDurationArgument() {
		ArgumentList args = new ArgumentList(new String[] { "1s", "2m", "3h", "4d", "5w", "6mo", "7y" });

		Optional<Duration> arg1 = args.getDuration(0);
		assertTrue(arg1.isPresent());
		assertEquals(Duration.ofSeconds(1), arg1.get());

		Optional<Duration> arg2 = args.getDuration(1);
		assertTrue(arg2.isPresent());
		assertEquals(Duration.ofMinutes(2), arg2.get());

		Optional<Duration> arg3 = args.getDuration(2);
		assertTrue(arg3.isPresent());
		assertEquals(Duration.ofHours(3), arg3.get());

		Optional<Duration> arg4 = args.getDuration(3);
		assertTrue(arg4.isPresent());
		assertEquals(Duration.ofDays(4), arg4.get());

		Optional<Duration> arg5 = args.getDuration(4);
		assertTrue(arg5.isPresent());
		assertEquals(Duration.ofDays(35), arg5.get());

		Optional<Duration> arg6 = args.getDuration(5);
		assertTrue(arg6.isPresent());
		assertEquals(Duration.ofDays(180), arg6.get());

		Optional<Duration> arg7 = args.getDuration(6);
		assertTrue(arg7.isPresent());
		assertEquals(Duration.ofDays(2555), arg7.get());

		Optional<Duration> arg8 = args.getDuration(7);
		assertTrue(arg8.isEmpty());
	}
}
