package me.colingrimes.midnight.util.io.visitor;

import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class PackageFileVisitor extends BaseFileVisitor<String> {

    private final Path startingPath;

    public PackageFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName) {
        super(startingPath, packageName);
        this.startingPath = startingPath;
    }

    @Nonnull
    @Override
    public FileVisitResult preVisitDirectory(@Nonnull Path dir, @Nullable BasicFileAttributes attrs) {
        Logger.debug("Package found: %s", dir.toString());
        if (startingPath.equals(dir)) {
            return FileVisitResult.CONTINUE;
        }

        getList().add(toQualifiedName(dir));
        return FileVisitResult.CONTINUE;
    }
}
