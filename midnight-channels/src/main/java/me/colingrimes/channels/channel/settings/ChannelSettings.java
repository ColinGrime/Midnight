package me.colingrimes.channels.channel.settings;

import me.colingrimes.channels.channel.Participant;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

/**
 * Represents the settings for a communication channel.
 */
public class ChannelSettings {

    private final Map<ChannelPermission, String> permissions = new EnumMap<>(ChannelPermission.class);
    private String messageFormat = "&7<&f&l{vault_prefix}&7> &e{player}&7: &f{message}";
    private ChatColor chatColor = ChatColor.GRAY;
    private boolean logMessages = false;

    /**
     * Gets the permissions for the channel.
     *
     * @return the permissions
     */
    @Nonnull
    public Map<ChannelPermission, String> getPermissions() {
        return permissions;
    }

    /**
     * Sets a permission for the channel.
     *
     * @param permission the channel permission to set
     * @param value      the permission value
     */
    public void setPermission(@Nonnull ChannelPermission permission, @Nonnull String value) {
        permissions.put(permission, value);
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
    public String getFormattedMessage(@Nonnull Participant participant, @Nonnull Message<?> message) {
        return Placeholders.create(participant.player())
                .add("{player}", participant.getName())
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
     * Gets the default chat color for the channel.
     *
     * @return the default chat color
     */
    @Nonnull
    public ChatColor getChatColor() {
        return chatColor;
    }

    /**
     * Sets the default chat color for the channel.
     *
     * @param color the default chat color
     */
    public void setChatColor(@Nonnull ChatColor color) {
        this.chatColor = color;
    }

    /**
     * Gets whether to log messages sent in the channel.
     *
     * @return whether to log messages
     */
    public boolean logMessages() {
        return logMessages;
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
