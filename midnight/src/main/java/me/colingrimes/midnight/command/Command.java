package me.colingrimes.midnight.command;

import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.exception.CommandNotImplementedException;
import me.colingrimes.midnight.Midnight;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a custom command designed for use with {@link Midnight} plugins.
 * This interface provides support for command execution, tab completion, usage messages,
 * permissions, aliases, and sender requirements, offering a flexible structure for
 * creating custom commands.
 */
public interface Command<T extends Midnight> {

	/**
	 * Executes the command.
	 *
	 * @param plugin the plugin instance
	 * @param sender the sender of the command
	 * @param args   the arguments of the command
	 * @throws CommandNotImplementedException if the command execution is not implemented
	 */
	default void execute(@Nonnull T plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		throw new CommandNotImplementedException();
	}

	/**
	 * Provides tab completion suggestions for the command.
	 *
	 * @param plugin the plugin instance
	 * @param sender the sender of the command
	 * @param args   the arguments of the command
	 * @return a list of tab completion suggestions or {@code null} if no suggestions are provided
	 */
	@Nullable
	default List<String> tabComplete(@Nonnull T plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return null;
	}

	/**
	 * Configures the command properties using a {@link CommandProperties} object.
	 * This method allows customization of properties such as usage message, permission,
	 * aliases, required arguments, and player requirements for a command.
	 *
	 * @param properties {@link CommandProperties} object containing the command properties
	 */
	default void configureProperties(@Nonnull CommandProperties properties) {}
}
