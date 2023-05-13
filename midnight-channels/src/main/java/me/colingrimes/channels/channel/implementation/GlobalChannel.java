package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.message.ChannelMessage;

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
                chatter.send(message);
            }
        }
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        return true;
    }
}
