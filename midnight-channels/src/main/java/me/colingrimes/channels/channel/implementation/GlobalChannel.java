package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.Set;

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

    @Nonnull
    @Override
    public Set<Chatter> getRecipients() {
        return Chatter.all();
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        return true;
    }

    /**
     * Sends a message from a {@code Chatter} to all users with access to the channel.
     * This is to be used to re-route chat messages from chat.
     *
     * @param event the chat event to send
     * @return true if the message was sent, otherwise
     */
    public boolean send(@Nonnull AsyncPlayerChatEvent event) {
        return send(Chatter.of(event.getPlayer()), event.getMessage());
    }
}
