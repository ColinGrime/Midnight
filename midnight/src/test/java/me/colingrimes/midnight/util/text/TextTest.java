package me.colingrimes.midnight.util.text;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

	@Test
	void testColorString() {
		String input = "&6Hello &cworld!";
		String expected = ChatColor.GOLD + "Hello " + ChatColor.RED + "world!";

		String result = Text.color(input);
		assertEquals(expected, result, "Colored text should match the expected format");
	}

	@Test
	void testColorList() {
		List<String> input = Arrays.asList("&6Hello", "&cworld!");
		List<String> expected = Arrays.asList(ChatColor.GOLD + "Hello", ChatColor.RED + "world!");

		List<String> result = Text.color(input);
		assertEquals(expected, result, "Colored list of strings should match the expected format");
	}

	@Test
	void testFormat() {
		String input = "hello_world:test-format";
		String expected = "Hello World Test Format";

		String result = Text.format(input);
		assertEquals(expected, result, "Formatted text should match the expected format");
	}

	@Test
	void testUnformat() {
		String input = "Hello World Test Format";
		String expected = "hello_world_test_format";

		String result = Text.unformat(input);
		assertEquals(expected, result, "Unformatted text should match the expected format");
	}
}
