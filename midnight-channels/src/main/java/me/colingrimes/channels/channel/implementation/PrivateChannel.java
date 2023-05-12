package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.Players;

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
        if (!enabled) {
            Settings.CHANNEL_DISABLED_MESSAGE.send(sender.player());
            return false;
        } else if (sender.isMuted()) {
            Settings.MUTED_MESSAGE.send(sender.player());
            return false;
        } else if (recipient.isIgnoring(sender.getID())) {
            return false;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        settings.logMessage(channelMessage);

        // Send filtered messages to staff.
        if (settings.filter(channelMessage)) {
            Placeholders placeholders = Placeholders
                    .of("{player}", sender.getName())
                    .add("{message}", message.toText());
            Players.all().stream()
                    .filter(p -> p.hasPermission("channels.filtered"))
                    .forEach(Settings.MESSAGE_FILTERED.replace(placeholders)::send);
            return false;
        }

        recipient.send(settings.getFormattedMessage(sender, channelMessage));
        recipient.setLastMessagedBy(sender.getID());
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
        if (!enabled) {
            Settings.CHANNEL_DISABLED_MESSAGE.send(sender.player());
            return false;
        } else if (sender.isMuted()) {
            Settings.MUTED_MESSAGE.send(sender.player());
            return false;
        } else if (sender.getLastMessagedBy() == null) {
            Settings.REPLY_FAILURE.send(sender.player());
            return false;
        }

        Optional<Chatter> recipient = ChannelAPI.getManager().getChatter(sender.getLastMessagedBy());
        if (recipient.isEmpty() || recipient.get().isIgnoring(sender.getID())) {
            return false;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        settings.logMessage(channelMessage);

        // Send filtered messages to staff.
        if (settings.filter(channelMessage)) {
            Placeholders placeholders = Placeholders
                    .of("{player}", sender.getName())
                    .add("{message}", message.toText());
            Players.all().stream()
                    .filter(p -> p.hasPermission("channels.filtered"))
                    .forEach(Settings.MESSAGE_FILTERED.replace(placeholders)::send);
            return false;
        }

        recipient.get().send(settings.getFormattedMessage(sender, channelMessage));
        recipient.get().setLastMessagedBy(sender.getID());
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
