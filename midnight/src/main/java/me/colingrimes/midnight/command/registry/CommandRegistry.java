package me.colingrimes.midnight.command.registry;

import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.command.registry.node.CommandNode;
import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry for managing commands and their handlers within the Midnight plugin.
 * This class provides methods for registering commands, subcommands, and their
 * associated {@link CommandHandler}s, and for automatically registering those
 * commands with the Bukkit command map.
 */
public class CommandRegistry {

	private final Midnight plugin;
	private final Map<String, CommandNode> commandNodes = new HashMap<>();

	/**
	 * Creates a new command registry.
	 *
	 * @param plugin the Midnight plugin
	 */
	public CommandRegistry(@Nonnull Midnight plugin) {
		this.plugin = plugin;
	}

	/**
	 * Scans and registers command classes within the root command package of the plugin.
	 */
	public void scanPackages() {
		CommandPackageScanner packageScanner = new CommandPackageScanner(plugin, this);
		packageScanner.scan();
	}

	/**
	 * Registers a command with its associated {@link CommandHandler}.
	 *
	 * @param args    the command arguments, where the first element is the command name,
	 *                and any subsequent elements are subcommand names
	 * @param handler the command handler for the command
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
	 * Registers a command with its associated {@link CommandNode}.
	 *
	 * @param name the command name
	 * @param node the command node
	 */
	private void registerCommand(@Nonnull String name, @Nonnull CommandNode node) {
		PluginCommand pluginCommand = Common.server().getPluginCommand(name);
		if (pluginCommand != null) {
			pluginCommand.setExecutor(node);
			pluginCommand.setTabCompleter(node);
			return;
		}

		try {
			Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(plugin.getName(), new CustomCommand(name, node, node));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A custom command wrapper around the {@link CommandExecutor} and {@link TabExecutor}
	 * interfaces, allowing commands to be executed and tab-completed by the Bukkit server.
	 */
	private static class CustomCommand extends Command {

		private final CommandExecutor commandExecutor;
		private final TabExecutor tabExecutor;

		public CustomCommand(@Nonnull String name, @Nonnull CommandExecutor commandExecutor, @Nonnull TabExecutor tabExecutor) {
			super(name);
			this.commandExecutor = commandExecutor;
			this.tabExecutor = tabExecutor;
		}

		@Override
		public boolean execute(@Nonnull CommandSender sender, @Nonnull String commandLabel, @Nonnull String[] args) {
			return commandExecutor.onCommand(sender, this, commandLabel, args);
		}

		@Nonnull
		@Override
		public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
			List<String> list = tabExecutor.onTabComplete(sender, this, alias, args);
			return list == null ? new ArrayList<>() : list;
		}
	}
}
