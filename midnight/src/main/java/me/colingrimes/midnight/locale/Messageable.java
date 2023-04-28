package me.colingrimes.midnight.locale;

import me.colingrimes.midnight.command.util.Sender;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Messageable {

	/**
	 * Sends the message to the specified sender.
	 * @param sender the sender to send the message to
	 */
	default void sendTo(@Nonnull Sender sender) {
		sendTo(sender.handle(), null);
	}

	/**
	 * Sends the message to the specified sender.
	 * @param sender the sender to send the message to
	 */
	default void sendTo(@Nonnull CommandSender sender) {
		sendTo(sender, null);
	}

	/**
	 * Sends the message, with replaced placeholders, to the specified sender
	 * @param sender the sender to send the message to
	 * @param placeholders the placeholders to search for
	 */
	default void sendTo(@Nonnull Sender sender, @Nullable Placeholders placeholders) {
		sendTo(sender.handle(), placeholders);
	}


	/**
	 * Sends the message, with replaced placeholders, to the specified sender
	 * @param sender the sender to send the message to
	 * @param placeholders the placeholders to search for
	 */
	void sendTo(@Nonnull CommandSender sender, @Nullable Placeholders placeholders);
}
