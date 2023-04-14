package me.colingrimes.midnight.util;

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
		// Mock logger.
		MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
		java.util.logging.Logger logger = mock(java.util.logging.Logger.class);
		when(Bukkit.getLogger()).thenReturn(logger);

		// Test valid input.
		Logger.log("Log message.");
		verify(logger).log(Level.INFO, "Log message.");

		Logger.warn("Warning message.");
		verify(logger).log(Level.WARNING, "Warning message.");

		Logger.severe("Severe message.");
		verify(logger).log(Level.SEVERE, "Severe message.");

		// Close the mocked class.
		bukkit.close();
	}
}