package me.colingrimes.midnight.util.io;

import me.colingrimes.midnight.storage.sql.DatabaseType;

import java.sql.*;
import java.time.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for database operations.
 */
public final class DatabaseUtils {

	/**
	 * Set a timestamp in a PreparedStatement in a way that works for all database types.
	 *
	 * @param ps             the preparedStatement in which to set the timestamp
	 * @param parameterIndex the index in the PreparedStatement at which to set the timestamp
	 * @param dateTime       the ZonedDateTime to set, may be null
	 * @param type           the type of the database
	 * @throws SQLException if a database access error occurs.
	 */
	public static void setTimestamp(@Nonnull PreparedStatement ps, int parameterIndex, @Nullable ZonedDateTime dateTime, @Nonnull DatabaseType type) throws SQLException {
		if (dateTime == null) {
			ps.setString(parameterIndex, null);
		} else if (type == DatabaseType.SQLITE) {
			ps.setString(parameterIndex, dateTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toString());
		} else {
			ps.setTimestamp(parameterIndex, Timestamp.from(dateTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant()));
		}
	}

	/**
	 * Get a timestamp from a ResultSet in a way that works for all database types.
	 *
	 * @param resultSet  the ResultSet from which to get the timestamp
	 * @param columnName the name of the column from which to get the timestamp
	 * @param type       the type of the database
	 * @return the ZonedDateTime represented by the timestamp, may be null
	 * @throws SQLException if a database access error occurs.
	 */
	@Nullable
	public static ZonedDateTime getTimestamp(@Nonnull ResultSet resultSet, @Nonnull String columnName, @Nonnull DatabaseType type) throws SQLException {
		if (type == DatabaseType.SQLITE) {
			String dateTimeStr = resultSet.getString(columnName);
			return dateTimeStr != null ? ZonedDateTime.parse(dateTimeStr).withZoneSameInstant(ZoneId.systemDefault()) : null;
		} else {
			Timestamp timestamp = resultSet.getTimestamp(columnName);
			return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
		}
	}

	private DatabaseUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
