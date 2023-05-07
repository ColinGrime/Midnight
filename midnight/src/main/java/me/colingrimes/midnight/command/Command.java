package me.colingrimes.midnight.command;

import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.exception.CommandNotImplementedException;
import me.colingrimes.midnight.Midnight;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a command with support for command execution, tab completion, usage message,
 * permissions, aliases, and sender requirements. Provides a flexible structure for
 * creating custom commands in a {@link Midnight}.
 */
public interface Command<T extends Midnight> {

	/**
	 * Executes the command.
	 * @param plugin the plugin instance
	 * @param sender the sender of the command
	 * @param args the arguments of the command
	 */
	default void execute(@Nonnull T plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		throw new CommandNotImplementedException();
	}

	/**
	 * Tab completes the command.
	 * @param plugin the plugin instance
	 * @param sender the sender of the command
	 * @param args the arguments of the command
	 * @return list of tab completions
	 */
	@Nullable
	default List<String> tabComplete(@Nonnull T plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return null;
	}

	/**
	 * Configures the command properties using a {@link CommandProperties} object.
	 * This method allows users to specify properties such as usage message, permission,
	 * aliases, arguments required, and player requirement for a command.
	 * @param properties {@link CommandProperties} object containing the command properties
	 */
	default void configureProperties(@Nonnull CommandProperties properties) {}
}