package me.colingrimes.channels.filter;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    default boolean filter(@Nonnull ChannelMessage<?> message) {
        return filter(message, message.getChatter().orElse(null));
    }

    /**
     * Filters a message based on its content.
     * <p>
     * Implementations of this method should return true
     * if the message should be blocked or modified.
     *
     * @param message the message to filter
     * @param chatter the chatter who sent the message
     * @return true if the message should be filtered, false otherwise
     */
    boolean filter(@Nonnull Message<?> message, @Nullable Chatter chatter);

    /**
     * Gets the type of this filter.
     *
     * @return the type of this filter
     */
    @Nullable
    default ChatFilterType getType() {
        return null;
    }
}
