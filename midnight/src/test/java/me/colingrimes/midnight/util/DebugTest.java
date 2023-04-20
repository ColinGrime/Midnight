package me.colingrimes.midnight.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.colingrimes.midnight.MidnightPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class DebugTest {

	private static ServerMock server;

	@BeforeAll
	public static void load() {
		server = MockBukkit.mock();
		MockBukkit.load(MidnightPlugin.class);
	}

	@AfterAll
	public static void unload() {
		MockBukkit.unmock();
	}

	@Test
	@DisplayName("Verify that debug messages appear.")
	void testDebug() throws NoSuchFieldException, IllegalAccessException {
		// Make DEBUG field accessible.
		Field debugField = Debug.class.getDeclaredField("DEBUG");
		debugField.setAccessible(true);
		debugField.setBoolean(null, true);

		// Add a player mock to the server.
		PlayerMock player = server.addPlayer("Bill");

		// Test valid input.
		Debug.send(player, "Test debug message.");
		player.assertSaid("§c[Debug] §eTest debug message.");

		Debug.send(player, "Key", "Value");
		player.assertSaid("§c[Debug] §eKey: §aValue");

		// Test invalid input.
		Debug.send(player, "");
		player.assertSaid("§c[Debug] §e");

		Debug.send(player, "Key", "");
		player.assertSaid("§c[Debug] §eKey: §a");

		Debug.send(player, "", "Value");
		player.assertSaid("§c[Debug] §e: §aValue");

		// Make DEBUG field inaccessible
		debugField.setBoolean(null, false);
		debugField.setAccessible(false);
	}
}
