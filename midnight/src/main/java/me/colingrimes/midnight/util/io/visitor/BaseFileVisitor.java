package me.colingrimes.midnight.util.io.visitor;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFileVisitor<T> extends SimpleFileVisitor<Path> {

    private final List<T> list = new ArrayList<>();
    private final Path startingPath;
    private final String packageName;

    public BaseFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName) {
        if (!startingPath.toString().endsWith(packageName.replace(".", File.separator))) {
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
        if (!path.toString().startsWith(startingPath.toString())) {
            throw new IllegalArgumentException("Path " + path + " is not a child of " + startingPath);
        }

        String name = path.toString();
        name = name.substring(startingPath.toString().length() + 1);
        name = name.replace(".class", "");
        return packageName + "." + name.replace(File.separator, ".");
    }
}
