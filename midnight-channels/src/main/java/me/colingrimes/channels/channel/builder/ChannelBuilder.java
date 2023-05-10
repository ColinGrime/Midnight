package me.colingrimes.channels.channel.builder;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.channels.channel.filter.ChatFilter;
import me.colingrimes.channels.channel.implementation.SimpleChannel;
import me.colingrimes.channels.channel.settings.ChannelPermission;
import me.colingrimes.channels.channel.settings.ChannelSettings;
import me.colingrimes.midnight.util.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ChannelBuilder is a builder class for creating and registering a {@link Channel}.
 */
public class ChannelBuilder {

    private final String name;
    private final ChannelSettings settings = new ChannelSettings();
    private final Set<ChatFilter> filters = new HashSet<>();
    private final Set<Participant> participants = new HashSet<>();
    private final Map<Participant, ChatColor> chatColors = new HashMap<>();

    /**
     * Static factory method for creating a new ChannelBuilder instance.
     *
     * @param name the name of the channel.
     * @return a new ChannelBuilder.
     */
    public static ChannelBuilder create(@Nonnull String name) {
        return new ChannelBuilder(name);
    }

    private ChannelBuilder(@Nonnull String name) {
        this.name = name;
    }

    /**
     * Adds a permission to the channel to be built.
     *
     * @param permission the permission to add.
     * @param node the permission node.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder permission(@Nonnull ChannelPermission permission, @Nonnull String node) {
        this.settings.setPermission(permission, node);
        return this;
    }

    /**
     * Sets the message format of the channel to be built.
     *
     * @param messageFormat the message format.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder messageFormat(@Nonnull String messageFormat) {
        this.settings.setMessageFormat(messageFormat);
        return this;
    }

    /**
     * Sets the chat color of the channel to be built.
     *
     * @param chatColor the chat color.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder chatColor(@Nonnull ChatColor chatColor) {
        this.settings.setChatColor(chatColor);
        return this;
    }

    /**
     * Sets whether messages in the channel to be built should be logged.
     *
     * @param logMessages whether to log messages.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder logMessages(boolean logMessages) {
        this.settings.setLogMessages(logMessages);
        return this;
    }

    /**
     * Adds a chat filter to the channel to be built.
     *
     * @param filter the chat filter to add.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder filter(@Nonnull ChatFilter filter) {
        this.filters.add(filter);
        return this;
    }

    /**
     * Adds a participant to the channel to be built.
     *
     * @param participant the participants to add.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder participant(@Nonnull Participant...participant) {
        this.participants.addAll(Set.of(participant));
        return this;
    }

    /**
     * Adds a chat color for a participant in the channel to be built.
     *
     * @param participant the participant.
     * @param color       the chat color for the participant.
     * @return this ChannelBuilder.
     */
    @Nonnull
    public ChannelBuilder chatColor(@Nonnull Participant participant, @Nonnull ChatColor color) {
        this.chatColors.put(participant, color);
        return this;
    }

    /**
     * Constructs a new Channel instance and registers it with the ChannelAPI.
     *
     * @return the newly constructed Channel.
     */
    @Nonnull
    public Channel build() {
        Channel channel = new SimpleChannel(name, settings, filters, participants, chatColors);
        ChannelAPI.getManager().registerChannel(channel);
        return channel;
    }
}
