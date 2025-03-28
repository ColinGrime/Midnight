package me.colingrimes.midnight.command.handler;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.exception.CommandNotImplementedException;
import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A standard implementation of the {@link CommandHandler} interface designed for use
 * with {@link Midnight} plugins. This class handles the execution and tab completion
 * of commands for a given {@link Command} and {@link Midnight} plugin instance.
 * <p>
 * Before executing the command, the handler checks for sender requirements, permissions,
 * and the number of arguments. Appropriate messages are sent to the sender based on these
 * conditions.
 *
 * @param <T> the type of the plugin
 */
public class StandardCommandHandler<T extends Midnight> implements CommandHandler {

	private final T plugin;
	private final Command<T> command;
	private final CommandProperties properties;

	public StandardCommandHandler(@Nonnull T plugin, @Nonnull Command<T> command) {
		this.plugin = plugin;
		this.command = command;
		this.properties = new CommandProperties();
		this.command.configureProperties(properties);
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command cmd, @Nonnull String label, @Nonnull String[] args) {
		if (properties.isPlayerRequired() && !(sender instanceof Player)) {
			sender.sendMessage(Text.color("&4&l❌ &cThis command can only be executed by a player."));
			return true;
		} else if (properties.getPermission() != null && !sender.hasPermission(properties.getPermission())) {
			sender.sendMessage(Text.color("&4&l❌ &cYou lack the required permission for this command."));
			return true;
		} else if (args.length < properties.getArgumentsRequired()) {
			if (getUsage() != null) {
				getUsage().send(sender);
				return true;
			}
			return false;
		}

		try {
			command.execute(plugin, new Sender(sender), new ArgumentList(args));
		} catch (CommandNotImplementedException e) {
			if (getUsage() != null) {
				getUsage().send(sender);
				return true;
			}
			return false;
		}
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command cmd, @Nonnull String label, @Nonnull String[] args) {
		if (properties.getPermission() != null && !sender.hasPermission(properties.getPermission())) {
			return null;
		}
		return command.tabComplete(plugin, new Sender(sender), new ArgumentList(args));
	}

	@Nullable
	@Override
	public Message<?> getUsage() {
		return properties.getUsage();
	}

	@Nonnull
	@Override
	public CommandProperties getProperties() {
		return properties;
	}
}
