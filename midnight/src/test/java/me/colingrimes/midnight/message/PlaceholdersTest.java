package me.colingrimes.midnight.message;

import net.md_5.bungee.api.chat.TextComponent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceholdersTest {

	@Test
	void testReplaceString() {
		Placeholders placeholders = Placeholders.of("{player}", "John");
		placeholders.add("{points}", 100);

		String input = "{player} has {points} points.";
		String expected = "John has 100 points.";
		String output = placeholders.apply(input);

		assertEquals(expected, output, "The replaced string should match the expected string");
	}

	@Test
	void testReplaceList() {
		Placeholders placeholders = Placeholders.of("{player}", "John");
		placeholders.add("{points}", 100);

		List<String> input = Arrays.asList("{player} has {points} points.", "Hello, {player}!");
		List<String> expected = Arrays.asList("John has 100 points.", "Hello, John!");
		List<String> output = placeholders.apply(input);

		assertEquals(expected, output, "The replaced list should match the expected list");
	}

	@Test
	void testReplaceTextComponent() {
		Placeholders placeholders = Placeholders.of("{player}", "John");
		placeholders.add("{points}", 100);

		TextComponent input = new TextComponent("{player} has {points} points.");
		TextComponent expected = new TextComponent("John has 100 points.");
		TextComponent output = placeholders.apply(input);

		assertEquals(expected.getText(), output.getText(), "The replaced TextComponent should match the expected TextComponent");
	}
}
