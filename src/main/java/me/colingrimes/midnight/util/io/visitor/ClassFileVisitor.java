package me.colingrimes.midnight.util.io.visitor;

import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ClassFileVisitor extends BaseFileVisitor<Class<?>> {

    private ClassLoader classLoader;

    public ClassFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName, @Nullable ClassLoader classLoader) {
        super(startingPath, packageName);
        this.classLoader = classLoader;
    }

    @Nonnull
    @Override
    public FileVisitResult visitFile(@Nonnull Path file, @Nullable BasicFileAttributes attrs) {
        if (!file.toString().endsWith(".class") || file.toString().contains("$")) {
            return FileVisitResult.CONTINUE;
        }

        addClass(toQualifiedName(file));
        return FileVisitResult.CONTINUE;
    }

    /**
     * Adds the given class to the list of classes.
     * Uses the ClassLoader of the plugin to load the class to avoid incorrect dependency warnings.
     *
     * @param className the fully qualified class name
     */
    protected void addClass(@Nonnull String className) {
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
            Logger.warn("No class loader provided, attempting to use system class loader.");
        }

        try {
            getList().add(Class.forName(className, true, classLoader));
        } catch (ClassNotFoundException e) {
            Logger.severe("[Midnight] Class '" + className + "' could not be found:", e);
            throw new RuntimeException(e);
        }
    }
}
