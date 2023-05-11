package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;

/**
 * This is an implementation of a staff channel.
 * In a staff channel, only chatters with staff permissions can send and receive messages.
 */
public class StaffChannel extends BaseChannel {

    private final String permissionNode;

    /**
     * Constructor for creating a new StaffChannel.
     *
     * @param name the name of the channel
     * @param permissionNode the permission node required to send and receive messages
     */
    public StaffChannel(@Nonnull String name, @Nonnull String permissionNode) {
        super(name);
        this.permissionNode = permissionNode;
    }

    @Nonnull
    @Override
    public ChannelType getType() {
        return ChannelType.STAFF;
    }

    @Override
    void broadcast(@Nonnull ChannelMessage<?> message) {
        Chatter.all().stream().filter(this::hasAccess).forEach(p -> p.send(message));
    }

    @Override
    void send(@Nonnull Chatter sender, @Nonnull ChannelMessage<?> message) {
        Chatter.all().stream().filter(this::hasAccess).forEach(p -> p.send(settings.getFormattedMessage(sender, message)));
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        return chatter.player().hasPermission(permissionNode);
    }
}
