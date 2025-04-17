package me.colingrimes.midnight.storage.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.colingrimes.midnight.storage.database.DatabaseCredentials;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariConnectionProvider implements ConnectionProvider {

    private final DatabaseCredentials credentials;
    private HikariDataSource hikari;
    private boolean initialized = false;

    public HikariConnectionProvider(@Nonnull DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Configure the database-specific settings for the HikariConfig.
     *
     * @param config the {@link HikariConfig} object to be configured
     * @param credentials the StorageCredentials containing the required database settings
     */
    protected abstract void configureDatabase(@Nonnull HikariConfig config, @Nonnull DatabaseCredentials credentials);

    @Override
    public void init() {
        if (initialized) {
            return;
        } else {
            initialized = true;
        }

        HikariConfig config = new HikariConfig();
        configureDatabase(config, credentials);

        // Recommended HikariCP settings for improved performance.
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        // Configure the connection pool settings.
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);

        hikari = new HikariDataSource(config);
    }

    @Override
    public void shutdown() {
        if (hikari != null) {
            hikari.close();
        }
    }

    @Nonnull
    @Override
    public Connection getConnection() throws SQLException {
        if (hikari == null) {
            throw new SQLException("Connection has failed, hikari is null.");
        }

        Connection connection = hikari.getConnection();
        if (connection == null) {
            throw new SQLException("Connection has failed, connection is null.");
        }

        return connection;
    }

    @Nonnull
    @Override
    public DataSource getDataSource() {
        return hikari;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
}
