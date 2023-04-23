package me.colingrimes.midnight.storage.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import me.colingrimes.midnight.storage.sql.connection.DatabaseCredentials;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class MySqlConnectionProvider extends HikariConnectionProvider {

    public MySqlConnectionProvider(@Nonnull DatabaseCredentials credentials) {
        super(credentials);
    }

    @Nonnull
    @Override
    public String getName() {
        return "MySQL";
    }

    @Nonnull
    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace('\'', '`');
    }

    @Override
    protected void configureDatabase(@Nonnull HikariConfig config, @Nonnull DatabaseCredentials credentials) {
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabase());
        config.setUsername(credentials.getUser());
        config.setPassword(credentials.getPassword());
    }
}
