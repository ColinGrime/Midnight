package me.colingrimes.midnight.storage.sql.connection.hikari;

import com.zaxxer.hikari.HikariConfig;
import me.colingrimes.midnight.storage.sql.DatabaseCredentials;
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
	public void testGetType() {
		assertEquals("MySQL", provider.getType().getName());
	}

	@Test
	public void testStatementProcessor() {
		String input = "CREATE TABLE 'test_table' ('id' INT PRIMARY KEY)";
		String expectedOutput = input.replace('\'', '`');
		assertEquals(expectedOutput, provider.getStatementProcessor().apply(input));
	}

	@Test
	public void testConfigureDatabase() {
		HikariConfig config = mock(HikariConfig.class);
		provider.configureDatabase(config, credentials);

		verify(config).setDriverClassName("com.mysql.cj.jdbc.Driver");
		verify(config).setJdbcUrl("jdbc:mysql://localhost:3306/test_db");
		verify(config).setUsername("user");
		verify(config).setPassword("password");
	}
}
