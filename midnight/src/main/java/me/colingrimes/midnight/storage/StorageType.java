package me.colingrimes.midnight.storage;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Represents various types of storage systems.
 */
public enum StorageType {
    YAML("YAML"),
    JSON("JSON"),
    MYSQL("MySQL"),
    SQLITE("SQLite");

    private final String name;

    StorageType(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Returns a StorageType enum by its name.
     * @param name the name of the storage type
     * @return the StorageType enum if it exists, otherwise an empty Optional
     */
    @Nonnull
    public static Optional<StorageType> fromString(@Nonnull String name) {
        for (StorageType storageType : StorageType.values()) {
            if (storageType.getName().equalsIgnoreCase(name)) {
                return Optional.of(storageType);
            }
        }
        return Optional.empty();
    }
}
