package me.colingrimes.midnight.storage.sql.connection;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;

/**
 * StorageCredentials is a simple class that holds the necessary information
 * for connecting to a data storage system, such as a database.
 */
public class DatabaseCredentials {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    /**
     * Creates a new instance of StorageCredentials with the given connection information.
     * @param host     the host address of the storage system
     * @param port     the port number of the storage system
     * @param database the name of the database to connect to
     * @param user     the username to use for authentication
     * @param password the password to use for authentication
     * @return a new instance of StorageCredentials
     */
    @Nonnull
    public static DatabaseCredentials of(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
        return new DatabaseCredentials(host, port, database, user, password);
    }

    /**
     * Creates a new instance of StorageCredentials with the given connection information.
     * @param config the configuration section to read from
     * @return a new instance of StorageCredentials
     */
    @Nonnull
    public static DatabaseCredentials fromConfig(@Nonnull ConfigurationSection config) {
        return of(
                config.getString("address", "localhost"),
                config.getInt("port", 3306),
                config.getString("database", "minecraft"),
                config.getString("username", "root"),
                config.getString("password", "passw0rd")
        );
    }

    private DatabaseCredentials(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    /**
     * Gets the host address of the storage system.
     * @return the host address
     */
    @Nonnull
    public String getHost() {
        return host;
    }

    /**
     * Gets the port number of the storage system.
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the name of the database to connect to.
     * @return the database name
     */
    @Nonnull
    public String getDatabase() {
        return database;
    }

    /**
     * Gets the username to use for authentication.
     * @return the username
     */
    @Nonnull
    public String getUser() {
        return user;
    }

    /**
     * Gets the password to use for authentication.
     * @return the password
     */
    @Nonnull
    public String getPassword() {
        return password;
    }

    /**
     * Returns a string representation of the StorageCredentials object.
     * @return a string representation of this object
     */
    @Nonnull
    @Override
    public String toString() {
        return "StorageCredentials{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
