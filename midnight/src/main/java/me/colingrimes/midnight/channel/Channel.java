package me.colingrimes.midnight.channel;

import me.colingrimes.midnight.channel.util.ChannelPermission;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.implementation.ChannelMessage;
import me.colingrimes.midnight.util.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a communication channel.
 */
public interface Channel {

    /**
     * Gets the channel's list of logs.
     * @return the logs of the channel
     */
    @Nonnull
    List<ChannelMessage<?>> getLogs();

    /**
     * Gets the channel's unique identifier.
     * @return the channel's unique identifier
     */
    @Nonnull
    UUID getID();

    /**
     * Gets the channel's display name.
     * @return the channel's display name
     */
    @Nonnull
    String getName();

    /**
     * Broadcasts a message to all participants in the channel.
     * This is to be used for system messages.
     *
     * @param message the message to send
     */
    void broadcast(@Nonnull Message<?> message);

    /**
     * Sends a message from a {@code Participant} to all participants in the channel.
     * This will fail if the sender has no permission to speak.
     * This is to be used for regular messages.
     *
     * @param sender the participant sending the message
     * @param message the message to send
     */
    void send(@Nonnull Participant sender, @Nonnull Message<?> message);

    /**
     * Gets an unmodifiable set of the channel's participants.
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
     * Verifies if a player is a participant in the channel.
     * @param player the player to check
     * @return true if the player is a participant, false otherwise
     */
    boolean contains(@Nonnull Player player);

    /**
     * Gets the chat color for a participant in the channel.
     * @param participant the participant to get the chat color for
     * @return the chat color, or null if no custom chat color is set
     */
    @Nullable
    ChatColor getChatColor(@Nonnull Participant participant);

    /**
     * Sets or removes the chat color for a participant in the channel.
     * @param participant the participant to modify
     * @param color the chat color to set, or null to remove the custom chat color
     */
    void setChatColor(@Nonnull Participant participant, @Nullable ChatColor color);

    /**
     * Checks if a participant has a specific permission in the channel.
     * @param participant the participant to check
     * @param permission the permission to check for
     * @return true if the participant has the specified permission, false otherwise
     */
    boolean hasPermission(@Nonnull Participant participant, @Nonnull ChannelPermission permission);
}
