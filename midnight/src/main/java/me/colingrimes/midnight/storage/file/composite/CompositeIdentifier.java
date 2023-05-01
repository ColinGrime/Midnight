package me.colingrimes.midnight.storage.file.composite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class CompositeIdentifier {

    private final String filePath;
    private final String internalPath;

    public CompositeIdentifier(@Nonnull String filePath, @Nullable String internalPath) {
        this.filePath = filePath;
        this.internalPath = internalPath;
    }

    /**
     * Gets the file path.
     * @return the file path
     */
    @Nonnull
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gets the internal path.
     * @return the internal path
     */
    @Nullable
    public String getInternalPath() {
        return internalPath;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeIdentifier that = (CompositeIdentifier) o;
        return Objects.equals(filePath, that.filePath) && Objects.equals(internalPath, that.internalPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, internalPath);
    }
}
