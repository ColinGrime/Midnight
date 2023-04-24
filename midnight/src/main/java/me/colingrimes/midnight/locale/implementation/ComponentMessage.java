package me.colingrimes.midnight.locale.implementation;

import me.colingrimes.midnight.locale.Messageable;
import me.colingrimes.midnight.locale.Placeholders;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ComponentMessage implements Messageable {

	private final TextComponent component;

	public ComponentMessage(@Nonnull TextComponent component) {
		this.component = component;
	}

	@Override
	public void sendTo(@Nonnull CommandSender sender, @Nullable Placeholders placeholders) {
		placeholders = Objects.requireNonNullElseGet(placeholders, Placeholders::new);
		sender.spigot().sendMessage(placeholders.replace(component));
	}
}
