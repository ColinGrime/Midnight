package me.colingrimes.midnight.storage.sql.connection;

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
	public void setUp() {
		String yamlConfig =
    			"""
				address: example.com
				port: 1234
				database: test_db
				username: test_user
				password: test_password
				""";

		config = YamlConfiguration.loadConfiguration(new StringReader(yamlConfig));
	}

	@Test
	public void testOf() {
		DatabaseCredentials credentials = DatabaseCredentials.of("example.com", 1234, "test_db", "test_user", "test_password");

		assertNotNull(credentials, "DatabaseCredentials object should not be null");
		assertEquals("example.com", credentials.getHost(), "Host should be 'example.com'");
		assertEquals(1234, credentials.getPort(), "Port should be 1234");
		assertEquals("test_db", credentials.getDatabase(), "Database should be 'test_db'");
		assertEquals("test_user", credentials.getUser(), "User should be 'test_user'");
		assertEquals("test_password", credentials.getPassword(), "Password should be 'test_password'");
	}

	@Test
	public void testFromConfig() {
		DatabaseCredentials credentials = DatabaseCredentials.fromConfig(config);

		assertNotNull(credentials, "DatabaseCredentials object should not be null");
		assertEquals("example.com", credentials.getHost(), "Host should be 'example.com'");
		assertEquals(1234, credentials.getPort(), "Port should be 1234");
		assertEquals("test_db", credentials.getDatabase(), "Database should be 'test_db'");
		assertEquals("test_user", credentials.getUser(), "User should be 'test_user'");
		assertEquals("test_password", credentials.getPassword(), "Password should be 'test_password'");
	}

	@Test
	public void testToString() {
		DatabaseCredentials credentials = DatabaseCredentials.of("example.com", 1234, "test_db", "test_user", "test_password");
		String expectedString = "StorageCredentials{host='example.com', port=1234, database='test_db', user='test_user', password='test_password'}";

		assertEquals(expectedString, credentials.toString(), "toString() should return the expected string representation");
	}
}
