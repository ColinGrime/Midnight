package me.colingrimes.midnight.command;

import me.colingrimes.midnight.command.node.CommandHandler;
import me.colingrimes.midnight.command.node.CommandNode;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.util.Logger;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

	private final MidnightPlugin plugin;
	private final Map<String, CommandNode> commandNodes = new HashMap<>();

	public CommandManager(@Nonnull MidnightPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Registers a command.
	 * @param args the command arguments
	 * @param handler the command handler
	 */
	public void register(@Nonnull String args, @Nonnull CommandHandler handler) {
		String[] words = args.split(" ");
		CommandNode current;

		// If there is only one word, then we need to set the command handler.
		if (words.length == 1) {
			current = commandNodes.computeIfAbsent(words[0], name -> new CommandNode(plugin, name, handler));
		} else {
			current = commandNodes.computeIfAbsent(words[0], name -> new CommandNode(plugin, name));
		}

		for (int i=1; i<words.length; i++) {
			String word = words[i];

			// If this is the last word, then we need to set the command handler.
			if (i == words.length - 1) {
				current = current.getChildren().computeIfAbsent(word, name -> new CommandNode(handler));
			} else {
				current = current.getChildren().computeIfAbsent(word, name -> new CommandNode());
			}
		}
	}
}
