package me.colingrimes.midnight.storage.file.composite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A composite identifier that uses a file name and an internal path to identify a piece of data.
 * <p>
 * If the {@code fileName} is {@code null}, the data will not be able to load and will throw an exception.
 * <p>
 * If the {@code internalPath} is {@code null}, the data will be loaded from the root of the file.
 */
public class Identifier {

    private String fileName;
    private String internalPath;

    /**
     * Creates a new composite identifier.
     * @return the composite identifier
     */
    public static Identifier create() {
        return new Identifier(null, null);
    }

    /**
     * Creates a new composite identifier with the given file path.
     *
     * @param fileName the file name
     * @return the composite identifier
     */
    @Nonnull
    public static Identifier fileName(@Nullable String fileName) {
        return new Identifier(fileName, null);
    }

    /**
     * Creates a new composite identifier with the given internal path.
     *
     * @param internalPath the internal path
     * @return the composite identifier
     */
    public static Identifier internalPath(@Nullable String internalPath) {
        return new Identifier(null, internalPath);
    }

    /**
     * Creates a new composite identifier with the given file path and internal path.
     *
     * @param fileName the file name
     * @param internalPath the internal path
     * @return the composite identifier
     */
    @Nonnull
    public static Identifier of(@Nullable String fileName, @Nullable String internalPath) {
        return new Identifier(fileName, internalPath);
    }

   private Identifier(@Nullable String filePath, @Nullable String internalPath) {
        this.fileName = filePath;
        this.internalPath = internalPath;
    }

    /**
     * Gets the name of the file.
     * @return the file name
     */
    @Nullable
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the name of the file.
     * @param fileName the file name
     */
    public void setFileName(@Nullable String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the internal path.
     * @return the internal path
     */
    @Nonnull
    public String getInternalPath() {
        return internalPath == null ? "" : internalPath;
    }

    /**
     * Sets the internal path.
     * @param internalPath the internal path
     */
    public void setInternalPath(@Nullable String internalPath) {
        this.internalPath = internalPath;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(internalPath, that.internalPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, internalPath);
    }
}
