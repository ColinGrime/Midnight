package me.colingrimes.midnight.command;

import me.colingrimes.midnight.locale.Message;
import me.colingrimes.midnight.plugin.Midnight;
import me.colingrimes.midnight.util.Logger;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class BaseCommand implements CommandExecutor, TabExecutor {

	private final Map<String, SubCommand> subCommands = new HashMap<>();

	public BaseCommand(@Nonnull Midnight plugin, @Nonnull String name) {
		PluginCommand command = plugin.getCommand(name);

		if (command == null) {
			Logger.severe("Commands have failed to load. Plugin has been disabled.");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		command.setExecutor(this);
		command.setTabCompleter(this);
		addSubCommands(plugin);
	}

	/**
	 * Adds the subcommands to the base command.
	 */
	private void addSubCommands(Midnight plugin) {
		// register subcommands
		List<SubCommand> subCommands = new ArrayList<>();
		registerSubCommands(subCommands, plugin);

		// add the subcommands
		for (SubCommand subCommand : subCommands) {
			registerSubCommand(subCommand);
		}
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, String[] args) {
		// if there's no subcommand input, send the command usage
		if (args.length == 0 || getSubCommand(args[0]).isEmpty()) {
			getUsage().sendTo(sender);
			return true;
		}

		SubCommand subCommand = getSubCommand(args[0]).get();
		String permission = subCommand.getPermission();

		// if the console is both disabled and the sender, it's invalid
		if (subCommand.requirePlayer() && !(sender instanceof Player)) {
//			Message.FAILURE_INVALID_SENDER.sendTo(sender);
			return true;
		}

		// sender doesn't have permission
		if (permission != null && !sender.hasPermission(permission)) {
//			Message.FAILURE_NO_PERMISSION.sendTo(sender);
			return true;
		}

		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
		if (subCommand.getArgumentsRequired() > subArgs.length) {
			subCommand.getUsage().sendTo(sender);
			return true;
		}

		// subcommand runs
		subCommand.onCommand(sender, subArgs);
		return true;
	}

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
		if (args.length == 1) {
			return new ArrayList<>(subCommands.keySet());
		}

		SubCommand subCommand = subCommands.get(args[0]);
		if (subCommand == null) {
			return null;
		}

		return subCommand.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	/**
	 * Registers a subcommand.
	 *
	 * When a base command is called, it attempts to
	 * match its first argument to the names/aliases
	 * of the cached subcommands.
	 *
	 * @param subCommand any subcommand
	 */
	public void registerSubCommand(SubCommand subCommand) {
		// register subcommand with name
		if (subCommand.getName() != null) {
			subCommands.put(subCommand.getName().toLowerCase(), subCommand);
		}

		// register subcommand with aliases
		if (subCommand.getAliases() != null) {
			for (String alias : subCommand.getAliases()) {
				subCommands.put(alias.toLowerCase(), subCommand);
			}
		}
	}

	/**
	 * @param subCommandString name/alias of a subcommand
	 * @return subcommand wrapped in an Optional object
	 */
	public Optional<SubCommand> getSubCommand(String subCommandString) {
		return Optional.ofNullable(subCommands.get(subCommandString.toLowerCase()));
	}

	/**
	 * @return command usage as configured in the config.yml
	 */
	public abstract Message getUsage();

	/**
	 * @param subCommands list to add the subcommands to
	 */
	public abstract void registerSubCommands(List<SubCommand> subCommands, Midnight plugin);
}