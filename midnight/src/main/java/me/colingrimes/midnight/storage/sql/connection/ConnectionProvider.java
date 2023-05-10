package me.colingrimes.midnight.storage.sql.connection;

import me.colingrimes.midnight.storage.sql.DatabaseType;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * A ConnectionProvider interface for providing SQL connections.
 */
public interface ConnectionProvider {

    /**
     * Gets the type of connection provider.
     *
     * @return the type of the provider
     */
    @Nonnull
    DatabaseType getType();

    /**
     * Initializes the connection provider.
     */
    void init() throws Exception;

    /**
     * Shuts down the connection provider and releases any resources.
     */
    void shutdown();

    /**
     * Gets a new SQL Connection.
     *
     * @return a new Connection instance
     * @throws SQLException if an error occurs while getting the connection
     */
    @Nonnull
    Connection getConnection() throws SQLException;

    /**
     * Gets the {@link DataSource} associated with the connection provider.
     *
     * @return the DataSource instance
     */
    @Nonnull
    DataSource getDataSource();

    /**
     * Returns a statement processor function which can modify statements.
     *
     * @return a function that processes statements
     */
    @Nonnull
    Function<String, String> getStatementProcessor();

    /**
     * Gets whether the connection provider has been initialized.
     *
     * @return true if the provider has been initialized
     */
    boolean isInitialized();
}
