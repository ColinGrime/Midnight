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
    private Player player;
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
        this.activeChannel = MidnightTemp.getInstance().getGlobalChannel();
        this.nickname = player.getName();
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
        this.player = player;
        this.activeChannel = MidnightTemp.getInstance().getGlobalChannel();
        this.nickname = player.getName();
        this.lastSeen = ZonedDateTime.now();
    }

    /**
     * Creates a participant that was stored in a database.
     *
     * @param id the unique identifier of the participant
     * @param channels the channels the participant is in
     * @param ignored the participants the participant is ignoring
     * @param joinedDate the date the participant joined
     * @param player the player associated with the participant
     * @param activeChannel the participant's active channel
     * @param isMuted whether the participant is muted
     * @param muteEndTime the time the participant's mute ends
     * @param nickname the participant's nickname
     * @param lastSeen the last time the participant was seen
     */
    public SimpleParticipant(@Nonnull UUID id,
                             @Nonnull List<Channel> channels,
                             @Nonnull Set<UUID> ignored,
                             @Nonnull ZonedDateTime joinedDate,
                             @Nullable Player player,
                             @Nonnull Channel activeChannel,
                             boolean isMuted,
                             @Nonnull ZonedDateTime muteEndTime,
                             @Nonnull String nickname,
                             @Nonnull ZonedDateTime lastSeen) {
        this.id = id;
        this.channels = channels;
        this.ignored = ignored;
        this.joinDate = joinedDate;
        this.player = player;
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
        return nickname != null ? nickname : player.getName();
    }

    @Nonnull
    @Override
    public Player player() {
        if (player != null) {
            return player;
        }

        Players.get(id).ifPresent(p -> player = p);
        return player;
    }

    @Override
    public void send(@Nonnull Message<?> message) {
        Scheduler.SYNC.run(() -> message.send(this));
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

    @Nonnull
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
    public ZonedDateTime getJoinDate() {
        return joinDate;
    }
}
