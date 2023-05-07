package me.colingrimes.midnight.channel.filter;

import me.colingrimes.midnight.message.implementation.ChannelMessage;

import javax.annotation.Nonnull;

/**
 * Represents a chat filter that can be used to filter messages in a chat channel.
 */
public interface ChatFilter {

    /**
     * Filters a channel message based on its content.
     *
     * <p>
     * Implementations of this method should return true if the message passes the filter,
     * and false if the message should be blocked or modified.
     * </p>
     *
     * @param message the message to filter
     * @return true if the message passes the filter, false otherwise
     */
    boolean filter(@Nonnull ChannelMessage<?> message);
}
