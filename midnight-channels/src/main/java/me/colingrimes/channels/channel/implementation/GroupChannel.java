package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * This is an implementation of a group channel.
 * In a group channel, only chatters in the same group can send and receive messages.
 */
public class GroupChannel extends BaseChannel {

    private final Supplier<Set<UUID>> groupMembersSupplier;

    /**
     * Constructor for creating a new GroupChannel.
     *
     * @param name the name of the channel
     * @param groupMembersSupplier the supplier that supplies the set of UUIDs of eligible chatters
     */
    public GroupChannel(@Nonnull String name, @Nonnull Supplier<Set<UUID>> groupMembersSupplier) {
        super(name);
        this.groupMembersSupplier = groupMembersSupplier;
    }

    @Nonnull
    @Override
    public ChannelType getType() {
        return ChannelType.GROUP;
    }

    @Override
    void broadcast(@Nonnull ChannelMessage<?> message) {
        for (UUID groupMember : groupMembersSupplier.get()) {
            ChannelAPI.getManager().getChatter(groupMember).ifPresent(c -> c.send(message));
        }
    }

    @Override
    void send(@Nonnull Chatter sender, @Nonnull ChannelMessage<?> message) {
        for (UUID groupMember : groupMembersSupplier.get()) {
            Optional<Chatter> chatter = ChannelAPI.getManager().getChatter(groupMember);
            if (chatter.isEmpty()) {
                continue;
            }

            Chatter recipient = chatter.get();
            if (sender.hasPermission("channels.staff") || recipient.hasPermission("channels.staff") || !recipient.isIgnoring(sender.getID())) {
                chatter.get().send(settings.getFormattedMessage(sender, message));
            }
        }
    }

    @Override
    public boolean hasAccess(@Nonnull Chatter chatter) {
        return groupMembersSupplier.get().contains(chatter.getID());
    }

    /**
     * Gets the set of UUIDs of eligible chatters.
     *
     * @return the set of UUIDs of eligible chatters
     */
    @Nonnull
    public Set<UUID> getGroupMembers() {
        return groupMembersSupplier.get();
    }
}
