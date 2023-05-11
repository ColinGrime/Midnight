package me.colingrimes.channels.channel.misc;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the settings for a communication channel.
 */
public class ChannelSettings {

    private final List<ChatFilter> filters = new ArrayList<>();
    private String messageFormat = "&7<&f&l{vault_prefix}&7> &e{player}&7: &f{message}";
    private boolean logMessages = false;

    /**
     * Filters a channel message based on its content.
     *
     * @param message the message to filter
     * @return whether the message should be filtered
     */
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        for (ChatFilter filter : filters) {
            if (filter.filter(message)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a filter to the channel.
     *
     * @param filter the filter to add
     */
    public void addFilter(@Nonnull ChatFilter filter) {
        filters.add(filter);
    }

    /**
     * Gets the formatted message for the channel. The format can include
     * placeholders like {player} and {message} to be replaced by the
     * player's name and message, respectively. In addition, this supports
     * all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public String getFormattedMessage(@Nonnull Chatter chatter, @Nonnull Message<?> message) {
        return Placeholders.create(chatter.player())
                .add("{player}", chatter.getName())
                .add("{message}", message.toText())
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
