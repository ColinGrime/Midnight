package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;

/**
 * This is an implementation of a global channel.
 * In a global channel, every chatter can send and receive messages.
 */
public class GlobalChannel extends BaseChannel {

    /**
     * Constructor for creating a new GlobalChannel.
     *
     * @param name the name of the channel
     */
    public GlobalChannel(@Nonnull String name) {
        super(name);
    }

    @Nonnull
    @Override
    public ChannelType getType() {
        return ChannelType.GLOBAL;
    }

    @Override
    void broadcast(@Nonnull ChannelMessage<?> message) {
        Chatter.all().forEach(p -> p.send(message));
    }

    @Override
    void send(@Nonnull Chatter sender, @Nonnull ChannelMessage<?> message) {
        for (Chatter chatter : Chatter.all()) {
            if (sender.hasPermission("channels.staff") || chatter.hasPermission("channels.staff") || !chatter.isIgnoring(sender.getID())) {
                chatter.send(settings.getFormattedMessage(sender, message));
            }
        }
    }

    /**
     * Sends a message from a {@code Chatter} to all users with access to the channel.
     * This is to be used to re-route chat messages from chat.
     *
     * @param event the chat event to send
     * @return true if the message was sent, false if the event was cancelled
     */
    public boolean send(@Nonnull AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Chatter sender = Chatter.of(player);

        if (!enabled) {
            Messages.CHANNEL_DISABLED.send(player);
        } else if (!hasAccess(sender)) {
            Messages.CHANNEL_ACCESS_DENIED.send(player);
        } else if (sender.isMuted()) {
            Messages.CURRENTLY_MUTED.send(player);
        } else {
            ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, Message.of(event.getMessage()));
            settings.logMessage(channelMessage);

            if (!settings.filter(channelMessage)) {
                for (Chatter chatter : Chatter.all()) {
                    if (chatter.isIgnoring(sender.getID()) && !sender.hasPermission("channels.staff") && !chatter.hasPermission("channels.staff")) {
                        event.getRecipients().remove(chatter.player());
                    }
                }

                event.setFormat(settings.getFormattedMessage(sender, channelMessage));
                return true;
            }
        }

        event.setCancelled(true);
        return false;
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        return true;
    }
}
