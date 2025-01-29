package me.colingrimes.midnight.command.registry;

import me.colingrimes.midnight.MockSetup;
import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.command.node.CommandNode;
import me.colingrimes.midnight.test.TestCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CommandRegistryTest extends MockSetup {

	@BeforeEach
	void setUp() {

	}

	@Test
	void testRegister() {
		CommandHandler handler = CommandHandler.create(plugin, new TestCommand());
		CommandRegistry registry = new CommandRegistry(plugin);
		registry.register(new String[]{"test", "sub", "sub2"}, handler);

		// Since there are subcommands, the handler only gets attached to the last subcommand.
		assertEquals(1, registry.getCommandNodes().size());
		CommandNode node = registry.getCommandNodes().get("test");
		assertEquals(1, node.getChildren().size());
		assertNull(node.getHandler());

		node = node.getChildren().get("sub");
		assertEquals(1, node.getChildren().size());
		assertNull(node.getHandler());

		node = node.getChildren().get("sub2");
		assertEquals(0, node.getChildren().size());
		assertEquals(handler, node.getHandler());
	}
}
