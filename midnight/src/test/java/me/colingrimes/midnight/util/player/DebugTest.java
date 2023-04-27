package me.colingrimes.midnight.util.player;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class DebugTest {

	private Player player;

	@BeforeEach
	public void setUp() {
		player = Mockito.mock(Player.class);
	}

	@Test
	public void testSend() {
		String message = "This is a debug message";
		Debug.send(player, message);

		verify(player).sendMessage(eq("§c[Debug] §eThis is a debug message"));
	}

	@Test
	public void testSendWithKeyValue() {
		String key = "Key";
		String value = "Value";
		Debug.send(player, key, value);

		verify(player).sendMessage(eq("§c[Debug] §eKey: §aValue"));
	}

	@Test
	public void testSendWithKeyValueAndFormatting() {
		String key = "Key";
		String value = "&6Value";
		Debug.send(player, key, value);

		verify(player).sendMessage(eq("§c[Debug] §eKey: §a§6Value"));
	}

}
