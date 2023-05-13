package me.colingrimes.midnight.storage.sql.connection.file;

import me.colingrimes.midnight.Midnight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqliteConnectionProviderTest {

	private Midnight plugin;
	private SqliteConnectionProvider provider;

	@BeforeEach
	public void setUp(@TempDir Path tempDir) {
		plugin = mock(Midnight.class);
		when(plugin.getDataFolder()).thenReturn(tempDir.toFile());
		provider = new SqliteConnectionProvider(plugin, "test_database");
	}

	@Test
	public void testName() {
		assertEquals("SQLite", provider.getType().getName(), "SqliteConnectionProvider should return 'SQLite' as its name");
	}

	@Test
	public void testStatementProcessor() {
		String input = "CREATE TABLE 'test_table' ('id' INT PRIMARY KEY)";
		String expectedOutput = "CREATE TABLE `test_table` (`id` INT PRIMARY KEY)";
		String actualOutput = provider.getStatementProcessor().apply(input);
		assertEquals(expectedOutput, actualOutput, "SqliteConnectionProvider statement processor should replace single quotes with backticks");
	}

	@Test
	public void testInit() throws Exception {
		assertFalse(new File(plugin.getDataFolder(), "test_database.db").exists(), "Database file should not exist before initialization");
		provider.init();
		assertTrue(new File(plugin.getDataFolder(), "test_database.db").exists(), "Database file should be created after initialization");
	}

	@Test
	public void testGetConnection() throws Exception {
		provider.init();
		try (Connection connection = provider.getConnection()) {
			assertNotNull(connection, "Connection should not be null");
			assertFalse(connection.isClosed(), "Connection should not be closed");
		} catch (SQLException e) {
			fail("Should not throw SQLException when getting a connection");
		}
	}
}
