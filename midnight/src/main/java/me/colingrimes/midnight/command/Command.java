package me.colingrimes.midnight.command;

import me.colingrimes.midnight.command.util.ArgumentList;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.locale.Messageable;
import me.colingrimes.midnight.plugin.MidnightPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a command with support for command execution, tab completion, usage message,
 * permissions, aliases, and sender requirements. Provides a flexible structure for
 * creating custom commands in a {@link MidnightPlugin}.
 */
public interface Command<T extends MidnightPlugin> {

	/**
	 * Executes the command.
	 * @param plugin the plugin instance
	 * @param sender the sender of the command
	 * @param args the arguments of the command
	 */
	default void execute(@Nonnull T plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {}

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
	 * Gets the usage message of the command.
	 * @return usage message
	 */
	@Nullable
	default Messageable getUsage() {
		return null;
	}

	/**
	 * Gets the permission required to run the command.
	 * @return permission required to run the command
	 */
	@Nullable
	default String getPermission() {
		return null;
	}

	/**
	 * Gets the aliases of the command.
	 * @return aliases of the command
	 */
	@Nonnull
	default String[] getAliases() {
		return new String[]{};
	}

	/**
	 * If arguments are required, the usage of the
	 * command will be displayed if there are
	 * not enough arguments inputted.
	 *
	 * @return amount of arguments required
	 */
	default int getArgumentsRequired() {
		return 0;
	}

	/**
	 * If player senders are required, the console
	 * will be sent an invalid sender message if
	 * they attempt to run the command.
	 *
	 * @return true if player sender should be required
	 */
	default boolean isPlayerRequired() {
		return false;
	}
}