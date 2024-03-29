package me.colingrimes.midnight.storage.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents various types of database systems.
 */
public enum DatabaseType {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    SQLITE("SQLite");

    private final String name;

    DatabaseType(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Returns a StorageType enum by its name.
     *
     * @param name the name of the storage type
     * @return the StorageType enum if it exists, or SQLite
     */
    @Nonnull
    public static DatabaseType fromString(@Nullable String name) {
        for (DatabaseType storageType : DatabaseType.values()) {
            if (storageType.getName().equalsIgnoreCase(name)) {
                return storageType;
            }
        }
        return SQLITE;
    }
}
