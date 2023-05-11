package me.colingrimes.channels.channel.misc;

import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;

/**
 * Represents a chat filter that can be used to filter messages in a chat channel.
 */
public interface ChatFilter {

    /**
     * Filters a channel message based on its content.
     * <p>
     * Implementations of this method should return true
     * if the message should be blocked or modified.
     *
     * @param message the message to filter
     * @return true if the message should be filtered, false otherwise
     */
    boolean filter(@Nonnull ChannelMessage<?> message);
}
