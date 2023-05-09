package me.colingrimes.midnight.storage.sql.connection.hikari;

import me.colingrimes.midnight.storage.sql.DatabaseCredentials;
import com.zaxxer.hikari.HikariConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MySqlConnectionProviderTest {

	private DatabaseCredentials credentials;
	private MySqlConnectionProvider provider;

	@BeforeEach
	public void setUp() {
		credentials = mock(DatabaseCredentials.class);
		when(credentials.getHost()).thenReturn("localhost");
		when(credentials.getPort()).thenReturn(3306);
		when(credentials.getDatabase()).thenReturn("test_db");
		when(credentials.getUser()).thenReturn("user");
		when(credentials.getPassword()).thenReturn("password");

		provider = new MySqlConnectionProvider(credentials);
	}

	@Test
	public void testName() {
		assertEquals("MySQL", provider.getName(), "MySqlConnectionProvider should return 'MySQL' as its name");
	}

	@Test
	public void testStatementProcessor() {
		String input = "CREATE TABLE 'test_table' ('id' INT PRIMARY KEY)";
		String expectedOutput = "CREATE TABLE `test_table` (`id` INT PRIMARY KEY)";
		String actualOutput = provider.getStatementProcessor().apply(input);
		assertEquals(expectedOutput, actualOutput, "MySqlConnectionProvider statement processor should replace single quotes with backticks");
	}

	@Test
	public void testConfigureDatabase() {
		HikariConfig config = mock(HikariConfig.class);

		doAnswer(invocation -> {
			assertEquals("com.mysql.cj.jdbc.Driver", invocation.getArgument(0), "Driver class name should be set correctly");
			return null;
		}).when(config).setDriverClassName(anyString());

		doAnswer(invocation -> {
			String jdbcUrl = invocation.getArgument(0);
			String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_db";
			assertEquals(expectedJdbcUrl, jdbcUrl, "JDBC URL should be set correctly");
			return null;
		}).when(config).setJdbcUrl(anyString());

		provider.configureDatabase(config, credentials);

		verify(config).setUsername("user");
		verify(config).setPassword("password");
	}
}
