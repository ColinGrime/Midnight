package me.colingrimes.midnight.util.io;

import me.colingrimes.midnight.util.io.visitor.BaseFileVisitor;
import me.colingrimes.midnight.util.io.visitor.ClassFileVisitor;
import me.colingrimes.midnight.util.io.visitor.PackageFileVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class Introspector {

	private enum FileVisitorType {
		CLASS,
		PACKAGE
	}

	/**
	 * Gets all classes in the given package, one level deep.
	 *
	 * @param classLoader the class loader that is getting the classes
	 * @param packageName the fully qualified package name
	 * @return the list of classes
	 */
	@Nonnull
	public static List<Class<?>> getClasses(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return walkFileSystem(classLoader, packageName, false, FileVisitorType.CLASS);
	}

	/**
	 * Gets all classes in the given package, recursively.
	 *
	 * @param classLoader the class loader that is getting the classes
	 * @param packageName the fully qualified package name
	 * @return the list of classes
	 */
	@Nonnull
	public static List<Class<?>> getClassesRecursively(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return walkFileSystem(classLoader, packageName, true, FileVisitorType.CLASS);
	}

	/**
	 * Gets all package names in the given package, one level deep.
	 *
	 * @param classLoader the class loader that is getting the package names
	 * @param packageName the fully qualified package name
	 * @return the list of package names
	 */
	@Nonnull
	public static List<String> getPackages(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return walkFileSystem(classLoader, packageName, false, FileVisitorType.PACKAGE);
	}

	/**
	 * Gets all package names in the given package, recursively.
	 *
	 * @param classLoader the class loader that is getting the package names
	 * @param packageName the fully qualified package name
	 * @return the list of package names
	 */
	@Nonnull
	public static List<String> getPackagesRecursively(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return walkFileSystem(classLoader, packageName, true, FileVisitorType.PACKAGE);
	}

	/**
	 * Walks the file system starting from a package, retrieving either classes or packages
	 * based on the specified {@link FileVisitorType}. This method differentiates between
	 * JAR files and directories and adjusts its behavior accordingly.
	 *
	 * @param classLoader     the class loader used to locate and retrieve classes
	 * @param packageName     the fully qualified package name
	 * @param recursive       whether the method should search recursively
	 * @param fileVisitorType determines whether the method should retrieve classes or packages
	 * @return a list of either classes or package names, depending on {@link FileVisitorType}
	 */
	@Nonnull
	private static <T> List<T> walkFileSystem(@Nonnull ClassLoader classLoader, @Nonnull String packageName, boolean recursive, @Nonnull FileVisitorType fileVisitorType) {
		String packagePath = packageName.replace('.', '/');
		URI uri = getUri(classLoader, packagePath);
		if (uri == null) {
			return new ArrayList<>();
		}

		// If the URI is a file, walk the directory.
		if (uri.getScheme().equals("file")) {
			Path startingPath = Paths.get(uri);
			return walkFileSystem(classLoader, packageName, recursive, fileVisitorType, startingPath);
		} else if (!uri.getScheme().equals("jar")) {
			throw new IllegalArgumentException("Unsupported URI scheme: " + uri.getScheme());
		}

		// If the URI is a JAR, walk the JAR.
		try (FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>())) {
			Path startingPath = fileSystem.getPath(packagePath);
			return walkFileSystem(classLoader, packageName, recursive, fileVisitorType, startingPath);
		} catch (IOException e) {
			throw new RuntimeException("Error creating the JAR file system.", e);
		}
	}

	/**
	 * Walks the file system from a starting path, retrieving either classes or packages
	 * based on the specified {@link FileVisitorType}.
	 *
	 * @param classLoader     the class loader used to locate and retrieve classes
	 * @param packageName     the fully qualified package name
	 * @param recursive       whether the method should search recursively
	 * @param fileVisitorType determines whether the method should retrieve classes or packages
	 * @param startingPath    the starting path from which to begin the search
	 * @return a list of either classes or package names, depending on {@link FileVisitorType}
	 */
	@Nonnull
	private static <T> List<T> walkFileSystem(@Nonnull ClassLoader classLoader, @Nonnull String packageName, boolean recursive, @Nonnull FileVisitorType fileVisitorType, @Nonnull Path startingPath) {
		@SuppressWarnings("unchecked")
		BaseFileVisitor<T> fileVisitor = switch (fileVisitorType) {
			case CLASS -> (BaseFileVisitor<T>) new ClassFileVisitor(startingPath, packageName, classLoader);
			case PACKAGE -> (BaseFileVisitor<T>) new PackageFileVisitor(startingPath, packageName);
		};

		try {
			Logger.debug("walkFileSystem() -> startingPath(%s), fileVisitorType(%s), recursive(%s)", startingPath, fileVisitorType, recursive);
			Files.walkFileTree(startingPath, Set.of(FileVisitOption.FOLLOW_LINKS), recursive ? Integer.MAX_VALUE : 2, fileVisitor);
			return fileVisitor.getList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves the URI of a resource based on its path, using the provided class loader.
	 * This method is useful for distinguishing resources located inside JAR files from those in directories.
	 *
	 * @param classLoader the class loader used to locate the resource
	 * @param path        the path of the resource to be located
	 * @return the URI of the resource
	 */
	@Nullable
	private static URI getUri(@Nonnull ClassLoader classLoader, @Nonnull String path) {
		URL resourceUrl = classLoader.getResource(path);
		if (resourceUrl == null) {
			return null;
		}

		try {
			return resourceUrl.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid URI syntax for resource path: " + path, e);
		}
	}

	private Introspector() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
