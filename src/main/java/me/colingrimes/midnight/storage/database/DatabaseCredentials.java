package me.colingrimes.midnight.storage.database;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * DatabaseCredentials is a simple class that holds the necessary information for connecting to a database.
 */
public class DatabaseCredentials {

    private final DatabaseType type;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    /**
     * Creates a new instance of {@link DatabaseCredentials} with the given connection information.
     *
     * @param type     the type of database to connect to
     * @param host     the host address of the database
     * @param port     the port number of the database
     * @param database the name of the database to connect to
     * @param user     the username to use for authentication
     * @param password the password to use for authentication
     * @return the database credentials
     */
    @Nonnull
    public static DatabaseCredentials of(@Nonnull DatabaseType type, @Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
        return new DatabaseCredentials(type, host, port, database, user, password);
    }

    /**
     * Creates a new instance of {@link DatabaseCredentials} with the given connection information.
     *
     * @param config the configuration section to read from
     * @return the database credentials
     */
    @Nullable
    public static DatabaseCredentials fromConfig(@Nullable ConfigurationSection config) {
        if (config == null) {
            return null;
        }

        return of(
                DatabaseType.fromString(config.getString("type")),
                config.getString("host", "localhost"),
                config.getInt("port", 3306),
                config.getString("database", "minecraft"),
                config.getString("username", "root"),
                config.getString("password", "passw0rd")
        );
    }

    private DatabaseCredentials(@Nonnull DatabaseType type, @Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    /**
     * Gets the type of database to connect to.
     *
     * @return the database type
     */
    @Nonnull
    public DatabaseType getType() {
        return type;
    }

    /**
     * Gets the host address of the database.
     *
     * @return the host address
     */
    @Nonnull
    public String getHost() {
        return host;
    }

    /**
     * Gets the port number of the database.
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the name of the database to connect to.
     *
     * @return the database name
     */
    @Nonnull
    public String getDatabase() {
        return database;
    }

    /**
     * Gets the username to use for authentication.
     *
     * @return the username
     */
    @Nonnull
    public String getUser() {
        return user;
    }

    /**
     * Gets the password to use for authentication.
     *
     * @return the password
     */
    @Nonnull
    public String getPassword() {
        return password;
    }
}
