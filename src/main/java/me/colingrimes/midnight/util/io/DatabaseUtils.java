package me.colingrimes.midnight.util.io;

import com.google.gson.JsonElement;
import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.storage.sql.DatabaseType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for database operations.
 */
public final class DatabaseUtils {

	/**
	 * Gets all queries for a specific {@link DatabaseType}.
	 *
	 * @param plugin the plugin to load the resource from
	 * @param type the database type to get the queries from
	 * @return the list of queries to run
	 */
	@Nonnull
	public static List<String> getQueries(@Nonnull Midnight plugin, @Nonnull DatabaseType type) {
		String path = "schema/" + type.getName().toLowerCase() + ".sql";
		URL url = plugin.getClass().getClassLoader().getResource(path);
		if (url == null) {
			throw new RuntimeException("Schema file not found (" + path + ")");
		}

		try (InputStream inputStream = url.openStream()) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				String queries = reader.lines().collect(Collectors.joining("\n"));
				return Arrays.stream(queries.split(";")).map(String::trim).filter(q -> !q.isEmpty()).toList();
			}
		} catch (IOException e) {
			Logger.severe(plugin, "DatabaseUtils has failed to load schema queries:", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets a UUID from a {@link ResultSet} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param rs the result set
	 * @param column the column name
	 * @return the UUID represented by the string or object
	 */
	@Nullable
	public static UUID getUUID(@Nonnull DatabaseType type, @Nonnull ResultSet rs, @Nonnull String column) throws SQLException {
		if (type == DatabaseType.POSTGRESQL) {
			return rs.getObject(column, UUID.class);
		} else {
			String uuid = rs.getString(column);
			return uuid != null ? UUID.fromString(uuid) : null;
		}
	}

	/**
	 * Sets a UUID in a {@link PreparedStatement} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param ps the statement
	 * @param index the parameter index
	 * @param uuid the uuid
	 */
	public static void setUUID(@Nonnull DatabaseType type, @Nonnull PreparedStatement ps, int index, @Nullable UUID uuid) throws SQLException {
		if (uuid == null) {
			setNullPostgres(type, ps, index);
		} else if (type == DatabaseType.POSTGRESQL) {
			ps.setObject(index, uuid);
		} else {
			ps.setString(index, uuid.toString());
		}
	}

	/**
	 * Gets a timestamp from a {@link ResultSet} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param rs the result set
	 * @param column the column name
	 * @return the timestamp represented by the string or object
	 */
	@Nullable
	public static Instant getTimestamp(@Nonnull DatabaseType type, @Nonnull ResultSet rs, @Nonnull String column) throws SQLException {
		if (type == DatabaseType.SQLITE) {
			String timestamp = rs.getString(column);
			return timestamp != null ? Instant.parse(timestamp) : null;
		} else {
			Timestamp timestamp = rs.getTimestamp(column);
			return timestamp != null ? timestamp.toInstant() : null;
		}
	}

	/**
	 * Sets a timestamp in a {@link PreparedStatement} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param ps the statement
	 * @param index the parameter index
	 * @param timestamp the timestamp
	 */
	public static void setTimestamp(@Nonnull DatabaseType type, @Nonnull PreparedStatement ps, int index, @Nullable Instant timestamp) throws SQLException {
		if (timestamp == null) {
			if (type == DatabaseType.SQLITE) {
				ps.setString(index, null);
			} else {
				ps.setNull(index, Types.TIMESTAMP);
			}
		} else if (type == DatabaseType.SQLITE) {
			ps.setString(index, timestamp.toString());
		} else {
			ps.setTimestamp(index, Timestamp.from(timestamp));
		}
	}

	/**
	 * Gets JSON from a {@link ResultSet} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param rs the result set
	 * @param column the column name
	 * @return the json represented by the string or object
	 */
	@Nullable
	public static JsonElement getJson(@Nonnull DatabaseType type, @Nonnull ResultSet rs, @Nonnull String column) throws SQLException {
		String json = rs.getString(column);
		return json != null ? Json.toElement(json) : null;
	}

	/**
	 * Gets the converted class that comes from the JSON in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param rs the result set
	 * @param column the column name
	 * @param clazz the class to convert the json to
	 * @return the json converted to the specified class
	 */
	@Nullable
	public static <T extends Serializable> T getJson(@Nonnull DatabaseType type, @Nonnull ResultSet rs, @Nonnull String column, @Nonnull Class<T> clazz) throws SQLException {
		JsonElement element = getJson(type, rs, column);
		return element != null ? Serializable.deserialize(clazz, element) : null;
	}

	/**
	 * Sets JSON in a {@link PreparedStatement} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param ps the statement
	 * @param index the parameter index
	 * @param element the json element
	 */
	public static void setJson(@Nonnull DatabaseType type, @Nonnull PreparedStatement ps, int index, @Nullable JsonElement element) throws SQLException {
		String json = (element == null || element.isJsonNull()) ? null : Json.toString(element);
		if (json == null) {
			setNullPostgres(type, ps, index);
		} else if (type == DatabaseType.POSTGRESQL) {
			ps.setObject(index, json, Types.OTHER);
		} else {
			ps.setString(index, json);
		}
	}

	/**
	 * Sets JSON in a {@link PreparedStatement} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param ps the statement
	 * @param index the parameter index
	 * @param serializable the serializable object
	 */
	public static void setJson(@Nonnull DatabaseType type, @Nonnull PreparedStatement ps, int index, @Nullable Serializable serializable) throws SQLException {
		setJson(type, ps, index, serializable == null ? null : serializable.serialize());
	}

	/**
	 * Sets a null value in a {@link PreparedStatement} in a way that works for all database types.
	 *
	 * @param type the database type
	 * @param ps the statement
	 * @param index the parameter index
	 */
	private static void setNullPostgres(@Nonnull DatabaseType type, @Nonnull PreparedStatement ps, int index) throws SQLException {
		if (type == DatabaseType.POSTGRESQL) {
			ps.setNull(index, Types.OTHER);
		} else {
			ps.setString(index, null);
		}
	}

	private DatabaseUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
