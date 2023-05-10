package me.colingrimes.midnight.storage.sql.connection;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.storage.sql.DatabaseCredentials;
import me.colingrimes.midnight.storage.sql.connection.file.SqliteConnectionProvider;
import me.colingrimes.midnight.storage.sql.connection.hikari.MySqlConnectionProvider;
import me.colingrimes.midnight.storage.sql.connection.hikari.PostgreSqlConnectionProvider;

import javax.annotation.Nonnull;

public class ConnectionFactory {

	private final Midnight plugin;

	public ConnectionFactory(@Nonnull Midnight plugin) {
		this.plugin = plugin;
	}

	/**
	 * Creates a new connection provider based on the given credentials.
	 *
	 * @param credentials the credentials to use
	 * @return the connection provider
	 */
	@Nonnull
	public ConnectionProvider createConnection(@Nonnull DatabaseCredentials credentials) {
		return switch (credentials.getType()) {
			case POSTGRESQL -> new PostgreSqlConnectionProvider(credentials);
			case MYSQL -> new MySqlConnectionProvider(credentials);
			case SQLITE -> new SqliteConnectionProvider(plugin, credentials.getDatabase());
		};
	}
}