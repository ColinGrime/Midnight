package me.colingrimes.midnight.storage;

import me.colingrimes.midnight.serialize.Serializable;

import javax.annotation.Nonnull;

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
     * @throws Exception if there is an issue initializing the storage
     */
    void init() throws Exception;

    /**
     * Shuts down the storage, closing any resources as necessary.
     */
    void shutdown();

    /**
     * Loads all data from the storage.
     * @throws Exception if there is an issue loading the data
     */
    void loadAll() throws Exception;

    /**
     * Loads the data with the specified identifier from the storage.
     * @param identifier the identifier of the data to be loaded
     * @throws Exception if there is an issue loading the data
     */
    void load(@Nonnull String identifier) throws Exception;

    /**
     * Saves the specified data to the storage.
     * @param data the data to be saved
     * @throws Exception if there is an issue saving the data
     */
    void save(@Nonnull T data) throws Exception;

    /**
     * Deletes the specified data from the storage.
     * @param data the data to be deleted
     * @throws Exception if there is an issue deleting the data
     */
    void delete(@Nonnull T data) throws Exception;
}
