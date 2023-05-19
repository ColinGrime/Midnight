package me.colingrimes.channels.channel;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelSettings;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Represents a communication channel.
 */
public interface Channel {

    /**
     * Gets the type of the channel.
     *
     * @return the channel's type
     */
    @Nonnull
    ChannelType getType();

    /**
     * Gets the channel's display name.
     *
     * @return the channel's display name
     */
    @Nonnull
    String getName();

    /**
     * Gets the channel's settings.
     *
     * @return the channel's settings
     */
    @Nonnull
    ChannelSettings getSettings();

    /**
     * Broadcasts a message to all users with access to the channel.
     * This is to be used for system messages and will bypass all chat filters.
     *
     * @param message the message to send
     */
     void broadcast(@Nonnull Message<?> message);

    /**
     * Broadcasts a message to all users with access to the channel.
     * This is to be used for system messages and will bypass all chat filters.
     *
     * @param message the message to send
     */
    default void broadcast(@Nonnull String message) {
        broadcast(Message.of(message));
    }

    /**
     * Sends a message from a {@code Chatter} to all users with access to the channel.
     * This is to be used for regular messages.
     *
     * @param sender  the chatter sending the message
     * @param message the message to send
     * @return true if the message was sent, false otherwise
     */
    boolean send(@Nonnull Chatter sender, @Nonnull Message<?> message);

    /**
     * Sends a message from a {@code Chatter} to all users with access to the channel.
     * This is to be used for regular messages.
     *
     * @param sender  the chatter sending the message
     * @param message the message to send
     * @return true if the message was sent, false otherwise
     */
    default boolean send(@Nonnull Chatter sender, @Nonnull String message) {
        return send(sender, Message.of(message));
    }

    /**
     * Gets the set of recipients that would receive the message.
     *
     * @return the set of recipients
     */
    @Nonnull
    Set<Chatter> getRecipients();

    /**
     * Verifies if a chatter has access to the channel.
     *
     * @param chatter the chatter to check
     * @return true if the chatter has access to the channel, false otherwise
     */
    boolean hasAccess(@Nonnull Chatter chatter);

    /**
     * Checks if the channel is enabled.
     * Disabled channels will not send any messages.
     *
     * @return true if the channel is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Enables the channel.
     * This will allow messages to be sent through the channel.
     */
    void enable();

    /**
     * Disables the channel.
     * This will prevent messages from being sent through the channel.
     */
    void disable();
}
