package me.colingrimes.midnight.locale;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SimpleMessage implements Messageable {

	private final String message;

	public SimpleMessage(@Nonnull String message) {
		this.message = message;
	}

	@Override
	public void sendTo(@Nonnull CommandSender sender, @Nullable Placeholders placeholders) {
		placeholders = Objects.requireNonNullElseGet(placeholders, Placeholders::new);
		sender.sendMessage(placeholders.replace(message));
	}
}
