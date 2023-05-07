package me.colingrimes.midnight.message.implementation;

import me.colingrimes.midnight.channel.Channel;
import me.colingrimes.midnight.channel.Participant;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Represents a message sent in a {@link Channel} by a {@link Participant}.
 * @param <T> the type of the message content
 */
public class ChannelMessage<T> implements Message<T> {

    private final Channel channel;
    private final Participant participant;
    private final Message<T> message;
    private final ZonedDateTime timestamp;

    public ChannelMessage(@Nullable Channel channel, @Nonnull Participant participant, @Nonnull Message<T> message) {
        this.channel = channel;
        this.participant = participant;
        this.message = message;
        this.timestamp = ZonedDateTime.now();
    }

    @Nullable
    public Channel getChannel() {
        return channel;
    }

    @Nonnull
    public Participant getParticipant() {
        return participant;
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
        return new ChannelMessage<>(channel, participant, message.replace(placeholders));
    }

    @Nonnull
    @Override
    public String toString() {
        return "ChannelMessage{" +
                "channel=" + channel +
                ", participant=" + participant +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ChannelMessage<?> that)) return false;
        return Objects.equals(getChannel(), that.getChannel())
                && Objects.equals(getParticipant(), that.getParticipant())
                && Objects.equals(message, that.message)
                && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannel(), getParticipant(), message, getTimestamp());
    }
}
