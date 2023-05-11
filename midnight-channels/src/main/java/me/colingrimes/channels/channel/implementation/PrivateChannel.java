package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This is an implementation of a private channel.
 * In a private channel, only the two specified chatters can send and receive messages.
 */
public class PrivateChannel extends BaseChannel {

    /**
     * Constructor for creating a new PrivateChannel.
     *
     * @param name    the name of the channel
     */
    public PrivateChannel(@Nonnull String name) {
        super(name);
    }

    @Nonnull
    @Override
    public ChannelType getType() {
        return ChannelType.PRIVATE;
    }

    /**
     * Sends a message to the recipient.
     *
     * @param sender    the sender of the message
     * @param recipient the recipient of the message
     * @param message   the message to send
     * @return true if the message was sent successfully, false otherwise
     */
    public boolean send(@Nonnull Chatter sender, @Nonnull Chatter recipient, @Nonnull Message<?> message) {
        if (!enabled || sender.isMuted()) {
            return false;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        if (settings.filter(channelMessage) || recipient.isIgnoring(sender.getID())) {
            return false;
        }

        recipient.send(settings.getFormattedMessage(sender, channelMessage));
        recipient.setLastMessagedBy(sender.getID());
        settings.logMessage(channelMessage);
        return true;
    }

    /**
     * Sends a reply to the last {@link Chatter} who messaged the sender.
     *
     * @param sender  the chatter sending the message
     * @param message the message to send
     * @return true if the message was sent successfully, false otherwise
     */
    @Override
    public boolean send(@Nonnull Chatter sender, @Nonnull Message<?> message) {
        if (!enabled || sender.isMuted() || sender.getLastMessagedBy() == null) {
            return false;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        Optional<Chatter> recipient = ChannelAPI.getManager().getChatter(sender.getLastMessagedBy());
        if (settings.filter(channelMessage) || recipient.isEmpty() || recipient.get().isIgnoring(sender.getID())) {
            return false;
        }

        recipient.get().send(settings.getFormattedMessage(sender, channelMessage));
        settings.logMessage(channelMessage);
        return true;
    }

    @Override
    public void broadcast(@Nonnull Message<?> message) {
        throw new UnsupportedOperationException("Cannot broadcast a message in a PrivateChannel.");
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        throw new UnsupportedOperationException("Access control is not applicable to a PrivateChannel.");
    }
}
