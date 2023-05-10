package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class SimpleParticipant implements Participant {

    private final UUID id;
    private final List<Channel> channels;
    private final Set<UUID> ignored;
    private final ZonedDateTime joinDate;
    private Channel activeChannel;
    private boolean isMuted;
    private ZonedDateTime muteEndTime;
    private String nickname;
    private ZonedDateTime lastSeen;

    /**
     * Creates a new participant from the uuid.
     *
     * @param uuid the uuid to create the participant from
     */
    public SimpleParticipant(@Nonnull UUID uuid) {
        this.id = uuid;
        this.channels = new ArrayList<>();
        this.ignored = new HashSet<>();
        this.joinDate = ZonedDateTime.now();
        this.isMuted = false;
        this.lastSeen = ZonedDateTime.now();
    }

    /**
     * Creates a new participant from a player.
     *
     * @param player the player to create the participant from
     */
    public SimpleParticipant(@Nonnull Player player) {
        this.id = player.getUniqueId();
        this.channels = new ArrayList<>();
        this.ignored = new HashSet<>();
        this.joinDate = ZonedDateTime.now();
        this.isMuted = false;
        this.nickname = player.getName();
        this.lastSeen = ZonedDateTime.now();
    }

    /**
     * Creates a participant that was stored in a database.
     *
     * @param id the unique identifier of the participant
     * @param channels the channels the participant is in
     * @param ignored the participants the participant is ignoring
     * @param joinDate the date the participant joined
     * @param activeChannel the participant's active channel
     * @param isMuted whether the participant is muted
     * @param muteEndTime the time the participant's mute ends
     * @param nickname the participant's nickname
     * @param lastSeen the last time the participant was seen
     */
    public SimpleParticipant(@Nonnull UUID id,
                             @Nonnull List<Channel> channels,
                             @Nonnull Set<UUID> ignored,
                             @Nonnull ZonedDateTime joinDate,
                             @Nonnull Channel activeChannel,
                             boolean isMuted,
                             @Nullable ZonedDateTime muteEndTime,
                             @Nonnull String nickname,
                             @Nonnull ZonedDateTime lastSeen) {
        this.id = id;
        this.channels = channels;
        this.ignored = ignored;
        this.joinDate = joinDate;
        this.activeChannel = activeChannel;
        this.isMuted = isMuted;
        this.muteEndTime = muteEndTime;
        this.nickname = nickname;
        this.lastSeen = lastSeen;
    }

    @Nonnull
    @Override
    public UUID getID() {
        return id;
    }

    @Nonnull
    @Override
    public String getName() {
        return nickname != null ? nickname : player().getName();
    }

    @Nonnull
    @Override
    public Player player() {
        return Players.get(id).orElseThrow(() -> new IllegalStateException("Player is not online!"));
    }

    @Override
    public void send(@Nonnull Message<?> message) {
        if (isOnline()) {
            Scheduler.SYNC.run(() -> message.send(player()));
        }
    }

    @Override
    public void send(@Nonnull String message) {
        if (isOnline()) {
            Scheduler.SYNC.run(() -> player().sendMessage(message));
        }
    }

    @Nonnull
    @Override
    public List<Channel> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    @Override
    public void addChannel(@Nonnull Channel channel) {
        if (!channels.contains(channel)) {
            channels.add(channel);
        }
    }

    @Override
    public void removeChannel(@Nonnull Channel channel) {
        channels.remove(channel);
    }

    @Nullable
    @Override
    public Channel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(@Nullable Channel channel) {
        this.activeChannel = channel;
    }

    @Nullable
    @Override
    public ZonedDateTime getMuteEndTime() {
        return muteEndTime;
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
    public boolean isIgnoring(@Nonnull UUID uuid) {
        return ignored.contains(uuid);
    }

    @Override
    public void ignore(@Nonnull UUID uuid) {
        ignored.add(uuid);
    }

    @Override
    public void unignore(@Nonnull UUID uuid) {
        ignored.remove(uuid);
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
        return Players.getNullable(id) != null;
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
    public ZonedDateTime getJoinDate() {
        return joinDate;
    }
}
