package me.colingrimes.midnight.message.implementation;

import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.message.Message;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a message that is sent as a {@link TextComponent}.
 */
public class ComponentMessage implements Message<TextComponent> {

    private final TextComponent content;

    public ComponentMessage(@Nonnull TextComponent content) {
        this.content = content;
    }

    @Nonnull
    @Override
    public TextComponent getContent() {
        return content;
    }

    @Override
    public void send(@Nonnull CommandSender recipient) {
        if (recipient instanceof Player) {
            ((Player) recipient).spigot().sendMessage(content);
        } else {
            recipient.sendMessage(content.toLegacyText());
        }
    }

    @Nonnull
    @Override
    public ComponentMessage replace(@Nonnull Placeholders placeholders) {
        return new ComponentMessage(placeholders.apply(content));
    }
}
