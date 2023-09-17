package me.colingrimes.midnight.util.io;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.Files.*;

public final class Files {

	/**
	 * Gets all classes in the given package, recursively.
	 *
	 * @param classLoader the class loader that is getting the classes
	 * @param packageName the package name
	 * @return the list of classes
	 */
	@Nonnull
	public static List<Class<?>> getClasses(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return getClasses(classLoader, packageName, true);
	}

	/**
	 * Gets all classes in the given package.
	 *
	 * @param classLoader the class loader that is getting the classes
	 * @param packageName the package name
	 * @param recursive   whether to recursively search sub-packages
	 * @return the list of classes
	 */
	@Nonnull
	public static List<Class<?>> getClasses(@Nonnull ClassLoader classLoader, @Nonnull String packageName, boolean recursive) {
		List<Class<?>> classes = new ArrayList<>();
		String path = packageName.replace('.', '/');
		int maxDepth = recursive ? Integer.MAX_VALUE : 1;

		try {
			URI uri = getUri(classLoader, path);
			if (uri == null) {
				return classes;
			}

			// If the URI is not a JAR, walk the directory.
			if (!uri.getScheme().equals("jar")) {
				Path packagePath = Paths.get(uri);
				walkFileTree(packagePath, Set.of(FileVisitOption.FOLLOW_LINKS), maxDepth, new CustomFileVisitor(classLoader, packageName, packagePath, classes));
				return classes;
			}

			// If the URI is a JAR, walk the JAR.
			try (FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>())) {
				Path packagePath = fileSystem.getPath(path);
				walkFileTree(packagePath, Set.of(FileVisitOption.FOLLOW_LINKS), maxDepth, new CustomFileVisitor(classLoader, packageName, packagePath, classes));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * Gets all package names in the given package, one level deep.
	 *
	 * @param classLoader the class loader that is getting the package names
	 * @param packageName the package name
	 * @return the list of package names
	 */
	@Nonnull
	public static List<String> getPackageNames(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		List<String> subPackages = new ArrayList<>();
		String path = packageName.replace('.', '/');

		try {
			URI uri = getUri(classLoader, path);
			if (uri == null) {
				return subPackages;
			}

			// If the URI is not a JAR, walk the directory.
			if (!uri.getScheme().equals("jar")) {
				try (DirectoryStream<Path> stream = newDirectoryStream(Paths.get(uri))) {
					for (Path subPath : stream) {
						if (isDirectory(subPath)) {
							subPackages.add(subPath.getFileName().toString());
						}
					}
				}
				return subPackages;
			}

			// If the URI is a JAR, walk the JAR.
			try (FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>())) {
				Path packagePath = fileSystem.getPath(path);
				try (DirectoryStream<Path> stream = newDirectoryStream(packagePath)) {
					for (Path subPath : stream) {
						if (isDirectory(subPath)) {
							subPackages.add(subPath.getFileName().toString());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return subPackages;
	}

	/**
	 * Gets the URI of the given path.
	 *
	 * @param path the path
	 * @return the URI
	 */
	@Nullable
	private static URI getUri(@Nonnull ClassLoader classLoader, @Nonnull String path) {
		try {
			return Objects.requireNonNull(classLoader.getResource(path)).toURI();
		} catch (NullPointerException | URISyntaxException e) {
			return null;
		}
	}

	/**
	 * A custom file visitor that adds each valid class to the given list.
	 */
	public static class CustomFileVisitor extends SimpleFileVisitor<Path> {

		private final ClassLoader classLoader;
		private final String packageName;
		private final Path packagePath;
		private final List<Class<?>> classes;

		public CustomFileVisitor(@Nonnull ClassLoader classLoader, @Nonnull String packageName, @Nonnull Path packagePath, @Nonnull List<Class<?>> classes) {
			this.classLoader = classLoader;
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
				classes.add(Class.forName(packageName + className, true, classLoader));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			return FileVisitResult.CONTINUE;
		}
	}

	private Files() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
