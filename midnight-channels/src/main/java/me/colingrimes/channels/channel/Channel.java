package me.colingrimes.channels.channel;

import me.colingrimes.channels.channel.filter.ChatFilter;
import me.colingrimes.channels.channel.permission.ChannelPermission;
import me.colingrimes.channels.channel.permission.PermissionProvider;
import me.colingrimes.channels.channel.settings.ChannelSettings;
import me.colingrimes.channels.channel.implementation.SimpleChannel;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a communication channel.
 */
public interface Channel {

    /**
     * Creates a new channel with the specified name.
     *
     * @param name               the name of the channel
     * @param permissionProvider the permission provider for the channel
     * @return the new channel
     */
    static Channel of(@Nonnull String name, @Nullable PermissionProvider permissionProvider) {
        return new SimpleChannel(name, permissionProvider);
    }

    /**
     * Gets the channel's unique identifier.
     *
     * @return the channel's unique identifier
     */
    @Nonnull
    UUID getID();

    /**
     * Gets the channel's display name.
     *
     * @return the channel's display name
     */
    @Nonnull
    String getName();

    /**
     * Gets the channel's permission provider.
     *
     * @return the channel's permission provider
     */
    @Nonnull
    PermissionProvider getPermissionProvider();

    /**
     * Checks if a participant has a specific permission in the channel.
     *
     * @param participant the participant to check
     * @param permission  the permission to check for
     * @return true if the participant has the specified permission, false otherwise
     */
    boolean hasPermission(@Nonnull Participant participant, @Nonnull ChannelPermission permission);

    /**
     * Gets the channel's settings.
     *
     * @return the channel's settings
     */
    @Nonnull
    ChannelSettings getSettings();

    /**
     * Gets an unmodifiable set of the channel's chat filters.
     *
     * @return an unmodifiable set of chat filters
     */
    @Nonnull
    Set<ChatFilter> getFilters();

    /**
     * Adds a chat filter to the channel.
     *
     * @param filter the chat filter to add
     */
    void addFilter(@Nonnull ChatFilter filter);

    /**
     * Removes a chat filter from the channel.
     *
     * @param filter the chat filter to remove
     */
    void removeFilter(@Nonnull ChatFilter filter);

    /**
     * Broadcasts a message to all participants in the channel.
     * This is to be used for system messages and will bypass all chat filters.
     *
     * @param message the message to send
     */
    void broadcast(@Nonnull Message<?> message);

    /**
     * Sends a message from a {@code Participant} to all participants in the channel.
     * This will fail if the sender has no permission to speak.
     * This is to be used for regular messages.
     *
     * @param sender  the participant sending the message
     * @param message the message to send
     */
    void send(@Nonnull Participant sender, @Nonnull Message<?> message);

    /**
     * Gets an unmodifiable set of the channel's participants.
     *
     * @return an unmodifiable set of participants
     */
    @Nonnull
    Set<Participant> getParticipants();

    /**
     * Adds a participant to the channel.
     * This will fail if the participant has no permission to join.
     *
     * @param participant the participant to add
     * @return true if added successfully, false if already present
     */
    boolean add(@Nonnull Participant participant);

    /**
     * Removes a participant from the channel.
     * This will fail if the participant has no permission to leave.
     *
     * @param participant the participant to remove
     * @return true if removed successfully, false if not present
     */
    boolean remove(@Nonnull Participant participant);

    /**
     * Verifies if a participant is in the channel.
     *
     * @param participant the participant to check
     * @return true if the participant is in the channel, false otherwise
     */
    boolean contains(@Nonnull Participant participant);

    /**
     * Gets the chat color for a participant in the channel.
     *
     * @param participant the participant to get the chat color for
     * @return the chat color, or null if no custom chat color is set
     */
    @Nullable
    ChatColor getChatColor(@Nonnull Participant participant);

    /**
     * Sets or removes the chat color for a participant in the channel.
     *
     * @param participant the participant to modify
     * @param color       the chat color to set, or null to remove the custom chat color
     */
    void setChatColor(@Nonnull Participant participant, @Nullable ChatColor color);
}
