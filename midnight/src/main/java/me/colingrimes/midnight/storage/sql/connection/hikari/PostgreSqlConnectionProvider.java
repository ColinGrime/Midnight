package me.colingrimes.midnight.storage.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import me.colingrimes.midnight.storage.sql.DatabaseCredentials;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class PostgreSqlConnectionProvider extends HikariConnectionProvider {

    public PostgreSqlConnectionProvider(@Nonnull DatabaseCredentials credentials) {
        super(credentials);
    }

    @Nonnull
    @Override
    public String getName() {
        return "PostgreSQL";
    }

    @Nonnull
    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace('\'', '"');
    }

    @Override
    protected void configureDatabase(@Nonnull HikariConfig config, @Nonnull DatabaseCredentials credentials) {
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabase());
        config.setUsername(credentials.getUser());
        config.setPassword(credentials.getPassword());
    }
}
