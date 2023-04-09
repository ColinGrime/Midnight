package me.colingrimes.midnight.util;

import me.colingrimes.midnight.plugin.Midnight;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.logging.Level;

import static org.mockito.Mockito.*;

class LoggerTest {

	@Test
	@DisplayName("Verify that messages are being logged.")
	void testNBT() {
		// Mock plugin.
		MockedStatic<Midnight> domains = mockStatic(Midnight.class);
		when(Midnight.getInstance()).thenReturn(mock(Midnight.class));
		when(Midnight.getInstance().getName()).thenReturn("Midnight");

		// Mock logger.
		MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
		java.util.logging.Logger logger = mock(java.util.logging.Logger.class);
		when(Bukkit.getLogger()).thenReturn(logger);

		// Test valid input.
		Logger.log("Log message.");
		verify(logger).log(Level.INFO, "[Midnight] Log message.");

		Logger.warn("Warning message.");
		verify(logger).log(Level.WARNING, "[Midnight] Warning message.");

		Logger.severe("Severe message.");
		verify(logger).log(Level.SEVERE, "[Midnight] Severe message.");

		// Close the mocked class.
		domains.close();
		bukkit.close();
	}
}