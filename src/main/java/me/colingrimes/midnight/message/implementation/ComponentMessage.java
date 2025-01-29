package me.colingrimes.midnight.message.implementation;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.text.Text;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a message that is sent as a {@link TextComponent}.
 */
public class ComponentMessage implements Message<TextComponent> {

    private final TextComponent content;

    public ComponentMessage() {
        this(new TextComponent());
    }

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
            recipient.sendMessage(Text.color(content.toLegacyText()));
        }
    }
}
