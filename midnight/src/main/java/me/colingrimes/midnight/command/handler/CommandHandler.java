package me.colingrimes.midnight.command.handler;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a handler for executing and providing tab completion for custom commands.
 * Implementations of this interface are responsible for handling command execution and
 * tab completion suggestions.
 */
public interface CommandHandler extends TabExecutor {

	/**
	 * Creates a new {@link CommandHandler} instance for the specified command.
	 *
	 * @param plugin  the plugin instance
	 * @param command the custom command to handle
	 * @param <T>     the type of the plugin
	 * @return a new {@link CommandHandler} instance
	 */
	@Nonnull
	static <T extends Midnight> CommandHandler create(@Nonnull T plugin, @Nonnull me.colingrimes.midnight.command.Command<T> command) {
		return new StandardCommandHandler<>(plugin, command);
	}

	/**
	 * Provides tab completion suggestions for the specified command.
	 *
	 * @param sender  the command sender
	 * @param command the command being executed
	 * @param label   the command label
	 * @param args    the command arguments
	 * @return a list of tab completion suggestions or {@code null} if no suggestions are provided
	 */
	@Override
	default List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		return null;
	}

	/**
	 * Gets the usage message for the command.
	 *
	 * @return the usage message or {@code null} if no usage message is provided
	 */
	@Nullable
	Message<?> getUsage();
}
