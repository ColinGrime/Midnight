package me.colingrimes.midnight.util.io.visitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class PackageFileVisitor extends BaseFileVisitor<String> {

    public PackageFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName) {
        super(startingPath, packageName);
    }

    @Nonnull
    @Override
    public FileVisitResult preVisitDirectory(@Nonnull Path dir, @Nullable BasicFileAttributes attrs) {
        getList().add(toQualifiedName(dir));
        return FileVisitResult.CONTINUE;
    }
}
