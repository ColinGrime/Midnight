package me.colingrimes.midnight.storage.sql;

import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import org.flywaydb.core.Flyway;

import javax.annotation.Nonnull;

/**
 * An abstract class representing an SQL-based storage.
 * @param <T> the type of data being stored
 */
public abstract class SqlStorage<T> implements Storage<T> {

    protected final ConnectionProvider connectionProvider;

    public SqlStorage(@Nonnull ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void init() throws Exception {
        if (connectionProvider.isInitialized()) {
            return;
        } else {
            connectionProvider.init();
        }

        // Configure and run Flyway migrations.
        Flyway.configure()
                .dataSource(connectionProvider.getDataSource())
                .locations("classpath:/migrations/" + connectionProvider.getName().toLowerCase())
                .load()
                .migrate();
    }

    @Override
    public void shutdown() {
        connectionProvider.shutdown();
    }
}
