package me.colingrimes.channels.message;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.midnight.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Represents a log sent in a {@code Channel} by a {@code Chatter}.
 * If the {@link Chatter} is {@code null}, then the message was sent by the server.
 *
 * @param <T> the type of the message content
 */
public class ChannelLog<T> implements Message<T> {

    private final String channelName;
    private final Chatter chatter;
    private final Message<T> message;
    private final ZonedDateTime timestamp;

    /**
     * Creates a new channel message.
     *
     * @param channelName the name of the channel the message was sent in
     * @param chatter     the chatter that sent the message
     * @param message     the message
     * @param timestamp   the timestamp of the message
     */
    public ChannelLog(@Nonnull String channelName, @Nullable Chatter chatter, @Nonnull Message<T> message, @Nonnull ZonedDateTime timestamp) {
        this.channelName = channelName;
        this.chatter = chatter;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Nonnull
    public String getChannelName() {
        return channelName;
    }

    @Nullable
    public Chatter getChatter() {
        return chatter;
    }

    @Nonnull
    @Override
    public T getContent() {
        return message.getContent();
    }

    @Nonnull
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public void send(@Nonnull CommandSender recipient) {
        message.send(recipient);
    }

    @Nonnull
    @Override
    public String toString() {
        return "ChannelMessage{" +
                "channelName=" + channelName +
                ", chatter=" + chatter +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ChannelLog<?> that)) return false;
        return Objects.equals(getChannelName(), that.getChannelName())
                && Objects.equals(getChatter(), that.getChatter())
                && Objects.equals(message, that.message)
                && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannelName(), getChatter(), message, getTimestamp());
    }
}
