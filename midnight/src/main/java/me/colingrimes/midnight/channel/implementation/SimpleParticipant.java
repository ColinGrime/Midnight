package me.colingrimes.midnight.channel.implementation;

import me.colingrimes.midnight.channel.Channel;
import me.colingrimes.midnight.channel.Participant;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.implementation.ChannelMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class SimpleParticipant implements Participant {

    private final List<ChannelMessage<?>> logs;
    private final UUID id;
    private final Player player;
    private final List<Channel> channels;
    private final Set<UUID> ignored;
    private final ZonedDateTime joinedDate;
    private Channel activeChannel;
    private boolean isMuted;
    private ZonedDateTime muteEndTime;
    private String nickname;
    private ZonedDateTime lastSeen;

    /**
     * Creates a new participant from a player.
     * @param player the player to create the participant from
     */
    public SimpleParticipant(@Nonnull Player player) {
        this.logs = new ArrayList<>();
        this.id = player.getUniqueId();
        this.player = player;
        this.channels = new ArrayList<>();
        this.ignored = new HashSet<>();
        this.joinedDate = ZonedDateTime.now();
        this.isMuted = false;
        this.nickname = player.getName();
        this.lastSeen = ZonedDateTime.now();
    }

    /**
     * Creates a participant that was stored in a database.
     * @param logs the logs of the participant
     * @param id the unique identifier of the participant
     * @param player the player associated with the participant
     * @param channels the channels the participant is in
     * @param ignored the participants the participant is ignoring
     * @param joinedDate the date the participant joined
     * @param activeChannel the participant's active channel
     * @param isMuted whether the participant is muted
     * @param muteEndTime the time the participant's mute ends
     * @param nickname the participant's nickname
     * @param lastSeen the last time the participant was seen
     */
    public SimpleParticipant(@Nonnull List<ChannelMessage<?>> logs, @Nonnull UUID id, @Nonnull Player player, @Nonnull List<Channel> channels, @Nonnull Set<UUID> ignored, @Nonnull ZonedDateTime joinedDate, @Nonnull Channel activeChannel, boolean isMuted, @Nonnull ZonedDateTime muteEndTime, @Nonnull String nickname, @Nonnull ZonedDateTime lastSeen) {
        this.logs = logs;
        this.id = id;
        this.player = player;
        this.channels = channels;
        this.ignored = ignored;
        this.joinedDate = joinedDate;
        this.activeChannel = activeChannel;
        this.isMuted = isMuted;
        this.muteEndTime = muteEndTime;
        this.nickname = nickname;
        this.lastSeen = lastSeen;
    }

    @Nonnull
    @Override
    public List<ChannelMessage<?>> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    @Override
    public void addLog(@Nonnull ChannelMessage<?> log) {
        logs.add(log);
    }

    @Nonnull
    @Override
    public UUID getID() {
        return id;
    }

    @Nonnull
    @Override
    public String getName() {
        return nickname != null ? nickname : player.getName();
    }

    @Nonnull
    @Override
    public Player player() {
        return player;
    }

    @Override
    public void send(@Nonnull Message<?> message) {
        message.send(this);
    }

    @Nonnull
    @Override
    public List<Channel> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    @Override
    public void addChannel(@Nonnull Channel channel) {
        channels.add(channel);
    }

    @Override
    public void removeChannel(@Nonnull Channel channel) {
        channels.remove(channel);
    }

    @Nonnull
    @Override
    public Channel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(@Nonnull Channel channel) {
        this.activeChannel = channel;
    }

    @Override
    public boolean isMuted() {
        if (isMuted && muteEndTime.isBefore(ZonedDateTime.now())) {
            isMuted = false;
        }
        return isMuted;
    }

    @Override
    public void mute(@Nonnull Duration duration) {
        isMuted = true;
        muteEndTime = ZonedDateTime.now().plus(duration);
    }

    @Override
    public void unmute() {
        isMuted = false;
    }

    @Nonnull
    @Override
    public Set<UUID> getIgnored() {
        return Collections.unmodifiableSet(ignored);
    }

    @Override
    public boolean isIgnoring(@Nonnull Participant participant) {
        return ignored.contains(participant.getID());
    }

    @Override
    public void ignore(@Nonnull Participant participant) {
        ignored.add(participant.getID());
    }
    @Override
    public void unignore(@Nonnull Participant participant) {
        ignored.remove(participant.getID());
    }

    @Nonnull
    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(@Nonnull String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Nonnull
    @Override
    public ZonedDateTime getLastSeen() {
        if (isOnline()) {
            lastSeen = ZonedDateTime.now();
        }
        return lastSeen;
    }

    @Nonnull
    @Override
    public ZonedDateTime getJoinedDate() {
        return joinedDate;
    }
}
