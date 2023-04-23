package me.colingrimes.midnight.storage.sql.connection;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * A ConnectionProvider interface for providing SQL connections.
 */
public interface ConnectionProvider {

    /**
     * Gets the name of the connection provider.
     * @return the name of the provider
     */
    @Nonnull
    String getName();

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
     * @return a new Connection instance
     * @throws SQLException if an error occurs while getting the connection
     */
    @Nonnull
    Connection getConnection() throws SQLException;

    /**
     * Returns a statement processor function which can modify statements, e.g., to add table prefixes.
     *
     * @return a function that processes statements
     */
    @Nonnull
    Function<String, String> getStatementProcessor();
}
