package me.colingrimes.midnight.storage.sql.connection.file;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Function;

public class SqliteConnectionProvider implements ConnectionProvider {

	private final MidnightPlugin plugin;
	private final String databaseName;
	private File file;

	public SqliteConnectionProvider(@Nonnull MidnightPlugin plugin, @Nonnull String databaseName) {
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
		file = new File(plugin.getDataFolder(), databaseName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}

	@Override
	public void shutdown() {}

	@Nonnull
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + file.toString());
	}

	@Nonnull
	@Override
	public Function<String, String> getStatementProcessor() {
		return s -> s.replace('\'', '`');
	}
}
