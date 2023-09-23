package me.colingrimes.channels.storage;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.message.ChannelLog;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.implementation.TextMessage;
import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.io.DatabaseUtils;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatLogStorage extends SqlStorage<ChannelMessage<?>> {

    private static final String LOGS_SAVE = "INSERT INTO 'channel_logs' (channel_name, chatter_id, content, timestamp) VALUES (?, ?, ?, ?)";
    private static final String LOGS_DELETE = "DELETE FROM 'channel_logs' WHERE channel_name = ? AND chatter_id = ? AND timestamp = ?";
    private static final String LOGS_GET_BY_CHANNEL = "SELECT * FROM 'channel_logs' WHERE channel_name = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";
    private static final String LOGS_GET_BY_CHATTER = "SELECT * FROM 'channel_logs' WHERE chatter_id = ? AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";

    private final MidnightChannels plugin;

    public ChatLogStorage(@Nonnull MidnightChannels plugin, @Nonnull ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.plugin = plugin;
    }

    @Override
    public void save(@Nonnull ChannelMessage<?> data) throws Exception {
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_SAVE))) {
                ps.setString(1, data.getChannel().getName());
                DatabaseUtils.setUUID(ps, 2, data.getChatter().isPresent() ? data.getChatter().get().getID() : null, database);
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
                DatabaseUtils.setUUID(ps, 2, data.getChatter().isPresent() ? data.getChatter().get().getID() : null, database);
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
    public List<ChannelLog<?>> getLogsByChannel(@Nonnull Channel channel, @Nonnull Instant from, @Nonnull Instant to) throws Exception {
        List<ChannelLog<?>> logs = new ArrayList<>();
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_GET_BY_CHANNEL))) {
                ps.setString(1, channel.getName());
                DatabaseUtils.setTimestamp(ps, 2, from, database);
                DatabaseUtils.setTimestamp(ps, 3, to, database);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        logs.add(createLog(rs));
                    }
                }
            }
        }
        return logs;
    }

    /**
     * Gets logs by {@code Chatter} within the specified time range.
     *
     * @param chatter the chatter to filter logs by
     * @param from    the start time of the range
     * @param to      the end time of the range
     * @return a list of logs filtered by the specified chatter and time range
     * @throws Exception if there is an issue retrieving the logs
     */
    @Nonnull
    public List<ChannelLog<?>> getLogsByChatter(@Nonnull Chatter chatter, @Nonnull Instant from, @Nonnull Instant to) throws Exception {
        List<ChannelLog<?>> logs = new ArrayList<>();
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(LOGS_GET_BY_CHATTER))) {
                DatabaseUtils.setUUID(ps, 1, chatter.getID(), database);
                DatabaseUtils.setTimestamp(ps, 2, from, database);
                DatabaseUtils.setTimestamp(ps, 3, to, database);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        logs.add(createLog(rs));
                    }
                }
            }
        }
        return logs;
    }

    /**
     * Creates a {@code ChannelLog} instance from a {@code ResultSet}.
     *
     * @param rs the ResultSet containing the data
     * @return a ChannelLog instance with the data from the ResultSet
     * @throws Exception if there is an issue creating the ChannelLog
     */
    @Nonnull
    private ChannelLog<?> createLog(ResultSet rs) throws Exception {
        String channelName = rs.getString("channel_name");
        UUID chatterID = DatabaseUtils.getUUID(rs, "chatter_id", database);
        String content = rs.getString("content");
        Instant timestamp = Objects.requireNonNull(DatabaseUtils.getTimestamp(rs, "timestamp", database));

        Chatter chatter = chatterID == null ? null : plugin.getChatterStorage().load(chatterID);
        return new ChannelLog<>(channelName, chatter, new TextMessage(content), timestamp);
    }
}
