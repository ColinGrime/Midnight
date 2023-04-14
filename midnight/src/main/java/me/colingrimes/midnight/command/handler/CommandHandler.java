package me.colingrimes.midnight.command.handler;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public interface CommandHandler {

	/**
	 * Invokes the command handler.
	 * @param sender the command sender
	 * @param args the command arguments
	 */
	boolean invoke(@Nonnull CommandSender sender, @Nonnull String[] args);
}
