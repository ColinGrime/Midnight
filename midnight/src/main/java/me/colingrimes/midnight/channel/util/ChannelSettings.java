package me.colingrimes.midnight.channel.util;

import me.colingrimes.midnight.util.bukkit.ChatColor;

import javax.annotation.Nonnull;

/**
 * Represents the settings for a communication channel.
 */
public class ChannelSettings {

    private ChatColor chatColor = ChatColor.GRAY;
    private String messageFormat = "<{name}> {message}";
    private boolean advertiseFilterEnabled = true;
    private boolean spamFilterEnabled = true;
    private boolean floodFilterEnabled = true;
    private boolean profanityFilterEnabled = true;

    /**
     * Gets the default chat color for the channel.
     * @return the default chat color
     */
    @Nonnull
    public ChatColor getChatColor() {
        return chatColor;
    }

    /**
     * Sets the default chat color for the channel.
     * @param color the default chat color
     */
    public void setChatColor(@Nonnull ChatColor color) {
        this.chatColor = color;
    }

    /**
     * Gets the message format for the channel. The format can include
     * placeholders like {player} and {message} to be replaced by the
     * player's name and message, respectively. In addition, this supports
     * all the placeholders from the PlaceholderAPI plugin.
     *
     * @return the message format
     */
    @Nonnull
    public String getMessageFormat() {
        return messageFormat;
    }

    /**
     * Sets the message format for the channel.
     * @param messageFormat the message format
     */
    public void setMessageFormat(@Nonnull String messageFormat) {
        this.messageFormat = messageFormat;
    }

    /**
     * Checks if the {@link me.colingrimes.midnight.channel.filter.AdvertiseFilter} is enabled for this channel.
     * @return true if enabled, false otherwise
     */
    public boolean isAdvertiseFilterEnabled() {
        return advertiseFilterEnabled;
    }

    /**
     * Sets the {@link me.colingrimes.midnight.channel.filter.AdvertiseFilter} status for this channel.
     * @param enabled true to enable, false to disable
     */
    public void setAdvertiseFilterEnabled(boolean enabled) {
        this.advertiseFilterEnabled = enabled;
    }

    /**
     * Checks if the {@link me.colingrimes.midnight.channel.filter.FloodFilter} is enabled for this channel.
     * @return true if enabled, false otherwise
     */
    public boolean isFloodFilterEnabled() {
        return floodFilterEnabled;
    }

    /**
     * Sets the {@link me.colingrimes.midnight.channel.filter.FloodFilter} status for this channel.
     * @param enabled true to enable, false to disable
     */
    public void setFloodFilterEnabled(boolean enabled) {
        this.floodFilterEnabled = enabled;
    }

    /**
     * Checks if the {@link me.colingrimes.midnight.channel.filter.SpamFilter} is enabled for this channel.
     * @return true if enabled, false otherwise
     */
    public boolean isSpamFilterEnabled() {
        return spamFilterEnabled;
    }

    /**
     * Sets the {@link me.colingrimes.midnight.channel.filter.SpamFilter} status for this channel.
     * @param enabled true to enable, false to disable
     */
    public void setSpamFilterEnabled(boolean enabled) {
        this.spamFilterEnabled = enabled;
    }

    /**
     * Checks if the {@link me.colingrimes.midnight.channel.filter.profanity.ProfanityFilter} is enabled for this channel.
     * @return true if enabled, false otherwise
     */
    public boolean isProfanityFilterEnabled() {
        return profanityFilterEnabled;
    }

    /**
     * Sets the {@link me.colingrimes.midnight.channel.filter.profanity.ProfanityFilter} status for this channel.
     * @param enabled true to enable, false to disable
     */
    public void setProfanityFilterEnabled(boolean enabled) {
        this.profanityFilterEnabled = enabled;
    }
}
