package me.colingrimes.midnight.command.node;

import me.colingrimes.midnight.MockSetup;
import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.test.TestCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandNodeTest extends MockSetup {

	@Mock private org.bukkit.command.Command cmd;
	private final String label = "test";
	private final String[] args = new String[] { "arg1", "arg2" };

	@Test
	void testOnCommand() {
		CommandHandler handler1 = CommandHandler.create(plugin, new TestCommand("Test 1"));
		CommandHandler handler2 = CommandHandler.create(plugin, new TestCommand("Test 2"));

		// Set up the parent command and the "test" sub command
		CommandNode parent = new CommandNode(null, handler1);
		CommandNode child = new CommandNode(parent, handler2);
		parent.getChildren().put("test", child);

		// Test the parent command being executed.
		parent.onCommand(bukkit.player, cmd, label, args);
		verify(bukkit.player).sendMessage("Test 1");

		// Test the sub command being executed.
		child.onCommand(bukkit.player, cmd, label, args);
		verify(bukkit.player).sendMessage("Test 2");

		// Test the sub command being executed through the parent and a sub command argument.
		parent.onCommand(bukkit.player, cmd, label, new String[] { "test", "arg1", "arg2" });
		verify(bukkit.player, times(2)).sendMessage("Test 2");
	}

	@Test
	void testOnCommandNoHandler() {
		// Set up the command handler to fail on command.
		CommandHandler handler = spy(CommandHandler.create(plugin, new TestCommand()));
		when(handler.onCommand(bukkit.player, cmd, label, args)).thenReturn(false);

		// Set up the parent command and the "test" sub command
		CommandNode parent = new CommandNode(null);
		CommandNode child = new CommandNode(null, handler);
		CommandNode grandchild = new CommandNode(child);
		parent.getChildren().put("test", child);
		child.getChildren().put("test", grandchild);

		// Test the parent command failing to execute and failing to find a usage message.
		parent.onCommand(bukkit.player, cmd, label, args);
		verify(bukkit.player).sendMessage("Unknown command. Type \"/help\" for help.");

		// Test the sub command being executed with its usage message.
		child.onCommand(bukkit.player, cmd, label, args);
		verify(bukkit.player).sendMessage("Usage: /test <arg1> <arg2>");

		// Test the grandchild command failing to execute and finding the usage message from the parent.
		grandchild.onCommand(bukkit.player, cmd, label, args);
		verify(bukkit.player, times(2)).sendMessage("Usage: /test <arg1> <arg2>");
	}

	@Test
	void testOnTabComplete() {
		CommandHandler handler1 = CommandHandler.create(plugin, new TestCommand("Test 1"));
		CommandHandler handler2 = CommandHandler.create(plugin, new TestCommand("Test 2", List.of("Test 2")));

		// Set up the parent command and the "test" sub command
		CommandNode parent = new CommandNode(null, handler1);
		CommandNode child = new CommandNode(parent, handler2);
		parent.getChildren().put("test", child);

		// Test the tab completion for the parent command.
		assertEquals(List.of("test", "testing", "tested"), parent.onTabComplete(bukkit.player, cmd, label, new String[] { "arg1" }));

		// Test the tab completion for the sub command.
		assertEquals(List.of("Test 2"), child.onTabComplete(bukkit.player, cmd, label, new String[] { "arg1" }));

		// Test the tab completion for the sub command through the parent and a sub command argument.
		assertEquals(List.of("Test 2"), parent.onTabComplete(bukkit.player, cmd, label, new String[] { "test", "arg2" }));
	}

	@Test
	void testOnTabCompleteNoHandler() {
		// Set up the parent command and the "test" sub command
		CommandNode parent = new CommandNode(null);
		CommandNode child = new CommandNode(parent);
		parent.getChildren().put("test", child);

		// Test the tab completion with the child command nodes from nothing else being found.
		assertEquals(List.of("test"), parent.onTabComplete(bukkit.player, cmd, label, new String[] { "te" }));

		// Test the failure to find any child command nodes.
		assertNull(parent.onTabComplete(bukkit.player, cmd, label, new String[]{"arg1"}));

		// Test the null tab completion from no args, no handler, and no children.
		assertNull(child.onTabComplete(bukkit.player, cmd, label, new String[]{"arg1", "arg2"}));
	}
}
