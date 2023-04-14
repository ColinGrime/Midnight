package me.colingrimes.midnight.annotation.util;

import me.colingrimes.midnight.plugin.MidnightPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ClassFinder {

	/**
	 * Gets all classes in the given package.
	 * @param packageName the package name
	 * @return the list of classes
	 */
	@Nonnull
	public static List<Class<?>> getClasses(@Nonnull MidnightPlugin plugin, @Nonnull String packageName) {
		List<Class<?>> classes = new ArrayList<>();
		String path = packageName.replace('.', '/');

		try {
			URI uri = getUri(plugin.getClass().getClassLoader(), path);

			// If the URI is not a JAR, walk the directory.
			if (!uri.getScheme().equals("jar")) {
				Path packagePath = Paths.get(uri);
				Files.walkFileTree(packagePath, new CustomFileVisitor(plugin, packageName, packagePath, classes));
				return classes;
			}

			// If the URI is a JAR, walk the JAR.
			try (FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>())) {
				Path packagePath = fileSystem.getPath(path);
				Files.walkFileTree(packagePath, new CustomFileVisitor(plugin, packageName, packagePath, classes));
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * Gets the URI of the given path.
	 * @param path the path
	 * @return the URI
	 * @throws URISyntaxException if the URI is invalid
	 */
	@Nonnull
	private static URI getUri(@Nonnull ClassLoader classLoader, @Nonnull String path) throws URISyntaxException {
		URI uri;

		try {
			uri = classLoader.getResource(path).toURI();
		} catch (NullPointerException | URISyntaxException e) {
			throw new RuntimeException("Failed to get the URI for the path: " + path);
		}

		return uri;
	}

	private static class CustomFileVisitor extends SimpleFileVisitor<Path> {

		private final MidnightPlugin plugin;
		private final String packageName;
		private final Path packagePath;
		private final List<Class<?>> classes;

		public CustomFileVisitor(@Nonnull MidnightPlugin plugin, @Nonnull String packageName, @Nonnull Path packagePath, @Nonnull List<Class<?>> classes) {
			this.plugin = plugin;
			this.packageName = packageName;
			this.packagePath = packagePath;
			this.classes = classes;
		}

		@Nonnull
		@Override
		public FileVisitResult visitFile(@Nonnull Path file, @Nonnull BasicFileAttributes attrs) {
			if (!file.toString().endsWith(".class")) {
				return FileVisitResult.CONTINUE;
			}

			String className = file.toString();
			className = className.substring(packagePath.toString().length());
			className = className.replace(".class", "");
			className = className.replace(File.separator, ".");

			try {
				// Must use the ClassLoader of the plugin to load the class to avoid incorrect dependency warnings.
				classes.add(Class.forName(packageName + className, true, plugin.getClass().getClassLoader()));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			return FileVisitResult.CONTINUE;
		}
	}

	private ClassFinder() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
