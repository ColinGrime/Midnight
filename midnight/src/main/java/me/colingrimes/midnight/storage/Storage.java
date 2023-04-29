package me.colingrimes.midnight.storage;

import me.colingrimes.midnight.serialize.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A generic interface for different storage types. This interface allows for the
 * implementation of various storage methods, such as flat files or SQL databases,
 * while maintaining a consistent API for interacting with the storage.
 *
 * @param <T> the type of data to be stored
 */
public interface Storage<T extends Serializable> {

    /**
     * Initializes the storage.
     */
    void init() throws Exception;

    /**
     * Shuts down the storage, closing any resources as necessary.
     */
    void shutdown();

    /**
     * Loads data from the storage.
     */
    void load() throws Exception;

    /**
     * Saves the specified data to the storage.
     * @param data the data to be saved
     */
    void save(@Nonnull T data) throws Exception;

    /**
     * Deletes the specified data from the storage.
     * @param data the data to be deleted
     */
    void delete(@Nonnull T data) throws Exception;

    /**
     * Returns an identifier specific to the storage implementation.
     * For flat-file storage, this could be a file path, while for SQL storage, this
     * could be a table name.
     *
     * <p>If the identifier is constant, this method will return a non-empty Optional containing the identifier.
     * If the identifier is not constant, this method will return an empty Optional.</p>
     *
     * @param data the data for which the identifier is required
     * @return the identifier specific to the storage implementation
     */
    @Nonnull
    Optional<String> getIdentifier(@Nullable T data);
}
