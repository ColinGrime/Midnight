package me.colingrimes.midnight.storage.sql.connection.file;

import me.colingrimes.midnight.MockSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SqliteConnectionProviderTest extends MockSetup {

	private SqliteConnectionProvider provider;

	@BeforeEach
	void setUp() {
		provider = new SqliteConnectionProvider(plugin, "test_database");
	}

	@Test
	void testGetType() {
		assertEquals("SQLite", provider.getType().getName());
	}

	@Test
	void testInit() throws Exception {
		assertFalse(new File(plugin.getDataFolder(), "test_database.db").exists());
		provider.init();
		assertTrue(new File(plugin.getDataFolder(), "test_database.db").exists());
	}

	@Test
	void testGetConnection() throws Exception {
		provider.init();

		try (Connection connection = provider.getConnection()) {
			assertNotNull(connection);
			assertFalse(connection.isClosed());
		} catch (SQLException e) {
			fail("Should not throw SQLException when getting a connection.");
		}
	}

	@Test
	void testStatementProcessor() {
		String input = "CREATE TABLE 'test_table' ('id' INT PRIMARY KEY)";
		String expectedOutput = input.replace('\'', '`');
		assertEquals(expectedOutput, provider.getStatementProcessor().apply(input));
	}
}
