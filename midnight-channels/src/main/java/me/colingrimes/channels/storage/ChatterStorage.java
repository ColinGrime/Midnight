package me.colingrimes.channels.storage;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.chatter.StandardChatter;
import me.colingrimes.channels.storage.exception.ChatterNotFoundException;
import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.io.DatabaseUtils;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class ChatterStorage extends SqlStorage<Chatter> {

    private static final String CHATTERS_SAVE = "INSERT INTO 'chatters' (id, mute_end_time, is_muted, nickname, last_messaged_by, last_seen, join_date) VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET mute_end_time = EXCLUDED.mute_end_time, is_muted = EXCLUDED.is_muted, nickname = EXCLUDED.nickname, last_messaged_by = EXCLUDED.last_messaged_by, last_seen = EXCLUDED.last_seen, join_date = EXCLUDED.join_date";
    private static final String CHATTERS_IGNORED_SAVE = "INSERT INTO 'chatters_ignored' (chatter_id, ignored_id) VALUES (?, ?) ON CONFLICT (chatter_id, ignored_id) DO NOTHING";
    private static final String CHATTERS_DELETE = "DELETE FROM 'chatters' WHERE id = ?";
    private static final String CHATTERS_LOAD = "SELECT * FROM 'chatters' WHERE id = ?";
    private static final String CHATTERS_IGNORED_LOAD = "SELECT ignored_id FROM 'chatters_ignored' WHERE chatter_id = ?";

    private final MidnightChannels plugin;

    public ChatterStorage(@Nonnull MidnightChannels plugin, @Nonnull ConnectionProvider connectionProvider) {
        super(connectionProvider);
        this.plugin = plugin;
    }

    @Override
    public void save(@Nonnull Chatter data) throws Exception {
        try (Connection c = provider.getConnection()) {
            // Save to 'chatters' table.
            try (PreparedStatement ps = c.prepareStatement(processor.apply(CHATTERS_SAVE))) {
                DatabaseUtils.setUUID(ps, 1, data.getID(), database);
                DatabaseUtils.setTimestamp(ps, 2, data.getMuteEndTime(), database);
                ps.setBoolean(3, data.isMuted());
                ps.setString(4, data.getNickname());
                DatabaseUtils.setUUID(ps, 5, data.getLastMessagedBy(), database);
                DatabaseUtils.setTimestamp(ps, 6, data.getLastSeen(), database);
                DatabaseUtils.setTimestamp(ps, 7, data.getJoinDate(), database);
                ps.executeUpdate();
            }

            // Save to 'chatters_ignored' table
            for (UUID ignored : data.getIgnored()) {
                try (PreparedStatement ps = c.prepareStatement(processor.apply(CHATTERS_IGNORED_SAVE))) {
                    DatabaseUtils.setUUID(ps, 1, data.getID(), database);
                    DatabaseUtils.setUUID(ps, 2, ignored, database);
                    ps.executeUpdate();
                }
            }
        }
    }

    @Override
    public void delete(@Nonnull Chatter data) throws Exception {
        try (Connection c = provider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(processor.apply(CHATTERS_DELETE))) {
                DatabaseUtils.setUUID(ps, 1, data.getID(), database);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Loads the {@code Chatter} data for a given player.
     *
     * @param uuid the UUID of the player whose data needs to be loaded
     * @return the loaded Chatter object
     * @throws Exception if an error occurs during the database operation
     */
    @Nonnull
    public Chatter load(@Nonnull UUID uuid) throws Exception {
        if (plugin.getChatManager().getChatter(uuid).isPresent()) {
            return plugin.getChatManager().getChatter(uuid).get();
        }

        Chatter chatter;
        try (Connection c = provider.getConnection()) {
            chatter = loadChatterData(c, uuid);
            loadChatterIgnoredData(c, chatter);
        } catch (ChatterNotFoundException e) {
            chatter = new StandardChatter(uuid);
            save(chatter);
        }

        // Add them to the channel manager.
        plugin.getChatManager().registerChatter(chatter);
        return chatter;
    }

    /**
     * Loads the main {@code Chatter} data from the 'chatters' table.
     *
     * @param connection the database connection
     * @param uuid       the UUID of the player whose data needs to be loaded
     * @return the loaded Chatter object without channel and ignored chatter data
     * @throws Exception if an error occurs during the database operation
     */
    private Chatter loadChatterData(@Nonnull Connection connection, @Nonnull UUID uuid) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(processor.apply(CHATTERS_LOAD))) {
            DatabaseUtils.setUUID(ps, 1, uuid, database);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    UUID id = DatabaseUtils.getUUID(resultSet, "id", database);
                    Instant muteEndTime = DatabaseUtils.getTimestamp(resultSet, "mute_end_time", database);
                    boolean isMuted = resultSet.getBoolean("is_muted");
                    String nickname = resultSet.getString("nickname");
                    UUID lastMessagedBy = DatabaseUtils.getUUID(resultSet, "last_messaged_by", database);
                    Instant lastSeen = Objects.requireNonNull(DatabaseUtils.getTimestamp(resultSet, "last_seen", database));
                    Instant joinDate = Objects.requireNonNull(DatabaseUtils.getTimestamp(resultSet, "join_date", database));
                    return new StandardChatter(Objects.requireNonNull(id), muteEndTime, isMuted, nickname, lastMessagedBy, lastSeen, joinDate);
                } else {
                    throw new ChatterNotFoundException("Chatter not found for UUID: " + uuid);
                }
            }
        }
    }

    /**
     * Loads ignored chatter data from the 'ignored_chatters' table.
     *
     * @param connection  the database connection
     * @param chatter the chatter object to be updated with the loaded data
     * @throws Exception if an error occurs during the database operation
     */
    private void loadChatterIgnoredData(@Nonnull Connection connection, @Nonnull Chatter chatter) throws Exception {
        try (PreparedStatement ps = connection.prepareStatement(processor.apply(CHATTERS_IGNORED_LOAD))) {
            DatabaseUtils.setUUID(ps, 1, chatter.getID(), database);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    UUID ignoredID = DatabaseUtils.getUUID(resultSet, "ignored_id", database);
                    chatter.ignore(Objects.requireNonNull(ignoredID));
                }
            }
        }
    }
}
