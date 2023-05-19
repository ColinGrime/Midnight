package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Nonnull
    @Override
    public Set<Chatter> getRecipients() {
        return Chatter.all().stream().filter(this::hasAccess).collect(Collectors.toSet());
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        return chatter.player().hasPermission(permissionNode);
    }
}
