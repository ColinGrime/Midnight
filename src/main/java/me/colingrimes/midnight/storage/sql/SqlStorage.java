package me.colingrimes.midnight.storage.sql;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.database.DatabaseType;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.storage.database.DatabaseUtils;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * An abstract class representing an SQL-based storage.
 * @param <T> the type of data being stored
 */
public abstract class SqlStorage<T> implements Storage<T> {

    private final Midnight plugin;
    private final String versionTable;
    protected final ConnectionProvider provider;
    protected final DatabaseType type;
    protected final Function<String, String> processor;

    public SqlStorage(@Nonnull Midnight plugin, @Nonnull ConnectionProvider connectionProvider) {
        this.plugin = plugin;
        this.versionTable = plugin.getName().toLowerCase() + "_version";
        this.provider = connectionProvider;
        this.type = connectionProvider.getType();
        this.processor = connectionProvider.getStatementProcessor();
    }

    @Override
    public final void init() throws Exception {
        if (provider.isInitialized()) {
            // Runs any migrations the storage might have.
            try (Connection c = provider.getConnection()) {
                migrate(c, getVersion(c));
            }
            return;
        }

        provider.init();

        // Runs schema corresponding to the database type.
        try (Connection c = provider.getConnection()) {
            for (String query : DatabaseUtils.getQueries(plugin, provider.getType())) {
                try (PreparedStatement ps = c.prepareStatement(query)) {
                    ps.execute();
                }
            }

            Logger.log(plugin, "Schema has been executed on the " + type.getName() + " database type.");
            migrate(c, getVersion(c));
        }
    }

    @Override
    public final void shutdown() {
        provider.shutdown();
    }

    /**
     * Runs optional migration logic after the {@link Storage#init()} method is called.
     *
     * @param connection the database connection
     * @param version the current database version of the plugin
     */
    protected void migrate(@Nonnull Connection connection, int version) throws SQLException {}

    /**
     * Gets the current database version of the plugin.
     *
     * @param connection the database connection
     * @return the database version
     */
    private int getVersion(@Nonnull Connection connection) throws SQLException {
        String versionCreate = String.format("CREATE TABLE IF NOT EXISTS '%s' (version INTEGER)", versionTable);
        String versionSelect = String.format("SELECT version FROM '%s' LIMIT 1", versionTable);

        // Creates the version table.
        try (PreparedStatement ps = prepare(connection, versionCreate)) {
            ps.execute();
        }

        // Gets the version number.
        try (PreparedStatement ps = prepare(connection, versionSelect)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("version");
                }
            }
        }

        return 1;
    }

    /**
     * Sets the current database version of the plugin.
     *
     * @param connection the database connection
     * @param version the new database version
     * @return the new version
     */
    protected int setVersion(@Nonnull Connection connection, int version) throws SQLException {
        String versionDelete = String.format("DELETE FROM '%s'", versionTable);
        String versionSet = String.format("INSERT INTO '%s' (version) VALUES (?)", versionTable);

        // Deletes the old version.
        try (PreparedStatement ps = prepare(connection, versionDelete)) {
            ps.executeUpdate();
        }

        // Sets the new version.
        try (PreparedStatement ps = prepare(connection, versionSet)) {
            ps.setInt(1, version);
            ps.executeUpdate();
        }

        return version;
    }

    /**
     * Creates a {@link PreparedStatement} from the given query.
     * <p>
     * Uses the {@link ConnectionProvider#getStatementProcessor()} to convert each statement to the correct format for its database type.
     *
     * @param connection the connection
     * @param query the query
     * @return the prepared statement
     */
    @Nonnull
    protected PreparedStatement prepare(@Nonnull Connection connection, @Nonnull String query) throws SQLException {
        return connection.prepareStatement(processor.apply(query));
    }
}
