package me.colingrimes.midnight.command.handler;

import me.colingrimes.midnight.MockSetup;
import me.colingrimes.midnight.command.exception.CommandNotImplementedException;
import me.colingrimes.midnight.test.TestCommand;
import me.colingrimes.midnight.test.TestCommandEmpty;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandHandlerTest extends MockSetup {

	@Mock private CommandSender sender;
	@Mock private org.bukkit.command.Command cmd;
	private final String label = "test";
	private final String[] args = new String[] { "arg1", "arg2" };

	@Test
	void testOnCommand() {
		TestCommand command = spy(new TestCommand());
		CommandHandler handler = CommandHandler.create(plugin, command);

		// Test player required message.
		assertTrue(handler.onCommand(sender, cmd, label, new String[] { "arg1" }));
		verify(sender).sendMessage((String) argThat(a -> a.toString().contains("This command can only be executed by a player.")));

		// Test permission required message.
		assertTrue(handler.onCommand(bukkit.player2, cmd, label, new String[] { "arg1" }));
		verify(bukkit.player2).sendMessage((String) argThat(a -> a.toString().contains("You lack the required permission for this command.")));

		// Test usage message from not enough arguments.
		assertTrue(handler.onCommand(bukkit.player, cmd, label, new String[] { "arg1" }));
		verify(bukkit.player).sendMessage((String) argThat(a -> a.toString().contains("Usage: /test <arg1> <arg2>")));

		// Test command execution.
		assertTrue(handler.onCommand(bukkit.player, cmd, label, args));
		verify(bukkit.player).sendMessage("Test executed successfully!");

		// Test usage message from CommandNotImplementedException.
		doThrow(new CommandNotImplementedException()).when(command).execute(eq(plugin), any(), any());
		assertTrue(handler.onCommand(bukkit.player, cmd, label, args));
		verify(bukkit.player, times(2)).sendMessage((String) argThat(a -> a.toString().contains("Usage: /test <arg1> <arg2>")));
	}

	@Test
	void testOnTabComplete() {
		CommandHandler handler = CommandHandler.create(plugin, new TestCommand());
		assertEquals(List.of("test", "testing", "tested"), handler.onTabComplete(sender, cmd, label, new String[] { "arg1" }));
		assertNull(handler.onTabComplete(sender, cmd, label, args));
	}

	@Test
	void testEmptyCommand() {
		CommandHandler handler = CommandHandler.create(plugin, new TestCommandEmpty());

		// Test command failure from not enough arguments.
		assertFalse(handler.onCommand(bukkit.player, cmd, label, new String[] { "arg1" }));

		// Test command failure from CommandNotImplementedException.
		assertFalse(handler.onCommand(bukkit.player, cmd, label, args));

		// Test null tab completions.
		assertNull(handler.onTabComplete(sender, cmd, label, new String[] { "arg1" }));
		assertNull(handler.onTabComplete(sender, cmd, label, args));
	}
}
