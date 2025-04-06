package me.colingrimes.midnight.storage.sql;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.io.DatabaseUtils;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Function;

/**
 * An abstract class representing an SQL-based storage.
 * @param <T> the type of data being stored
 */
public abstract class SqlStorage<T> implements Storage<T> {

    private final Midnight plugin;
    protected final ConnectionProvider provider;
    protected final DatabaseType database;
    protected final Function<String, String> processor;

    public SqlStorage(@Nonnull Midnight plugin, @Nonnull ConnectionProvider connectionProvider) {
        this.plugin = plugin;
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

        // Runs schema corresponding to the database type.
        try (Connection c = provider.getConnection()) {
            for (String query : DatabaseUtils.getQueries(plugin, provider.getType())) {
                try (PreparedStatement ps = c.prepareStatement(query)) {
                    ps.execute();
                }
            }
        }

        Logger.log(plugin, "Schema has been executed on the " + database.getName() + " database type.");
    }

    @Override
    public void shutdown() {
        provider.shutdown();
    }
}
