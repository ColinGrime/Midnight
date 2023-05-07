package me.colingrimes.midnight.channel;

import me.colingrimes.midnight.channel.implementation.BasicParticipant;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.implementation.ChannelMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a participant in a communication channel.
 */
public interface Participant {

    /**
     * Creates a new participant from a player.
     * @param player the player to create the participant from
     * @return the new participant
     */
    static Participant of(@Nonnull Player player) {
        return new BasicParticipant(player);
    }

    /**
     * Gets the participant's list of logs.
     * @return the logs of the participant
     */
    @Nonnull
    List<ChannelMessage<?>> getLogs();

    /**
     * Gets the participant's unique identifier.
     * @return the participant's unique identifier
     */
    @Nonnull
    UUID getID();

    /**
     * Gets the participant's display name.
     * @return the participant's display name
     */
    @Nonnull
    String getName();

    /**
     * Gets the player associated with this participant.
     * @return the associated player
     */
    @Nonnull
    Player player();

    /**
     * Sends a message to this participant.
     * @param message the message to send
     */
    void send(@Nonnull Message<?> message);

    /**
     * Sends a private message to another participant.
     * @param recipient the participant to send the private message to
     * @param message the message to send
     */
    void privateMessage(@Nonnull Participant recipient, @Nonnull Message<?> message);

    /**
     * Gets the channels this participant has access to.
     * @return a list of associated channels
     */
    @Nonnull
    List<Channel> getChannels();

    /**
     * Adds a channel to the participant's list of accessible channels.
     * @param channel the channel to add
     */
    void addChannel(@Nonnull Channel channel);

    /**
     * Removes a channel from the participant's list of accessible channels.
     * @param channel the channel to remove
     */
    void removeChannel(@Nonnull Channel channel);

    /**
     * Gets the active channel the participant is in.
     * This is the channel that will be used when sending messages.
     * @return the active channel
     */
    @Nonnull
    Channel getActiveChannel();

    /**
     * Sets the active channel for the participant.
     * This is the channel that will be used when sending messages.
     * @param channel the channel to set as the active channel
     */
    void setActiveChannel(@Nonnull Channel channel);

    /**
     * Checks if the participant is muted in the chat.
     * @return true if the participant is muted, false otherwise
     */
    boolean isMuted();

    /**
     * Mutes the participant for a specific duration.
     * @param duration the duration to mute the participant for
     */
    void mute(@Nonnull Duration duration);

    /**
     * Unmutes the participant.
     */
    void unmute();

    /**
     * Gets the set of ignored players for this participant.
     * @return a set of ignored players' unique identifiers
     */
    @Nonnull
    Set<UUID> getIgnored();

    /**
     * Checks if the specified participant is being ignored.
     * @param participant the participant to check
     * @return true if the participant is being ignored, false otherwise
     */
    boolean isIgnoring(@Nonnull Participant participant);

    /**
     * Adds a participant to the list of ignored players.
     * @param participant the participant to ignore
     */
    void ignore(@Nonnull Participant participant);

    /**
     * Removes a participant from the list of ignored players.
     * @param participant the participant to unignore
     */
    void unignore(@Nonnull Participant participant);

    /**
     * Gets the participant's nickname, if any.
     * @return the participant's nickname, or {@link #getName()} if not set
     */
    @Nonnull
    String getNickname();

    /**
     * Sets the participant's nickname.
     * @param nickname the nickname to set
     */
    void setNickname(@Nonnull String nickname);

    /**
     * Checks if the participant is currently online.
     * @return true if the participant is online, false otherwise
     */
    boolean isOnline();

    /**
     * Gets the timestamp when the participant was last seen online.
     * @return the timestamp when the participant was last seen online
     */
    @Nonnull
    ZonedDateTime getLastSeen();

    /**
     * Gets the timestamp when the participant first joined the server.
     * @return the timestamp when the participant first joined the server
     */
    @Nonnull
    ZonedDateTime getJoinedDate();
}
