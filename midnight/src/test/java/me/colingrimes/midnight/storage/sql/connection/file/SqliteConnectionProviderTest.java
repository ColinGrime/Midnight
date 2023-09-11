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
	public void testGetType() {
		assertEquals("SQLite", provider.getType().getName());
	}

	@Test
	public void testInit() throws Exception {
		assertFalse(new File(plugin.getDataFolder(), "test_database.db").exists());
		provider.init();
		assertTrue(new File(plugin.getDataFolder(), "test_database.db").exists());
	}

	@Test
	public void testGetConnection() throws Exception {
		provider.init();

		try (Connection connection = provider.getConnection()) {
			assertNotNull(connection);
			assertFalse(connection.isClosed());
		} catch (SQLException e) {
			fail("Should not throw SQLException when getting a connection.");
		}
	}

	@Test
	public void testStatementProcessor() {
		String input = "CREATE TABLE 'test_table' ('id' INT PRIMARY KEY)";
		String expectedOutput = input.replace('\'', '`');
		assertEquals(expectedOutput, provider.getStatementProcessor().apply(input));
	}
}
