package me.colingrimes.midnight.command.handler;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.argument.ArgumentList;
import me.colingrimes.midnight.locale.Messageable;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.plugin.config.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A standard implementation of the {@link CommandHandler} interface.
 * This class handles the execution and tab completion of commands
 * for a given {@link Command} and {@link MidnightPlugin} instance.
 *
 * <p>It checks for sender requirements, permissions, and the number of arguments
 * before executing the command. Appropriate messages are sent to the sender
 * based on these conditions.</p>
 *
 * @param <T> the type of the MidnightPlugin
 */
public class StandardCommandHandler<T extends MidnightPlugin> implements CommandHandler {

	private final T plugin;
	private final Command<T> command;

	public StandardCommandHandler(@Nonnull T plugin, @Nonnull Command<T> command) {
		this.plugin = plugin;
		this.command = command;
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command cmd, @Nonnull String label, @Nonnull String[] args) {
		if (command.isPlayerRequired() && !(sender instanceof Player)) {
			Settings.INVALID_SENDER.sendTo(sender);
			return true;
		} else if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
			Settings.PERMISSION_DENIED.sendTo(sender);
			return true;
		} else if (args.length < command.getArgumentsRequired()) {
			if (getUsage() != null) {
				getUsage().sendTo(sender);
				return true;
			}
			return false;
		}

		ArgumentList argumentList = new ArgumentList(args);
		if (!command.execute(plugin, sender, argumentList)) {
			if (getUsage() != null) {
				getUsage().sendTo(sender);
				return true;
			}
			return false;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command cmd, @Nonnull String label, @Nonnull String[] args) {
		return command.tabComplete(plugin, sender, new ArgumentList(args));
	}

	@Nullable
	@Override
	public Messageable getUsage() {
		return command.getUsage();
	}
}
