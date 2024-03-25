package me.colingrimes.midnight.util.io.visitor;

import me.colingrimes.midnight.MockSetup;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ClassFileVisitorTest extends MockSetup {

	private final Path startingPath = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test");

	@Test
	void testConstructor() {
		assertDoesNotThrow(() -> new ClassFileVisitor(startingPath, "me.colingrimes.midnight.test", null));
		assertThrows(IllegalArgumentException.class, () -> new ClassFileVisitor(startingPath, "me.colingrimes.midnight.test2", null));
	}

	@Test
	void testVisitFile() {
		ClassFileVisitor classFileVisitor = new ClassFileVisitor(startingPath, "me.colingrimes.midnight.test", null);
		Path invalidPath = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test", "Test");
		Path invalidPath2 = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test", "Invalid.class");
		Path invalidPath3 = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test2", "Test.class");
		Path invalidPath4 = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "extra", "test", "Test.class");
		Path invalidPath5 = Paths.get("src", "test", "java", "me", "colingrimes", "extra", "midnight", "test", "Test.class");
		Path validPath = Paths.get("src", "test", "java", "me", "colingrimes", "midnight", "test", "Test.class");

		classFileVisitor.visitFile(invalidPath, null);
		assertThrows(RuntimeException.class, () -> classFileVisitor.visitFile(invalidPath2, null));
		assertThrows(IllegalArgumentException.class, () -> classFileVisitor.visitFile(invalidPath3, null));
		assertThrows(IllegalArgumentException.class, () -> classFileVisitor.visitFile(invalidPath4, null));
		assertThrows(IllegalArgumentException.class, () -> classFileVisitor.visitFile(invalidPath5, null));
		assertEquals(0, classFileVisitor.getList().size());

		classFileVisitor.visitFile(validPath, null);
		assertEquals(1, classFileVisitor.getList().size());
		assertEquals("me.colingrimes.midnight.test.Test", classFileVisitor.getList().get(0).getName());
	}
}
