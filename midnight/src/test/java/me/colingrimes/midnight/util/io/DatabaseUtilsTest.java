package me.colingrimes.midnight.util.io;

import me.colingrimes.midnight.storage.sql.DatabaseType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseUtilsTest {

	@Mock private ResultSet resultSet;
	@Mock private PreparedStatement preparedStatement;

	@Test
	public void testGetTimestamp() throws SQLException {
		String stringTime = "2023-09-16T12:00:00Z";
		Timestamp timestamp = Timestamp.valueOf("2023-09-16 12:00:00");

		when(resultSet.getString("timestamp")).thenReturn(stringTime);
		when(resultSet.getTimestamp("timestamp")).thenReturn(timestamp);

		// SQLite Test
		Instant result1 = DatabaseUtils.getTimestamp(resultSet, "timestamp", DatabaseType.SQLITE);
		assertEquals(Instant.parse(stringTime), result1);

		// PostgreSQL Test
		Instant result2 = DatabaseUtils.getTimestamp(resultSet, "timestamp", DatabaseType.POSTGRESQL);
		assertEquals(timestamp.toInstant(), result2);
	}

	@Test
	public void testSetTimestamp() throws SQLException {
		Instant dateTime = Instant.now();

		// SQLite Test
		DatabaseUtils.setTimestamp(preparedStatement, 1, dateTime, DatabaseType.SQLITE);
		verify(preparedStatement, times(1)).setString(eq(1), anyString());

		// PostgreSQL Test
		DatabaseUtils.setTimestamp(preparedStatement, 1, dateTime, DatabaseType.POSTGRESQL);
		verify(preparedStatement, times(1)).setTimestamp(eq(1), any());
	}

	@Test
	public void testGetUUID() throws SQLException {
		UUID expectedUUID = UUID.randomUUID();

		// SQLite Test
		when(resultSet.getString("uuid")).thenReturn(expectedUUID.toString());
		UUID result1 = DatabaseUtils.getUUID(resultSet, "uuid", DatabaseType.SQLITE);
		assertEquals(expectedUUID, result1);

		// PostgreSQL Test
		when(resultSet.getObject("uuid")).thenReturn(expectedUUID);
		UUID result2 = DatabaseUtils.getUUID(resultSet, "uuid", DatabaseType.POSTGRESQL);
		assertEquals(expectedUUID, result2);
	}

	@Test
	public void testSetUUID() throws SQLException {
		UUID uuid = UUID.randomUUID();

		// SQLite Test
		DatabaseUtils.setUUID(preparedStatement, 1, uuid, DatabaseType.SQLITE);
		verify(preparedStatement, times(1)).setString(1, uuid.toString());

		// PostgreSQL Test
		DatabaseUtils.setUUID(preparedStatement, 1, uuid, DatabaseType.POSTGRESQL);
		verify(preparedStatement, times(1)).setObject(1, uuid);
	}

	@Test
	public void testNullsAreHandled() throws SQLException {
		// Setting nulls for SQLite.
		DatabaseUtils.setTimestamp(preparedStatement, 1, null, DatabaseType.SQLITE);
		DatabaseUtils.setUUID(preparedStatement, 1, null, DatabaseType.SQLITE);
		verify(preparedStatement, times(2)).setString(eq(1), eq(null));

		// Setting nulls for PostgreSQL.
		DatabaseUtils.setTimestamp(preparedStatement, 1, null, DatabaseType.POSTGRESQL);
		DatabaseUtils.setUUID(preparedStatement, 1, null, DatabaseType.POSTGRESQL);
		verify(preparedStatement, times(1)).setNull(eq(1), eq(java.sql.Types.TIMESTAMP));
		verify(preparedStatement, times(1)).setNull(eq(1), eq(java.sql.Types.OTHER));

		// Checking that nulls are handled in retrieval for SQLite.
		when(resultSet.getString(anyString())).thenReturn(null);
		assertNull(DatabaseUtils.getTimestamp(resultSet, "timestamp", DatabaseType.SQLITE));
		assertNull(DatabaseUtils.getUUID(resultSet, "uuid", DatabaseType.SQLITE));

		// Checking that nulls are handled in retrieval for PostgreSQL.
		when(resultSet.getTimestamp(anyString())).thenReturn(null);
		when(resultSet.getObject(anyString())).thenReturn(null);
		assertNull(DatabaseUtils.getTimestamp(resultSet, "timestamp", DatabaseType.POSTGRESQL));
		assertNull(DatabaseUtils.getUUID(resultSet, "uuid", DatabaseType.POSTGRESQL));
	}
}
