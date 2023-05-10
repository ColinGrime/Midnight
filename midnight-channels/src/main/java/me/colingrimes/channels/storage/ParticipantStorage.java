package me.colingrimes.channels.storage;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.channels.channel.implementation.SimpleParticipant;
import me.colingrimes.channels.storage.exception.ParticipantNotFoundException;
import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.bukkit.ChatColor;
import me.colingrimes.midnight.util.io.DatabaseUtils;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class ParticipantStorage extends SqlStorage<Participant> {

    private static final String PARTICIPANTS_SAVE = "INSERT INTO 'participants' (id, active_channel, is_muted, mute_end_time, nickname, last_seen, join_date) VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET active_channel = EXCLUDED.active_channel, is_muted = EXCLUDED.is_muted, mute_end_time = EXCLUDED.mute_end_time, nickname = EXCLUDED.nickname, last_seen = EXCLUDED.last_seen, join_date = EXCLUDED.join_date";
    private static final String PARTICIPANT_CHANNELS_SAVE = "INSERT INTO participant_channels (participant_id, channel_name, chat_color) VALUES (?, ?, ?) ON CONFLICT (participant_id, channel_name) DO UPDATE SET chat_color = EXCLUDED.chat_color";
    private static final String IGNORED_PARTICIPANTS_SAVE = "INSERT INTO ignored_participants (participant_id, ignored_id) VALUES (?, ?) ON CONFLICT (participant_id, ignored_id) DO NOTHING";
    private static final String PARTICIPANTS_DELETE = "DELETE FROM 'participants' WHERE id = ?";
    private static final String PARTICIPANTS_LOAD = "SELECT * FROM 'participants' WHERE id = ?";
    private static final String PARTICIPANT_CHANNELS_LOAD = "SELECT channel_name, chat_color FROM 'participant_channels' WHERE participant_id = ?";
    private static final String IGNORED_PARTICIPANTS_LOAD = "SELECT ignored_id FROM 'ignored_participants' WHERE participant_id = ?";

    private final MidnightChannels plugin;

    public ParticipantStorage(@Nonnull MidnightChannels plugin, @Nonnull ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.plugin = plugin;
    }

    @Override
    public void save(@Nonnull Participant data) throws Exception {
        try (Connection c = provider.getConnection()) {
            // Save to 'participants' table.
            try (PreparedStatement ps = c.prepareStatement(processor.apply(PARTICIPANTS_SAVE))) {
                ps.setString(1, data.getID().toString());
                ps.setString(2, data.getActiveChannel() != null ? data.getActiveChannel().getName() : null);
                ps.setBoolean(3, data.isMuted());
                DatabaseUtils.setTimestamp(ps, 4, data.getMuteEndTime(), database);
                ps.setString(5, data.getNickname());
                DatabaseUtils.setTimestamp(ps, 6, data.getLastSeen(), database);
                DatabaseUtils.setTimestamp(ps, 7, data.getJoinDate(), database);
                ps.executeUpdate();
            }

            // Save to 'participant_channels' table.
            for (Channel channel : data.getChannels()) {
                try (PreparedStatement ps = c.prepareStatement(processor.apply(PARTICIPANT_CHANNELS_SAVE))) {
                    ps.setString(1, data.getID().toString());
                    ps.setString(2, channel.getName());
                    ChatColor color = channel.getChatColor(data);
                    ps.setString(3, color != null ? color.name() : null);
                    ps.executeUpdate();
                }
            }

            // Save to 'ignored_participants' table
            for (UUID ignored : data.getIgnored()) {
                try (PreparedStatement ps = c.prepareStatement(processor.apply(IGNORED_PARTICIPANTS_SAVE))) {
                    ps.setString(1, data.getID().toString());
                    ps.setString(2, ignored.toString());
                    ps.executeUpdate();
                }
            }
        }
    }

    @Override
    public void delete(@Nonnull Participant data) throws Exception {
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(PARTICIPANTS_DELETE))) {
                ps.setString(1, data.getID().toString());
                ps.executeUpdate();
            }
        }
    }

    /**
     * Loads the {@code Participant} data for a given player.
     *
     * @param uuid the UUID of the player whose data needs to be loaded
     * @return the loaded Participant object
     * @throws Exception if an error occurs during the database operation
     */
    @Nonnull
    public Participant load(@Nonnull UUID uuid) throws Exception {
        if (plugin.getChannelManager().getParticipant(uuid).isPresent()) {
            return plugin.getChannelManager().getParticipant(uuid).get();
        }

        Participant participant;
        try (Connection c = provider.getConnection()) {
            participant = loadParticipantData(c, uuid);
            loadParticipantChannelsData(c, participant);
            loadIgnoredParticipantsData(c, participant);
        } catch (ParticipantNotFoundException e) {
            participant = new SimpleParticipant(uuid);
        }

        // Add them to the channel manager.
        plugin.getChannelManager().registerParticipant(participant);
        return participant;
    }

    /**
     * Loads the main {@code Participant} data from the 'participants' table.
     *
     * @param connection the database connection
     * @param uuid       the UUID of the player whose data needs to be loaded
     * @return the loaded Participant object without channel and ignored participant data
     * @throws Exception if an error occurs during the database operation
     */
    private Participant loadParticipantData(@Nonnull Connection connection, @Nonnull UUID uuid) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(processor.apply(PARTICIPANTS_LOAD))) {
            ps.setString(1, uuid.toString());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    String activeChannelName = resultSet.getString("active_channel");
                    boolean isMuted = resultSet.getBoolean("is_muted");
                    ZonedDateTime muteEndTime = DatabaseUtils.getTimestamp(resultSet, "mute_end_time", database);
                    String nickname = resultSet.getString("nickname");
                    ZonedDateTime lastSeen = Objects.requireNonNull(DatabaseUtils.getTimestamp(resultSet, "last_seen", database));
                    ZonedDateTime joinDate = Objects.requireNonNull(DatabaseUtils.getTimestamp(resultSet, "join_date", database));

                    Channel activeChannel = plugin.getChannelManager().getChannel(activeChannelName).orElseThrow();
                    return new SimpleParticipant(id, new ArrayList<>(), new HashSet<>(), joinDate, activeChannel, isMuted, muteEndTime, nickname, lastSeen);
                } else {
                    throw new ParticipantNotFoundException("Participant not found for UUID: " + uuid);
                }
            }
        }
    }

    /**
     * Loads participant channel data from the 'participant_channels' table.
     *
     * @param connection  the database connection
     * @param participant the participant object to be updated with the loaded data
     * @throws Exception if an error occurs during the database operation
     */
    private void loadParticipantChannelsData(@Nonnull Connection connection, @Nonnull Participant participant) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(processor.apply(PARTICIPANT_CHANNELS_LOAD))) {
            ps.setString(1, participant.getID().toString());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String channelName = resultSet.getString("channel_name");
                    String chatColor = resultSet.getString("chat_color");

                    Channel channel = plugin.getChannelManager().getChannel(channelName).orElseThrow();
                    channel.add(participant);
                    channel.setChatColor(participant, ChatColor.fromString(chatColor));
                    participant.addChannel(channel);
                }
            }
        }
    }

    /**
     * Loads ignored participant data from the 'ignored_participants' table.
     *
     * @param connection  the database connection
     * @param participant the participant object to be updated with the loaded data
     * @throws Exception if an error occurs during the database operation
     */
    private void loadIgnoredParticipantsData(@Nonnull Connection connection, @Nonnull Participant participant) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(processor.apply(IGNORED_PARTICIPANTS_LOAD))) {
            ps.setString(1, participant.getID().toString());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    UUID ignoredID = UUID.fromString(resultSet.getString("ignored_id"));
                    participant.ignore(ignoredID);
                }
            }
        }
    }
}
