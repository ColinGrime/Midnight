package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.channels.channel.filter.ChatFilter;
import me.colingrimes.channels.channel.settings.ChannelPermission;
import me.colingrimes.channels.channel.settings.ChannelSettings;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.bukkit.ChatColor;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class SimpleChannel implements Channel {

    private final String name;
    private final ChannelSettings settings;
    private final Set<ChatFilter> filters;
    private final Set<Participant> participants;
    private final Map<Participant, ChatColor> chatColors;

    /**
     * Creates a new channel with the given name.
     *
     * @param name the name of the channel
     */
    public SimpleChannel(@Nonnull String name) {
        this.name = name;
        this.settings = new ChannelSettings();
        this.filters = new HashSet<>();
        this.participants = new HashSet<>();
        this.chatColors = new HashMap<>();
    }

    /**
     * Creates a new channel with all the given parameters.
     *
     * @param name         the name of the channel
     * @param settings     the settings of the channel
     * @param filters      the filters of the channel
     * @param participants the participants of the channel
     * @param chatColors   the chat colors of the channel
     */
    public SimpleChannel(@Nonnull String name, @Nonnull ChannelSettings settings, @Nonnull Set<ChatFilter> filters, @Nonnull Set<Participant> participants, @Nonnull Map<Participant, ChatColor> chatColors) {
        this.name = name;
        this.settings = settings;
        this.filters = filters;
        this.participants = participants;
        this.chatColors = chatColors;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }
    @Nonnull
    @Override
    public ChannelSettings getSettings() {
        return settings;
    }

    @Nonnull
    @Override
    public Set<ChatFilter> getFilters() {
        return filters;
    }

    @Override
    public void addFilter(@Nonnull ChatFilter filter) {
        filters.add(filter);
    }

    @Override
    public void removeFilter(@Nonnull ChatFilter filter) {
        filters.remove(filter);
    }

    @Override
    public void broadcast(@Nonnull Message<?> message) {
        participants.forEach(p -> p.send(message));
        if (settings.logMessages()) {
            Scheduler.ASYNC.execute(() -> {
                ChannelMessage<?> channelMessage = new ChannelMessage<>(this, null, message);
                MidnightChannels.getInstance().getChatStorage().save(channelMessage);
            }).exceptionally((e) -> {
                Logger.severe("Failed to save message to database: " + message.toText());
                e.printStackTrace();
                return null;
            });
        }
    }

    @Override
    public void broadcast(@Nonnull String message) {
        broadcast(Message.of(message));
    }

    @Override
    public void send(@Nonnull Participant sender, @Nonnull Message<?> message) {
        if (!hasPermission(sender, ChannelPermission.SEND)) {
            return;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        for (ChatFilter filter : filters) {
            if (!filter.filter(channelMessage)) {
                return;
            }
        }

        participants.forEach(p -> p.send(settings.getFormattedMessage(sender, message)));
        if (settings.logMessages()) {
            Scheduler.ASYNC.execute(() -> {
                MidnightChannels.getInstance().getChatStorage().save(channelMessage);
            }).exceptionally((e) -> {
                Logger.severe("Failed to save message to database: " + message.toText());
                e.printStackTrace();
                return null;
            });
        }
    }

    @Override
    public void send(@Nonnull Participant sender, @Nonnull String message) {
        send(sender, Message.of(message));
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
    public boolean contains(@Nonnull Participant participant) {
        return participants.contains(participant);
    }

    @Override
    public boolean hasPermission(@Nonnull Participant participant, @Nonnull ChannelPermission permission) {
        String perm = settings.getPermissions().get(permission);
        if (perm == null) {
            return true;
        } else if (!participant.isOnline()) {
            return false;
        } else {
            return participant.player().hasPermission(perm);
        }
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
