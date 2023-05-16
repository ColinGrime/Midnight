package me.colingrimes.channels.channel.chatter;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class StandardChatter implements Chatter {

    private final UUID id;
    private final Set<UUID> ignored;
    private final ZonedDateTime joinDate;
    private boolean isMuted;
    private ZonedDateTime muteEndTime;
    private String nickname;
    private UUID lastMessagedBy;
    private ZonedDateTime lastSeen;

    /**
     * Creates a new chatter from the uuid.
     *
     * @param uuid the uuid to create the chatter from
     */
    public StandardChatter(@Nonnull UUID uuid) {
        this.id = uuid;
        this.isMuted = false;
        this.ignored = new HashSet<>();
        this.lastSeen = ZonedDateTime.now();
        this.joinDate = ZonedDateTime.now();
    }

    /**
     * Creates a new chatter from a player.
     *
     * @param player the player to create the chatter from
     */
    public StandardChatter(@Nonnull Player player) {
        this.id = player.getUniqueId();
        this.isMuted = false;
        this.ignored = new HashSet<>();
        this.nickname = player.getName();
        this.lastSeen = ZonedDateTime.now();
        this.joinDate = ZonedDateTime.now();
    }

    /**
     * Creates a chatter that was stored in a database.
     *
     * @param id the unique identifier of the chatter
     * @param muteEndTime the time the chatter's mute ends
     * @param isMuted whether the chatter is muted
     * @param nickname the chatter's nickname
     * @param lastMessagedBy the last person to message the chatter
     * @param lastSeen the last time the chatter was seen
     * @param joinDate the date the chatter joined
     */
    public StandardChatter(@Nonnull UUID id,
                           @Nullable ZonedDateTime muteEndTime,
                           boolean isMuted,
                           @Nonnull String nickname,
                           @Nullable UUID lastMessagedBy,
                           @Nonnull ZonedDateTime lastSeen,
                           @Nonnull ZonedDateTime joinDate) {
        this.id = id;
        this.ignored = new HashSet<>();
        this.muteEndTime = muteEndTime;
        this.isMuted = isMuted;
        this.nickname = nickname;
        this.lastMessagedBy = lastMessagedBy;
        this.lastSeen = lastSeen;
        this.joinDate = joinDate;
    }

    @Nonnull
    @Override
    public UUID getID() {
        return id;
    }

    @Nonnull
    @Override
    public String getName() {
        return player().getName();
    }

    @Nonnull
    @Override
    public Player player() {
        return Players.get(id).orElseThrow(() -> new IllegalStateException("Player is not online!"));
    }

    @Override
    public boolean online() {
        return Players.get(id).isPresent();
    }

    @Override
    public boolean hasPermission(@Nonnull String permission) {
        if (online()) {
            return player().hasPermission(permission);
        } else {
            return false;
        }
    }

    @Override
    public void send(@Nonnull Message<?> message) {
        if (online()) {
            Scheduler.SYNC.run(() -> message.send(player()));
        }
    }

    @Override
    public void send(@Nonnull String message) {
        if (online()) {
            Scheduler.SYNC.run(() -> player().sendMessage(message));
        }
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
    public boolean ignore(@Nonnull UUID uuid) {
        return ignored.add(uuid);
    }

    @Override
    public boolean unignore(@Nonnull UUID uuid) {
        return ignored.remove(uuid);
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

    @Nullable
    @Override
    public UUID getLastMessagedBy() {
        return lastMessagedBy;
    }

    @Override
    public void setLastMessagedBy(@Nonnull UUID uuid) {
        this.lastMessagedBy = uuid;
    }

    @Nonnull
    @Override
    public ZonedDateTime getLastSeen() {
        if (online()) {
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
