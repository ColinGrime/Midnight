package me.colingrimes.channels.message;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a message sent in a {@code Channel} by a {@code Chatter}.
 * If the {@link Chatter} is {@code null}, then the message was sent by the server.
 *
 * @param <T> the type of the message content
 */
public class ChannelMessage<T> implements Message<T> {

    private final Channel channel;
    private final Chatter chatter;
    private final Message<T> message;
    private final ZonedDateTime timestamp;

    /**
     * Creates a new channel message.
     *
     * @param channel the channel the message was sent in
     * @param chatter the chatter that sent the message
     * @param message the message
     */
    public ChannelMessage(@Nonnull Channel channel, @Nullable Chatter chatter, @Nonnull Message<T> message) {
        this.channel = channel;
        this.chatter = chatter;
        this.message = message;
        this.timestamp = ZonedDateTime.now();
    }

    /**
     * Creates a new channel message from storage.
     *
     * @param channel   the channel the message was sent in
     * @param chatter   the chatter that sent the message
     * @param message   the message
     * @param timestamp the timestamp of the message
     */
    public ChannelMessage(@Nonnull Channel channel, @Nullable Chatter chatter, @Nonnull Message<T> message, @Nonnull ZonedDateTime timestamp) {
        this.channel = channel;
        this.chatter = chatter;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Nonnull
    public Channel getChannel() {
        return channel;
    }

    @Nonnull
    public Optional<Chatter> getChatter() {
        return Optional.ofNullable(chatter);
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
    public Message<T> replace(@Nonnull Placeholders placeholders) {
        return new ChannelMessage<>(channel, chatter, message.replace(placeholders));
    }

    @Nonnull
    @Override
    public String toString() {
        return "ChannelMessage{" +
                "channel=" + channel +
                ", chatter=" + chatter +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ChannelMessage<?> that)) return false;
        return Objects.equals(getChannel(), that.getChannel())
                && Objects.equals(getChatter(), that.getChatter())
                && Objects.equals(message, that.message)
                && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannel(), getChatter(), message, getTimestamp());
    }
}
