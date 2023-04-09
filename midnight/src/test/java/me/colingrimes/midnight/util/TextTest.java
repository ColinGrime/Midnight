package me.colingrimes.midnight.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

	@Test
	@DisplayName("Verify that messages are being colored.")
	void testColor() {
		// Test valid input.
		Assertions.assertEquals("§cRed §1Blue", Text.color("&cRed &1Blue"));
		assertEquals(List.of("§cRed", "§1Blue"), Text.color(List.of("&cRed", "§1Blue")));

		// Test invalid input.
		assertEquals("", Text.color((String) null));
		assertEquals("", Text.color(""));
		assertEquals(new ArrayList<>(), Text.color((List<String>) null));
		assertEquals(new ArrayList<>(), Text.color(new ArrayList<>()));
	}

	@Test
	@DisplayName("Verify that messages are being formatted/unformatted.")
	void testFormat() {
		// Test valid input.
		assertEquals("Wither Skeleton", Text.format("wither_skeleton"));
		assertEquals("Iron Golem", Text.format("iRON-golEM"));
		assertEquals("Ender Dragon", Text.format("ENDER:DRAGON"));
		assertEquals("diamond_block", Text.unformat("Diamond Block"));

		// Test invalid input.
		assertEquals("", Text.format(null));
		assertEquals("", Text.format(""));
		assertEquals("", Text.unformat(null));
		assertEquals("", Text.unformat(""));
	}
}