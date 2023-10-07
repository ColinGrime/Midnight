package me.colingrimes.midnight.util.io.visitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassFileVisitorTest {

	private TestableClassFileVisitor classFileVisitor;

	@BeforeEach
	public void setUp() {
		Path mockStartingPath = mock(Path.class);
		when(mockStartingPath.toString()).thenReturn("file:/path/to/start/some/package");
		classFileVisitor = new TestableClassFileVisitor(mockStartingPath, "some.package");
	}

	@Test
	public void testConstructor() {
		Path mockStartingPath = mock(Path.class);
		when(mockStartingPath.toString()).thenReturn("file:/path/to/start/some/package");
		assertDoesNotThrow(() -> new TestableClassFileVisitor(mockStartingPath, "some.package"));
		assertThrows(IllegalArgumentException.class, () -> new TestableClassFileVisitor(mockStartingPath, "some.other.package"));
	}

	@Test
	public void testVisitFile() {
		Path validStringPath = mock(Path.class);
		Path invalidDoublePath = mock(Path.class);
		Path invalidFloatPath = mock(Path.class);
		Path validIntegerPath = mock(Path.class);
		Path invalidCharacterPath = mock(Path.class);

		when(validStringPath.toString()).thenReturn("file:/path/to/start/some/package/String.class");
		when(invalidDoublePath.toString()).thenReturn("file:/path/to/start/fake/some/package/Double.class");
		when(invalidFloatPath.toString()).thenReturn("file:/path/to/start/some/package/extra/Float.class");
		when(validIntegerPath.toString()).thenReturn("file:/path/to/start/some/package/Integer.class");
		when(invalidCharacterPath.toString()).thenReturn("file:/path/to/start/some/package/Character");

		classFileVisitor.visitFile(validStringPath, null);
		assertEquals(1, classFileVisitor.getList().size());
		assertTrue(classFileVisitor.getList().contains(String.class));

		assertThrows(IllegalArgumentException.class, () -> classFileVisitor.visitFile(invalidDoublePath, null));

		classFileVisitor.visitFile(invalidFloatPath, null);
		assertEquals(1, classFileVisitor.getList().size());
		assertFalse(classFileVisitor.getList().contains(Float.class));

		classFileVisitor.visitFile(validIntegerPath, null);
		assertEquals(2, classFileVisitor.getList().size());
		assertTrue(classFileVisitor.getList().contains(Integer.class));

		classFileVisitor.visitFile(invalidCharacterPath, null);
		assertEquals(2, classFileVisitor.getList().size());
		assertFalse(classFileVisitor.getList().contains(Character.class));
	}

	private static class TestableClassFileVisitor extends ClassFileVisitor {

		public TestableClassFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName) {
			super(startingPath, packageName, TestableClassFileVisitor.class.getClassLoader());
		}

		@Override
		protected void addClass(@Nonnull String className) {
			switch (className) {
				case "some.package.String" -> getList().add(String.class);
				case "some.package.Integer" -> getList().add(Integer.class);
				case "some.package.Double" -> getList().add(Double.class);
				case "some.package.Float" -> getList().add(Float.class);
				case "some.package.Character" -> getList().add(Character.class);
			}
		}
	}
}
