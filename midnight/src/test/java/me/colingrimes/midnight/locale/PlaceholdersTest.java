package me.colingrimes.midnight.locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceholdersTest {

	@Test
	@DisplayName("Verify that placeholders are working.")
	void testPlaceholders() {
		// Create placeholders.
		Placeholders test1 = new Placeholders("Key", "Value");
		Placeholders test2 = new Placeholders("Hi", "Hello").add("Universe", "World");
		Placeholders test3 = new Placeholders().add(".", "!");
		Placeholders test4 = new Placeholders("Wow", 123).add("Pi", 3.14);

		// Test valid input.
		assertEquals("Value", test1.replace("Key"));
		assertEquals("Hello World!", test2.replace(test3.replace("Hi Universe.")));
		assertEquals("123, 3.14.", test4.replace("Wow, Pi."));

		List<String> strList = Arrays.asList("Hi", "Universe Key", "", "Test");
		assertEquals(List.of("Hello", "World Value", "", "Test"), test1.replace(test2.replace(strList)));

		// Test invalid input.
		assertEquals("", test1.replace((String) null));
		assertEquals("", test1.replace(""));
		assertEquals(List.of(), test1.replace((List<String>) null));
		assertEquals(List.of(), test1.replace(List.of()));
	}
}