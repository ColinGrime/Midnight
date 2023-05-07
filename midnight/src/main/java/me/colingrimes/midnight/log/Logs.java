package me.colingrimes.midnight.log;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a generic log storage interface.
 * @param <T> the type of the key
 * @param <M> the type of the message
 */
public interface Logs<T, M> {

    /**
     * Gets all the messages associated with a key.
     * @param key the key for the log
     * @return a list of messages associated with the specified key
     */
    @Nonnull
    List<M> getLogs(@Nonnull T key);

    /**
     * Add a message to the logs.
     * @param key the key for the log
     * @param message the message to add
     */
    void addLog(@Nonnull T key, @Nonnull M message);

    /**
     * Clear all messages associated with a key.
     * @param key the key for the log
     */
    void clearLogs(@Nonnull T key);
}
