package me.colingrimes.midnight.channel.implementation;

import me.colingrimes.midnight.channel.Channel;
import me.colingrimes.midnight.channel.Participant;
import me.colingrimes.midnight.channel.util.ChannelPermission;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.implementation.ChannelMessage;
import me.colingrimes.midnight.util.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseChannel implements Channel {

    private final List<ChannelMessage<?>> logs;
    private final UUID id;
    private final String name;
    private final Set<Participant> participants;
    private final Map<Participant, ChatColor> chatColors;

    /**
     * Creates a new channel with a random UUID.
     * @param name the name of the channel
     */
    public BaseChannel(@Nonnull String name) {
        this.logs = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.name = name;
        this.participants = new HashSet<>();
        this.chatColors = new HashMap<>();
    }

    /**
     * Creates a channel that was stored in a database.
     * @param logs the logs of the channel
     * @param id the unique identifier of the channel
     * @param name the name of the channel
     * @param participants the participants in the channel
     * @param chatColors the chat colors of the participants in the channel
     */
    public BaseChannel(@Nonnull List<ChannelMessage<?>> logs, @Nonnull UUID id, @Nonnull String name, @Nonnull Set<Participant> participants, @Nonnull Map<Participant, ChatColor> chatColors) {
        this.logs = logs;
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.chatColors = chatColors;
    }

    @Nonnull
    @Override
    public List<ChannelMessage<?>> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    @Nonnull
    @Override
    public UUID getID() {
        return id;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void broadcast(@Nonnull Message<?> message) {
        participants.forEach(p -> p.send(message));
        logs.add(new ChannelMessage<>(this, null, message));
    }

    @Override
    public void send(@Nonnull Participant sender, @Nonnull Message<?> message) {
        if (hasPermission(sender, ChannelPermission.SEND)) {
            participants.forEach(p -> p.send(message));
            logs.add(new ChannelMessage<>(this, sender, message));
            sender.addLog(new ChannelMessage<>(this, sender, message));
        }
    }

    @Nonnull
    @Override
    public Set<Participant> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    @Override
    public boolean add(@Nonnull Participant participant) {
        if (hasPermission(participant, ChannelPermission.JOIN)) {
            return participants.add(participant);
        }
        return false;
    }

    @Override
    public boolean remove(@Nonnull Participant participant) {
        if (hasPermission(participant, ChannelPermission.LEAVE)) {
            return participants.remove(participant);
        }
        return false;
    }

    @Override
    public boolean contains(@Nonnull Player player) {
        return participants.stream().anyMatch(p -> p.player().equals(player));
    }

    @Nullable
    @Override
    public ChatColor getChatColor(@Nonnull Participant participant) {
        return chatColors.get(participant);
    }

    @Override
    public void setChatColor(@Nonnull Participant participant, @Nullable ChatColor color) {
        if (color == null) {
            chatColors.remove(participant);
        } else {
            chatColors.put(participant, color);
        }
    }
}
