package me.colingrimes.midnight.storage.sql;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * An abstract class representing an SQL-based storage.
 * @param <T> the type of data being stored
 */
public abstract class SqlStorage<T> implements Storage<T> {

    protected final MidnightPlugin plugin;
    protected final ConnectionProvider connectionProvider;

    public SqlStorage(@Nonnull MidnightPlugin plugin, @Nonnull ConnectionProvider connectionProvider) {
        this.plugin = plugin;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void init() throws Exception {
        connectionProvider.init();

        Optional<String> identifier = getIdentifier(null);
        if (identifier.isPresent()) {
            if (!tableExists(identifier.get())) {
                applySchema(identifier.get());
            }
        } else {
            throw new Exception("Identifier not present");
        }
    }

    @Override
    public void shutdown() {
        connectionProvider.shutdown();
    }

    /**
     * Checks whether a table with the given name exists in the connected database.
     * The comparison is case-insensitive.
     *
     * @param tableName the name of the table to check for existence
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean tableExists(@Nonnull String tableName) throws SQLException {
        try (Connection connection = connectionProvider.getConnection()) {
            try (ResultSet resultSet = connection.getMetaData().getTables(connection.getCatalog(), null, tableName, null)) {
                while (resultSet.next()) {
                    if (resultSet.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /**
     * Applies the schema to the connected database.
     * Reads an SQL schema file specific to the type of database being used
     * and executes the queries to create the tables.
     *
     * @param schemaName the name of the schema file to read
     * @throws IOException if there's an error reading the schema file
     * @throws SQLException if a database access error occurs
     */
    private void applySchema(@Nonnull String schemaName) throws IOException, SQLException {
        List<String> queries;
        String path = "/schema/" + connectionProvider.getName().toLowerCase() + "/" + schemaName + ".sql";

        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Schema file not found (" + path + ")");
            }
            queries = SchemaReader.getQueries(inputStream);
        }

        try (Connection connection = connectionProvider.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                for (String query : queries) {
                    statement.addBatch(query);
                }
                statement.executeBatch();
            }
        }
    }
}
