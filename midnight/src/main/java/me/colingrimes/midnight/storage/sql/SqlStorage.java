package me.colingrimes.midnight.storage.sql;

import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import org.flywaydb.core.Flyway;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * An abstract class representing an SQL-based storage.
 * @param <T> the type of data being stored
 */
public abstract class SqlStorage<T> implements Storage<T> {

    protected final ConnectionProvider provider;
    protected final DatabaseType database;
    protected final Function<String, String> processor;

    public SqlStorage(@Nonnull ConnectionProvider connectionProvider) {
        this.provider = connectionProvider;
        this.database = connectionProvider.getType();
        this.processor = connectionProvider.getStatementProcessor();
    }

    @Override
    public void init() throws Exception {
        if (provider.isInitialized()) {
            return;
        } else {
            provider.init();
        }

        // Configure and run Flyway migrations.
        Flyway.configure(getClass().getClassLoader())
                .dataSource(provider.getDataSource())
                .locations("classpath:/migrations/" + provider.getType().getName().toLowerCase())
                .load()
                .migrate();
    }

    @Override
    public void shutdown() {
        provider.shutdown();
    }
}
