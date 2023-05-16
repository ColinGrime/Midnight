package me.colingrimes.channels.filter;

import me.colingrimes.channels.message.ChannelMessage;

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
    boolean filter(@Nonnull ChannelMessage<?> message);

    /**
     * Filters a message based on its content.
     * <p>
     * Implementations of this method should return true
     * if the message should be blocked or modified.
     * <p>
     * This method does not work with {@code SpamFilter}s or {@code FloodFilter}s.
     * Use {@link #filter(ChannelMessage)} instead for those filters.
     *
     * @param message the message to filter
     * @return true if the message should be filtered, false otherwise
     */
    default boolean filter(@Nonnull String message) {
        return false;
    }

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
