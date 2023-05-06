package me.colingrimes.midnight.message.implementation;

import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * Represents a message that is sent as plain text.
 */
public class TextMessage implements Message<String> {

    private final String content;

    public TextMessage(@Nonnull String content) {
        this.content = content;
    }

    @Nonnull
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void send(@Nonnull CommandSender recipient) {
        recipient.sendMessage(content);
    }

    @Nonnull
    @Override
    public TextMessage replace(@Nonnull Placeholders placeholders) {
        return new TextMessage(placeholders.apply(content));
    }
}
