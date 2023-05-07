package me.colingrimes.midnight.storage.sql.connection.file;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import org.sqlite.SQLiteDataSource;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class SqliteConnectionProvider implements ConnectionProvider {

	private final Midnight plugin;
	private final String databaseName;
	private SQLiteDataSource dataSource;
	private boolean initialized = false;

	public SqliteConnectionProvider(@Nonnull Midnight plugin, @Nonnull String databaseName) {
		this.plugin = plugin;
		this.databaseName = databaseName.endsWith(".db") ? databaseName : databaseName + ".db";
	}

	@Nonnull
	@Override
	public String getName() {
		return "SQLite";
	}

	@Override
	public void init() throws IOException {
		if (initialized) {
			return;
		} else {
			initialized = true;
		}

		File file = new File(plugin.getDataFolder(), databaseName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:" + file);
	}

	@Override
	public void shutdown() {}

	@Nonnull
	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Nonnull
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Nonnull
	@Override
	public Function<String, String> getStatementProcessor() {
		return s -> s.replace('\'', '`');
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
