package me.colingrimes.midnight.log.channel;

import me.colingrimes.midnight.channel.Channel;
import me.colingrimes.midnight.channel.Participant;
import me.colingrimes.midnight.log.MessageLogs;
import me.colingrimes.midnight.message.implementation.ChannelMessage;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores and retrieves logs related to channels.
 */
public class ChannelLogs extends MessageLogs<Channel, ChannelMessage<?>> {

    /**
     * Gets logs by content.
     * @param channel the channel for the log
     * @param content the content to filter by
     * @return a list of logs with the specified content
     */
    @Nonnull
    public List<ChannelMessage<?>> getLogs(@Nonnull Channel channel, @Nonnull String content) {
        return getLogs(channel).stream()
                .filter(log -> log.toText().contains(content))
                .collect(Collectors.toList());
    }

    /**
     * Gets logs by {@link Participant}.
     * @param channel the channel for the log
     * @param participant the participant to filter by
     * @return a list of logs sent by the specified participant
     */
    @Nonnull
    public List<ChannelMessage<?>> getLogs(@Nonnull Channel channel, @Nonnull Participant participant) {
        return getLogs(channel).stream()
                .filter(log -> log.getParticipant().equals(participant))
                .collect(Collectors.toList());
    }

    /**
     * Get logs from a specific time to the present.
     * @param channel the channel for the log
     * @param from the start time of the range
     * @return a list of logs from the specified time to the present
     */
    @Nonnull
    public List<ChannelMessage<?>> getLogs(@Nonnull Channel channel, @Nonnull ZonedDateTime from) {
        return getLogs(channel, from, ZonedDateTime.now());
    }

    /**
     * Get logs by time range.
     * @param channel the channel for the log
     * @param from the start time of the range
     * @param to the end time of the range
     * @return a list of logs within the specified time range
     */
    @Nonnull
    public List<ChannelMessage<?>> getLogs(@Nonnull Channel channel, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) {
        return getLogs(channel).stream()
                .filter(log -> log.getTimestamp().compareTo(from) >= 0 && log.getTimestamp().compareTo(to) <= 0)
                .collect(Collectors.toList());
    }
}
