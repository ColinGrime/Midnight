package me.colingrimes.midnight.command.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a command handler responsible for executing and tab completing custom commands.
 */
public interface CommandHandler extends TabExecutor {

	@Override
	default List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		return null;
	}
}
