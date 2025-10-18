package me.colingrimes.midnight.util.io.visitor;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class BaseFileVisitor<T> extends SimpleFileVisitor<Path> {

    private final List<T> list = new ArrayList<>();
    private final Path startingPath;
    private final String packageName;

    public BaseFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName) {
        String normalizedPath = startingPath.toString().replace("\\", "/");
        String normalizedPackage = packageName.replace(".", "/");
        if (!normalizedPath.endsWith(normalizedPackage)) {
            throw new IllegalArgumentException("Path " + startingPath + " does not end with " + packageName);
        }

        this.startingPath = startingPath;
        this.packageName = packageName;
    }

    /**
     * Gets the list of items found by the visitor.
     *
     * @return the list of items
     */
    @Nonnull
    public List<T> getList() {
        return list;
    }

    /**
     * Converts a file system path to a fully qualified Java class or package name.
     * <p>
     * This method processes the path, removes any file extension related to class files,
     * and formats it to match Java's naming convention for classes and packages.
     * </p>
     *
     * @param path the file system path representing a class or a package
     * @return the fully qualified Java class or package name
     */
    @Nonnull
    String toQualifiedName(@Nonnull Path path) {
        if (!path.normalize().toUri().toString().startsWith(startingPath.normalize().toUri().toString())) {
            throw new IllegalArgumentException("Path " + path + " is not a child of " + startingPath);
        }

        String name = path.toString();
        int startLength = startingPath.toString().length();
        if (startLength < name.length() && (name.charAt(startLength) == '/' || name.charAt(startLength) == '\\')) {
            startLength++;
        }
        
        name = name.substring(startLength);
        name = name.replace(".class", "");
        name = name.replace("\\", ".").replace("/", ".");
        return packageName + "." + name;
    }
}
