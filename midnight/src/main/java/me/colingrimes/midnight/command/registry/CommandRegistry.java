package me.colingrimes.midnight.command.registry;

import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.command.node.CommandNode;
import me.colingrimes.midnight.command.registry.util.CommandRegistrar;
import me.colingrimes.midnight.MidnightPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

	private final MidnightPlugin plugin;
	private final Map<String, CommandNode> commandNodes = new HashMap<>();

	public CommandRegistry(@Nonnull MidnightPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Registers a command.
	 * @param args the command arguments
	 * @param handler the command handler
	 */
	public void register(@Nonnull String[] args, @Nullable CommandHandler handler) {
		CommandNode current = commandNodes.get(args[0]);
		if (current == null) {
			// If there is only one word, then we need to set the command handler.
			if (args.length == 1) {
				current = commandNodes.computeIfAbsent(args[0], name -> new CommandNode(null, handler));
			} else {
				current = commandNodes.computeIfAbsent(args[0], name -> new CommandNode(null));
			}

			// Register the new command.
			registerCommand(args[0], current);
		}

		for (int i=1; i<args.length; i++) {
			String word = args[i];
			CommandNode finalCurrent = current;

			// If this is the last word, then we need to set the command handler.
			if (i == args.length - 1) {
				current = current.getChildren().computeIfAbsent(word, name -> new CommandNode(finalCurrent, handler));
			} else {
				current = current.getChildren().computeIfAbsent(word, name -> new CommandNode(finalCurrent));
			}
		}
	}

	/**
	 * Registers a command.
	 * @param name the command name
	 * @param node the command node
	 */
	private void registerCommand(@Nonnull String name, @Nonnull CommandNode node) {
		CustomCommand customCommand = new CustomCommand(name, node, node);
		CommandRegistrar.registerCommand(plugin, customCommand);
	}
}
