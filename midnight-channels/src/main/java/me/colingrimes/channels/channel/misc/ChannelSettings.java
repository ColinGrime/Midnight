package me.colingrimes.channels.channel.misc;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.filter.ChatFilter;
import me.colingrimes.channels.filter.ChatFilterType;
import me.colingrimes.channels.filter.ChatFilters;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;

/**
 * Represents the settings for a communication channel.
 */
public class ChannelSettings implements ChatFilter {

    private final ChatFilters filters = new ChatFilters();
    private Message<?> messageFormat = Message.of("&7<&f&lMember&7> &e{sender}&7: &f{message}");
    private boolean logMessages = false;

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        return filters.filter(message);
    }

    @Override
    public boolean filter(@Nonnull String message) {
        return filters.filter(message);
    }

    /**
     * Adds the chat filter types for the channel.
     *
     * @param types the types of filters to add
     * @return the channel settings
     */
    @Nonnull
    public ChannelSettings addFilters(@Nonnull ChatFilterType... types) {
        this.filters.add(types);
        return this;
    }

    /**
     * Adds the specified custom filters.
     *
     * @param filters the custom filters to add
     * @return this instance
     */
    @Nonnull
    public ChannelSettings addFilters(@Nonnull ChatFilter... filters) {
        this.filters.add(filters);
        return this;
    }

    /**
     * Gets the message format for the channel.
     *
     * @return the message format
     */
    @Nonnull
    public Message<?> getMessageFormat() {
        return messageFormat;
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {message} and {sender} to be replaced by the
     * message and sender, respectively. In addition, this supports
     * all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public Message<?> getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message) {
        return getFormattedMessage(chatter, message, chatter.getName());
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {message} and {sender} to be replaced by the
     * message and sender, respectively. In addition, this supports
     * all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public Message<?> getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message, @Nonnull String sender) {
        return Placeholders.create(chatter.player())
                .add("{message}", message.toText())
                .add("{sender}", sender)
                .apply(messageFormat);
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {message}, {sender}, {recipient} to be replaced
     * by the message, sender, and recipient, respectively. In addition, this
     * supports all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public Message<?> getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message, @Nonnull String sender, @Nonnull String recipient) {
        return Placeholders.create(chatter.player())
                .add("{message}", message.toText())
                .add("{sender}", sender)
                .add("{recipient}", recipient)
                .apply(messageFormat);
    }

    /**
     * Sets the message format for the channel.
     *
     * @param format the message format
     * @return the channel settings
     */
    @Nonnull
    public ChannelSettings setMessageFormat(@Nonnull Message<?> format) {
        this.messageFormat = format;
        return this;
    }

    /**
     * Logs the message sent in the channel if enabled.
     */
    public void logMessage(@Nonnull ChannelMessage<?> message) {
        if (!logMessages) {
            return;
        }

        Scheduler.ASYNC.execute(() -> {
            MidnightChannels.getInstance().getChatLogStorage().save(message);
        }).exceptionally((e) -> {
            Logger.severe("Failed to save message to database: " + message.toText());
            e.printStackTrace();
            return null;
        });
    }

    /**
     * Sets whether to log messages sent in the channel.
     *
     * @param logMessages whether to log messages
     * @return the channel settings
     */
    @Nonnull
    public ChannelSettings setLogMessages(boolean logMessages) {
        this.logMessages = logMessages;
        return this;
    }
}
