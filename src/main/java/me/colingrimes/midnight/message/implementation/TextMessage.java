package me.colingrimes.midnight.message.implementation;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * Represents a message that is sent as plain text.
 * Text is automatically colored.
 */
public class TextMessage implements Message<String> {

    private final String content;

    public TextMessage(@Nonnull String content) {
        this.content = Text.color(content);
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
}
