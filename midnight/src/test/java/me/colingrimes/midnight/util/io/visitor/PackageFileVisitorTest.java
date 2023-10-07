package me.colingrimes.midnight.util.io.visitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageFileVisitorTest {

	private PackageFileVisitor packageFileVisitor;

	@BeforeEach
	public void setUp() {
		Path mockStartingPath = mock(Path.class);
		when(mockStartingPath.toString()).thenReturn("file:/path/to/start/some/package");
		packageFileVisitor = new PackageFileVisitor(mockStartingPath, "some.package");
	}

	@Test
	public void testConstructor() {
		Path mockStartingPath = mock(Path.class);
		when(mockStartingPath.toString()).thenReturn("file:/path/to/start/some/package");
		assertDoesNotThrow(() -> new PackageFileVisitor(mockStartingPath, "some.package"));
		assertThrows(IllegalArgumentException.class, () -> new PackageFileVisitor(mockStartingPath, "some.other.package"));
	}

	@Test
	public void testPreVisitDirectory() {
		Path validPackagePath = mock(Path.class);
		Path invalidPackagePath = mock(Path.class);
		Path validPackagePathLong = mock(Path.class);

		when(validPackagePath.toString()).thenReturn("file:/path/to/start/some/package/subpackage");
		when(invalidPackagePath.toString()).thenReturn("file:/path/to/start/fake/some/package");
		when(validPackagePathLong.toString()).thenReturn("file:/path/to/start/some/package/subpackage/longer");

		packageFileVisitor.preVisitDirectory(validPackagePath, null);
		assertEquals(1, packageFileVisitor.getList().size());
		assertTrue(packageFileVisitor.getList().contains("some.package.subpackage"));

		assertThrows(IllegalArgumentException.class, () -> packageFileVisitor.preVisitDirectory(invalidPackagePath, null));

		packageFileVisitor.preVisitDirectory(validPackagePathLong, null);
		assertEquals(2, packageFileVisitor.getList().size());
		assertTrue(packageFileVisitor.getList().contains("some.package.subpackage.longer"));
	}
}
