package me.colingrimes.channels.storage;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.implementation.TextMessage;
import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.io.DatabaseUtils;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatStorage extends SqlStorage<ChannelMessage<?>> {

    private static final String LOGS_SAVE = "INSERT INTO 'channel_logs' (channel_name, participant_id, content, timestamp) VALUES (?, ?, ?, ?)";
    private static final String LOGS_DELETE = "DELETE FROM 'channel_logs' WHERE channel_name = ? AND participant_id = ? AND timestamp = ?";
    private static final String LOGS_GET_BY_CHANNEL = "SELECT * FROM 'channel_logs' WHERE channel_name = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";
    private static final String LOGS_GET_BY_PARTICIPANT = "SELECT * FROM 'channel_logs' WHERE participant_id = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";

    private final MidnightChannels plugin;

    public ChatStorage(@Nonnull MidnightChannels plugin, @Nonnull ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.plugin = plugin;
    }

    @Override
    public void save(@Nonnull ChannelMessage<?> data) throws Exception {
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_SAVE))) {
                ps.setString(1, data.getChannel().getName());
                ps.setString(2, data.getParticipant() == null ? null : data.getParticipant().getID().toString());
                ps.setString(3, data.toText());
                DatabaseUtils.setTimestamp(ps, 4, data.getTimestamp(), database);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void delete(@Nonnull ChannelMessage<?> data) throws Exception {
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_DELETE))) {
                ps.setString(1, data.getChannel().getName());
                ps.setString(2, data.getParticipant() == null ? null : data.getParticipant().getID().toString());
                DatabaseUtils.setTimestamp(ps, 3, data.getTimestamp(), database);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Gets logs by {@code Channel} within the specified time range.
     *
     * @param channel the channel to filter logs by
     * @param from    the start time of the range
     * @param to      the end time of the range
     * @return a list of logs filtered by the specified channel and time range
     * @throws Exception if there is an issue retrieving the logs
     */
    @Nonnull
    public List<ChannelMessage<?>> getLogsByChannel(@Nonnull Channel channel, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) throws Exception {
        List<ChannelMessage<?>> logs = new ArrayList<>();
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_GET_BY_CHANNEL))) {
                ps.setString(1, channel.getName());
                DatabaseUtils.setTimestamp(ps, 2, from, database);
                DatabaseUtils.setTimestamp(ps, 3, to, database);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        logs.add(createChannelMessage(rs));
                    }
                }
            }
        }
        return logs;
    }

    /**
     * Gets logs by {@code Participant} within the specified time range.
     *
     * @param participant the participant to filter logs by
     * @param from        the start time of the range
     * @param to          the end time of the range
     * @return a list of logs filtered by the specified participant and time range
     * @throws Exception if there is an issue retrieving the logs
     */
    @Nonnull
    public List<ChannelMessage<?>> getLogsByParticipant(@Nonnull Participant participant, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) throws Exception {
        List<ChannelMessage<?>> logs = new ArrayList<>();
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_GET_BY_PARTICIPANT))) {
                ps.setString(1, participant.getID().toString());
                DatabaseUtils.setTimestamp(ps, 2, from, database);
                DatabaseUtils.setTimestamp(ps, 3, to, database);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        logs.add(createChannelMessage(rs));
                    }
                }
            }
        }
        return logs;
    }

    /**
     * Creates a {@code ChannelMessage} instance from a {@code ResultSet}.
     *
     * @param rs the ResultSet containing the data
     * @return a ChannelMessage instance with the data from the ResultSet
     * @throws Exception if there is an issue creating the ChannelMessage
     */
    @Nonnull
    private ChannelMessage<?> createChannelMessage(ResultSet rs) throws Exception {
        String channelName = rs.getString("channel_name");
        UUID participantID = rs.getString("participant_id") != null ? UUID.fromString(rs.getString("participant_id")) : null;
        String content = rs.getString("content");
        ZonedDateTime timestamp = Objects.requireNonNull(DatabaseUtils.getTimestamp(rs, "timestamp", database));

        Channel channel = plugin.getChannelManager().getChannel(channelName).orElseThrow();
        Participant participant = participantID == null ? null : plugin.getParticipantStorage().load(participantID);
        return new ChannelMessage<>(channel, participant, new TextMessage(content), timestamp);
    }
}
