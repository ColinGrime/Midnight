package me.colingrimes.midnight.util.io.visitor;

import me.colingrimes.midnight.MockSetup;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class PackageFileVisitorTest extends MockSetup {

	private final Path startingPath = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test");

	@Test
	void testConstructor() {
		assertDoesNotThrow(() -> new PackageFileVisitor(startingPath, "me.colingrimes.midnight.test"));
		assertThrows(IllegalArgumentException.class, () -> new PackageFileVisitor(startingPath, "me.colingrimes.midnight.test2"));
	}

	@Test
	void testPreVisitDirectory() {
		PackageFileVisitor packageFileVisitor = new PackageFileVisitor(startingPath, "me.colingrimes.midnight.test");
		Path invalidPath = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test2");
		Path invalidPath2 = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "extra", "test");
		Path invalidPath3 = Paths.get("src", "test", "java", "me", "colingrimes", "extra", "midnight", "test");
		Path validPath = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test", "test2");

		assertThrows(IllegalArgumentException.class, () -> packageFileVisitor.preVisitDirectory(invalidPath, null));
		assertThrows(IllegalArgumentException.class, () -> packageFileVisitor.preVisitDirectory(invalidPath2, null));
		assertThrows(IllegalArgumentException.class, () -> packageFileVisitor.preVisitDirectory(invalidPath3, null));

		packageFileVisitor.preVisitDirectory(validPath, null);
		assertEquals(1, packageFileVisitor.getList().size());
		assertEquals("me.colingrimes.midnight.test.test2", packageFileVisitor.getList().get(0));
	}
}
