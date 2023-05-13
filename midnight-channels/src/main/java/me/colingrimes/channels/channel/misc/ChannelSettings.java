package me.colingrimes.channels.channel.misc;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.filter.ChatFilter;
import me.colingrimes.channels.filter.ChatFilters;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents the settings for a communication channel.
 */
public class ChannelSettings implements ChatFilter {

    private final ChatFilters filters = new ChatFilters();
    private String messageFormat = "&7<&f&l{vault_prefix}&7> &e{sender}&7: &f{message}";
    private boolean logMessages = false;

    /**
     * Gets the chat filters for the channel.
     *
     * @return the chat filters
     */
    @Nonnull
    public ChatFilters getFilters() {
        return filters;
    }

    @Override
    public boolean filter(@Nonnull Message<?> message, @Nullable Chatter chatter) {
        return filters.filter(message, chatter);
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {message} and {sender} to be replaced by the
     * player's name and message, respectively. In addition, this
     * supports all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public String getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message) {
        return Placeholders.create(chatter.player())
                .add("{message}", message.toText())
                .add("{sender}", chatter.getName())
                .apply(messageFormat);
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {message} and {sender} to be replaced by the
     * player's name and message, respectively. In addition, this
     * supports all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public String getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message, @Nonnull String sender) {
        return Placeholders.create(chatter.player())
                .add("{message}", message.toText())
                .add("{sender}", sender)
                .apply(messageFormat);
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {message}, {sender}, {recipient} to be replaced
     * by the player's name and message, respectively. In addition, this
     * supports all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public String getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message, @Nonnull String sender, @Nonnull String recipient) {
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
     */
    public void setMessageFormat(@Nonnull String format) {
        this.messageFormat = format;
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
     */
    public void setLogMessages(boolean logMessages) {
        this.logMessages = logMessages;
    }
}
