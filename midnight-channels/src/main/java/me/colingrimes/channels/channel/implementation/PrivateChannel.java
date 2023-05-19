package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.channels.event.ChannelMessageEvent;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.Common;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
     * Sends a server message to the recipient.
     *
     * @param recipient the recipient of the message
     * @param message   the message to send
     */
    public void broadcast(@Nonnull Chatter recipient, @Nonnull Message<?> message) {
        if (!enabled) {
            return;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, null, message);
        settings.logMessage(channelMessage);
        recipient.send(channelMessage);
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
        return sendPrivateMessage(sender, recipient, message);
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
        if (sender.getLastMessagedBy() == null) {
            Messages.NOBODY_TO_REPLY_TO.send(sender.player());
            return false;
        }

        Optional<Chatter> recipient = ChannelAPI.getManager().getChatter(sender.getLastMessagedBy());
        if (recipient.isEmpty()) {
            Messages.NOBODY_TO_REPLY_TO.send(sender.player());
            return false;
        }

        return sendPrivateMessage(sender, recipient.get(), message);
    }

    /**
     * Sends a message from the sender to the recipient.
     *
     * @param sender    the sender
     * @param recipient the recipient
     * @param message   the message
     * @return true if the message was sent successfully, false otherwise
     */
    private boolean sendPrivateMessage(@Nonnull Chatter sender, @Nonnull Chatter recipient, @Nonnull Message<?> message) {
        if (!enabled) {
            Messages.CHANNEL_DISABLED.send(sender.player());
            return false;
        } else if (sender.getID().equals(recipient.getID())) {
            Messages.MESSAGE_SELF.send(sender.player());
            return false;
        } else if (sender.isMuted()) {
            Messages.CURRENTLY_MUTED.send(sender.player());
            return false;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        settings.logMessage(channelMessage);
        if (settings.filter(channelMessage)) {
            return false;
        }

        Set<Chatter> recipients = new HashSet<>(Set.of(sender, recipient));

        // You cannot ignore staff, staff cannot ignore you.
        if (!sender.hasPermission("channels.staff")) {
            recipients.removeIf(r -> r.isIgnoring(sender.getID()) && !r.hasPermission("channels.staff"));
        }

        ChannelMessageEvent channelMessageEvent = new ChannelMessageEvent(this, sender, recipients, message);
        Common.call(channelMessageEvent);
        Message<?> newMessage = channelMessageEvent.getMessage();

        if (channelMessageEvent.isCancelled()) {
            return false;
        } else if (recipients.size() == 1) {
            sender.send(settings.getFormattedMessage(sender, newMessage, "Me", recipient.getName()));
            return true;
        } else {
            sender.send(settings.getFormattedMessage(sender, newMessage, "Me", recipient.getName()));
            recipient.send(settings.getFormattedMessage(sender, newMessage, sender.getName(), "Me"));
            recipient.setLastMessagedBy(sender.getID());
            return true;
        }
    }

    @Override
    public void broadcast(@Nonnull Message<?> message) {
        throw new UnsupportedOperationException("Cannot broadcast a message in a PrivateChannel.");
    }

    @Nonnull
    @Override
    public Set<Chatter> getRecipients() {
        throw new UnsupportedOperationException("Cannot get recipients of a PrivateChannel.");
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        throw new UnsupportedOperationException("Access control is not applicable to a PrivateChannel.");
    }
}
