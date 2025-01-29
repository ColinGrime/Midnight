package me.colingrimes.midnight.storage.sql.connection;

import me.colingrimes.midnight.storage.sql.DatabaseCredentials;
import me.colingrimes.midnight.storage.sql.DatabaseType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatabaseCredentialsTest {

	private ConfigurationSection config;

	@BeforeEach
	void setUp() {
		String yamlConfig =
				"""
                host: example.com
                port: 1234
                database: test_db
                username: test_user
                password: test_password
                """;

		config = YamlConfiguration.loadConfiguration(new StringReader(yamlConfig));
	}

	@Test
	void testOf() {
		DatabaseCredentials credentials = DatabaseCredentials.of(
				DatabaseType.MYSQL,
				"example.com",
				1234,
				"test_db",
				"test_user",
				"test_password"
		);

		assertNotNull(credentials);
		assertEquals("example.com", credentials.getHost());
		assertEquals(1234, credentials.getPort());
		assertEquals("test_db", credentials.getDatabase());
		assertEquals("test_user", credentials.getUser());
		assertEquals("test_password", credentials.getPassword());
	}

	@Test
	void testFromConfig() {
		DatabaseCredentials credentials = DatabaseCredentials.fromConfig(config);
		assertNotNull(credentials);
		assertEquals("example.com", credentials.getHost());
		assertEquals(1234, credentials.getPort());
		assertEquals("test_db", credentials.getDatabase());
		assertEquals("test_user", credentials.getUser());
		assertEquals("test_password", credentials.getPassword());
	}

	@Test
	void testToString() {
		DatabaseCredentials credentials = DatabaseCredentials.of(
				DatabaseType.MYSQL,
				"example.com",
				1234,
				"test_db",
				"test_user",
				"test_password"
		);

		String expectedString = "StorageCredentials{" +
				"host='example.com'" +
				", " +
				"port=1234" +
				", " +
				"database='test_db'" +
				", " +
				"user='test_user'" +
				", " +
				"password='test_password'" +
		"}";

		assertEquals(expectedString, credentials.toString());
	}
}
