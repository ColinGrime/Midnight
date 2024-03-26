package me.colingrimes.midnight.command.handler.util;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.command.ConsoleCommandSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class SenderTest extends MockSetup {

	@Test
	void testSender_Player() {
		Sender sender = new Sender(bukkit.player);
		assertEquals(bukkit.player, sender.handle());
		assertTrue(sender.hasPermission("test.permission"));
		assertFalse(sender.hasPermission("midnight.test"));
		assertTrue(sender.isPlayer());
		assertEquals(bukkit.player, sender.player());
		assertEquals(bukkit.location, sender.location());
		assertEquals(bukkit.world, sender.world());
		assertEquals(1.0, sender.x());
		assertEquals(2.0, sender.y());
		assertEquals(3.0, sender.z());

		sender.message("Hello, world!");
		verify(bukkit.player).sendMessage("Hello, world!");
	}

	@Test
	void testSender_Console(@Nonnull @Mock ConsoleCommandSender console) {
		Sender sender = new Sender(console);
		assertEquals(console, sender.handle());
		assertFalse(sender.hasPermission("test.permission"));
		assertFalse(sender.hasPermission("midnight.test"));
		assertFalse(sender.isPlayer());
		assertThrows(IllegalStateException.class, sender::player);
		assertThrows(IllegalStateException.class, sender::location);
		assertThrows(IllegalStateException.class, sender::world);
		assertThrows(IllegalStateException.class, sender::x);
		assertThrows(IllegalStateException.class, sender::y);
		assertThrows(IllegalStateException.class, sender::z);

		sender.message("Hello, world!");
		verify(console).sendMessage("Hello, world!");
	}
}
