package me.colingrimes.midnight.util.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilesTest {

	@Mock private ClassLoader mockClassLoader;
	private Path packagePath;
	private FileSystem mockFileSystem;

	private MockedStatic<Paths> mockPaths;
	private MockedStatic<FileSystems> mockFileSystems;

	@BeforeEach
	public void setUp() {
		packagePath = mock(Path.class);
		mockPaths = mockStatic(Paths.class);
		mockPaths.when(() -> Paths.get(any(URI.class))).thenReturn(packagePath);

		mockFileSystem = mock(FileSystem.class);
		mockFileSystems = mockStatic(FileSystems.class);
		mockFileSystems.when(() -> FileSystems.newFileSystem(any(URI.class), anyMap())).thenReturn(mockFileSystem);
	}

	@AfterEach
	public void tearDown() {
		mockPaths.close();
		mockFileSystems.close();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetClasses() throws Exception {
		// Mocking walkFileTree to simulate that it's finding classes.
		MockedStatic<java.nio.file.Files> mockFiles = mockStatic(java.nio.file.Files.class);
		mockFiles.when(() -> java.nio.file.Files.walkFileTree(eq(packagePath), anySet(), anyInt(), any())).thenAnswer(invocation -> {
			try {
				Field field = Files.CustomFileVisitor.class.getDeclaredField("classes");
				field.setAccessible(true);

				List<Class<?>> classes = (List<Class<?>>) field.get(invocation.getArgument(3));
				classes.add(String.class);
				classes.add(Integer.class);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		});

		List<Class<?>> expected = Arrays.asList(String.class, Integer.class);
		String packageName = "some.package";
		String path = packageName.replace('.', '/');

		// Non-JAR case.
		when(mockClassLoader.getResource(path)).thenReturn(new URI("file://some/path").toURL());
		assertEquals(expected, Files.getClasses(mockClassLoader, packageName));

		// JAR case.
		when(mockClassLoader.getResource(path)).thenReturn(new URI("jar:file:/path/to/jar.jar!/some/path").toURL());
		when(mockFileSystem.getPath(path)).thenReturn(packagePath);
		assertEquals(expected, Files.getClasses(mockClassLoader, packageName));

		mockFiles.close();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetPackageNames() throws Exception {
		List<String> expectedSubPackages = Arrays.asList("subPackage1", "subPackage2");
		String packageName = "some.package";
		String path = packageName.replace('.', '/');

		// Mocking the iteration of the directory stream.
		DirectoryStream<Path> mockDirectoryStream = mock(DirectoryStream.class);
		when(mockDirectoryStream.iterator()).thenAnswer(invocation -> expectedSubPackages.stream().map(sub -> {
			Path subPath = mock(Path.class);
			Path fileName = mock(Path.class);
			when(subPath.getFileName()).thenReturn(fileName);
			when(fileName.toString()).thenReturn(sub);
			return subPath;
		}).iterator());

		// Mocking the directory calls.
		MockedStatic<java.nio.file.Files> mockFiles = mockStatic(java.nio.file.Files.class);
		mockFiles.when(() -> java.nio.file.Files.newDirectoryStream(packagePath)).thenReturn(mockDirectoryStream);
		mockFiles.when(() -> java.nio.file.Files.isDirectory(any())).thenReturn(true);

		// Non-JAR case.
		when(mockClassLoader.getResource(path)).thenReturn(new URI("file://some/path").toURL());
		assertEquals(expectedSubPackages, Files.getPackageNames(mockClassLoader, packageName));

		// JAR case.
		when(mockClassLoader.getResource(path)).thenReturn(new URI("jar:file:/path/to/jar.jar!/some/path").toURL());
		when(mockFileSystem.getPath(path)).thenReturn(packagePath);
		assertEquals(expectedSubPackages, Files.getPackageNames(mockClassLoader, packageName));

		mockFiles.close();
	}
}
