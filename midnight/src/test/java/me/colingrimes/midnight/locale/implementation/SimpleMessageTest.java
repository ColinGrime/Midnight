package me.colingrimes.midnight.locale.implementation;

import me.colingrimes.midnight.locale.Placeholders;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class SimpleMessageTest {

	@Test
	void testSendTo() {
		CommandSender senderMock = Mockito.mock(CommandSender.class);
		String message = "Hello, {player}!";
		SimpleMessage simpleMessage = new SimpleMessage(message);

		Placeholders placeholders = new Placeholders("{player}", "John");

		simpleMessage.sendTo(senderMock, placeholders);
		verify(senderMock).sendMessage("Hello, John!");
	}

	@Test
	void testSendToWithoutPlaceholders() {
		CommandSender senderMock = Mockito.mock(CommandSender.class);
		String message = "Hello, World!";
		SimpleMessage simpleMessage = new SimpleMessage(message);

		simpleMessage.sendTo(senderMock, null);
		verify(senderMock).sendMessage("Hello, World!");
	}
}
