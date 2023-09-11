package me.colingrimes.midnight.util.text;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

	@Test
	void testColor() {
		String expectedString = ChatColor.GOLD + "Hello " + ChatColor.RED + "world!";
		String resultString = Text.color("&6Hello &cworld!");
		assertEquals(expectedString, resultString);

		List<String> expectedList = List.of(ChatColor.GOLD + "Hello", ChatColor.RED + "world!");
		List<String> resultList = Text.color(List.of("&6Hello", "&cworld!"));
		assertEquals(expectedList, resultList);
	}

	@Test
	void testFormats() {
		String expectedFormat = "Hello World Test Format";
		String resultFormat = Text.format("hello_world:test-format");
		assertEquals(expectedFormat, resultFormat);

		String expectedUnformat = "hello_world_test_format";
		String resultUnformat = Text.unformat("Hello World Test Format");
		assertEquals(expectedUnformat, resultUnformat);
	}
}
