package me.colingrimes.midnight.util.misc;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TypesTest {

	@Test
	public void testAsStringList() {
		Optional<List<String>> stringList = Types.asStringList(Arrays.asList("a", "b", "c"));
		assertTrue(stringList.isPresent());
		assertEquals(Arrays.asList("a", "b", "c"), stringList.get());

		Optional<List<String>> integerList = Types.asStringList(Arrays.asList(1, 2, 3));
		assertTrue(integerList.isEmpty());

		Optional<List<String>> mixedList = Types.asStringList(Arrays.asList("a", 2, "c"));
		assertTrue(mixedList.isEmpty());

		Optional<List<String>> emptyList = Types.asStringList(Collections.emptyList());
		assertTrue(emptyList.isEmpty());

		Optional<List<String>> nonList = Types.asStringList("Not a List");
		assertTrue(nonList.isEmpty());
	}
}
