package me.colingrimes.midnight.command.handler;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a handler for executing and providing tab completion for custom commands.
 * Implementations of this interface are responsible for handling command execution and
 * tab completion suggestions.
 */
public interface CommandHandler extends TabExecutor {

	/**
	 * Creates a new {@link StandardCommandHandler} instance for the specified command.
	 *
	 * @param plugin  the plugin instance
	 * @param command the custom command to handle
	 * @param <T>     the type of the plugin
	 * @return a new {@link CommandHandler} instance
	 */
	@Nonnull
	static <T extends Midnight> StandardCommandHandler<T> create(@Nonnull T plugin, @Nonnull me.colingrimes.midnight.command.Command<T> command) {
		return new StandardCommandHandler<>(plugin, command);
	}

	/**
	 * Executes the command.
	 * If nothing executes or no message was sent to the sender, {@code false} is
	 * returned so that a parent usage message or unknown command message can be sent.
	 *
	 * @param commandSender the sender of the command
	 * @param command 	    the command that was executed
	 * @param s 		    the label of the command
	 * @param strings 	    the arguments provided with the command
	 * @return {@code true} if the command was executed successfully or some message was sent, {@code false} if nothing occurred
	 */
	@Override
	boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings);

	/**
	 * Gets the usage message for the command.
	 *
	 * @return the usage message or {@code null} if no usage message is provided
	 */
	@Nullable
	Message<?> getUsage();
}
