package me.colingrimes.channels.channel;

import me.colingrimes.channels.channel.implementation.GlobalChannel;
import me.colingrimes.channels.channel.implementation.GroupChannel;
import me.colingrimes.channels.channel.implementation.PrivateChannel;
import me.colingrimes.channels.channel.implementation.StaffChannel;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A factory for creating channel instances.
 */
public class ChannelFactory {

    /**
     * Creates a new global channel with the given name.
     *
     * @param name the name of the channel
     * @return a new global channel
     */
    @Nonnull
    public Channel createGlobalChannel(@Nonnull String name) {
        return new GlobalChannel(name);
    }

    /**
     * Creates a new private channel with the given name and the UUIDs of the chatters involved.
     *
     * @param player1 the first chatter
     * @param player2 the second chatter
     * @return a new private channel
     */
    @Nonnull
    public Channel createPrivateChannel(@Nonnull Player player1, @Nonnull Player player2) {
        return new PrivateChannel(player1, player2);
    }

    /**
     * Creates a new group channel with the given name and a supplier
     * that provides the UUIDs of the chatters in the group.
     *
     * @param name                 the name of the channel
     * @param groupMembersSupplier the supplier that provides the UUIDs of the chatters in the group
     * @return a new group channel
     */
    @Nonnull
    public Channel createGroupChannel(@Nonnull String name, @Nonnull Supplier<Set<UUID>> groupMembersSupplier) {
        return new GroupChannel(name, groupMembersSupplier);
    }

    /**
     * Creates a new staff channel with the given name and a permission node.
     *
     * @param name           the name of the channel
     * @param permissionNode the permission node
     * @return a new staff channel
     */
    @Nonnull
    public Channel createStaffChannel(@Nonnull String name, @Nonnull String permissionNode) {
        return new StaffChannel(name, permissionNode);
    }
}
