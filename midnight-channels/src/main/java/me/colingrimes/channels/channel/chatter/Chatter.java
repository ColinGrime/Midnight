package me.colingrimes.channels.channel.chatter;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.midnight.message.Message;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a chatting player in a channel.
 */
public interface Chatter {

    /**
     * Gets the Chatter associated with the player.
     *
     * @param player the player to get the chatter from
     * @return the chatter associated with the player
     * @throws IllegalStateException if the chatter data is not found
     */
    @Nonnull
    static Chatter of(@Nonnull Player player) {
        return ChannelAPI.getManager().getChatter(player).orElseThrow(() -> new IllegalStateException("Chatter data not found for player " + player.getName()));
    }

    /**
     * Gets an immutable set of all <b>online</b> chatters.
     *
     * @return a set of all chatters
     */
    @Nonnull
    static Set<Chatter> all() {
        return ChannelAPI.getManager().getChatters().stream().filter(Chatter::online).collect(Collectors.toSet());
    }

    /**
     * Gets the chatter's unique identifier.
     *
     * @return the chatter's unique identifier
     */
    @Nonnull
    UUID getID();

    /**
     * Gets the chatter's display name.
     *
     * @return the chatter's display name
     */
    @Nonnull
    String getName();

    /**
     * Gets the player associated with this chatter.
     *
     * @return the associated player
     */
    @Nonnull
    Player player();

    /**
     * Checks if the chatter is currently online.
     *
     * @return true if the chatter is online, false otherwise
     */
    boolean online();

    /**
     * Checks if the chatter has the specified permission.
     * If they are not online, this will return false.
     *
     * @param permission the permission to check
     * @return true if the chatter has the permission, false otherwise
     */
    boolean hasPermission(@Nonnull String permission);

    /**
     * Sends a message to this chatter.
     *
     * @param message the message to send
     */
    void send(@Nonnull Message<?> message);

    /**
     * Sends a message to this chatter.
     *
     * @param message the message to send
     */
    default void send(@Nonnull String message) {
        send(Message.of(message));
    }

    /**
     * Gets the time the chatter is muted until.
     *
     * @return the time the chatter is muted until
     */
    @Nullable
    ZonedDateTime getMuteEndTime();

    /**
     * Checks if the chatter is muted in the chat.
     *
     * @return true if the chatter is muted, false otherwise
     */
    boolean isMuted();

    /**
     * Mutes the chatter for a specific duration.
     *
     * @param duration the duration to mute the chatter for
     */
    void mute(@Nonnull Duration duration);

    /**
     * Unmutes the chatter.
     */
    void unmute();

    /**
     * Gets the set of ignored players for this chatter.
     *
     * @return a set of ignored players' unique identifiers
     */
    @Nonnull
    Set<UUID> getIgnored();

    /**
     * Checks if the specified UUID is being ignored.
     *
     * @param uuid the chatter's uuid to check
     * @return true if the chatter is being ignored, false otherwise
     */
    boolean isIgnoring(@Nonnull UUID uuid);

    /**
     * Adds the UUID to the list of ignored players.
     *
     * @param uuid the chatter's uuid to ignore
     * @return true if the chatter was not already being ignored, false otherwise
     */
    boolean ignore(@Nonnull UUID uuid);

    /**
     * Removes the UUID from the list of ignored players.
     *
     * @param uuid the chatter's uuid to unignore
     * @return true if the chatter was being ignored, false otherwise
     */
    boolean unignore(@Nonnull UUID uuid);

    /**
     * Gets the chatter's nickname, if any.
     *
     * @return the chatter's nickname, or {@link #getName()} if not set
     */
    @Nonnull
    String getNickname();

    /**
     * Sets the chatter's nickname.
     *
     * @param nickname the nickname to set
     */
    void setNickname(@Nonnull String nickname);

    /**
     * Gets the UUID of the chatter who last sent a private message to this chatter.
     *
     * @return the UUID of the last chatter who sent a private message to this chatter, or null if no one has
     */
    @Nullable
    UUID getLastMessagedBy();

    /**
     * Sets the UUID of the chatter who last sent a private message to this chatter.
     *
     * @param uuid the UUID of the chatter who last sent a private message to this chatter
     */
    void setLastMessagedBy(@Nonnull UUID uuid);

    /**
     * Gets the timestamp when the chatter was last seen online.
     *
     * @return the timestamp when the chatter was last seen online
     */
    @Nonnull
    ZonedDateTime getLastSeen();

    /**
     * Gets the timestamp when the chatter first joined the server.
     *
     * @return the timestamp when the chatter first joined the server
     */
    @Nonnull
    ZonedDateTime getJoinDate();
}
