package me.colingrimes.midnight.message.implementation;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a list of messages that is sent as plain text.
 * Text is automatically colored.
 */
public class ListMessage implements Message<List<String>> {

    private final List<String> content;

    public ListMessage(@Nonnull List<String> content) {
        this.content = Text.color(content);
    }

    @Nonnull
    @Override
    public List<String> getContent() {
        return content;
    }

    @Override
    public void send(@Nonnull CommandSender recipient) {
        content.forEach(recipient::sendMessage);
    }

    @Nonnull
    @Override
    public ListMessage replace(@Nonnull Placeholders placeholders) {
        return new ListMessage(placeholders.apply(content));
    }
}
