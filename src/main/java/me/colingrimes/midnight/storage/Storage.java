package me.colingrimes.midnight.storage;

import javax.annotation.Nonnull;

/**
 * A generic interface for different storage types. This interface allows for the
 * implementation of various storage methods, such as flat files or SQL databases,
 * while maintaining a consistent API for interacting with the storage.
 *
 * @param <T> the type of data to be stored
 */
public interface Storage<T> {

    /**
     * Initializes the storage.
     *
     * @throws Exception if there is an issue initializing the storage
     */
    void init() throws Exception;

    /**
     * Shuts down the storage, closing any resources as necessary.
     */
    void shutdown();

    /**
     * Saves the specified data to the storage.
     *
     * @param data the data to be saved
     * @throws Exception if there is an issue saving the data
     */
    void save(@Nonnull T data) throws Exception;

    /**
     * Deletes the specified data from the storage.
     *
     * @param data the data to be deleted
     * @throws Exception if there is an issue deleting the data
     */
    void delete(@Nonnull T data) throws Exception;
}
