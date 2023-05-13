package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelSettings;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;

/**
 * This is a base implementation of the Channel interface.
 * It contains common behavior that can be shared across all channel types.
 */
public abstract class BaseChannel implements Channel {

    protected final String name;
    protected final ChannelSettings settings;
    protected boolean enabled = true;

    /**
     * Constructor for creating a new BaseChannel.
     *
     * @param name the name of the channel
     */
    public BaseChannel(@Nonnull String name) {
        this.name = name;
        this.settings = new ChannelSettings();
    }

    @Override
    @Nonnull
    public String getName() {
        return this.name;
    }

    @Override
    @Nonnull
    public ChannelSettings getSettings() {
        return this.settings;
    }

    // Override as needed.
    void broadcast(@Nonnull ChannelMessage<?> message) {}
    void send(@Nonnull Chatter sender, @Nonnull ChannelMessage<?> message) {}

    @Override
    public void broadcast(@Nonnull Message<?> message) {
        if (!enabled) {
            return;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, null, message);
        settings.logMessage(channelMessage);
        broadcast(channelMessage);
    }

    @Override
    public boolean send(@Nonnull Chatter sender, @Nonnull Message<?> message) {
        if (!enabled) {
            Settings.CHANNEL_DISABLED_MESSAGE.send(sender.player());
        } else if (!hasAccess(sender)) {
            Settings.CHANNEL_ACCESS_DENIED.send(sender.player());
        } else if (sender.isMuted()) {
            Settings.MUTED_MESSAGE.send(sender.player());
        } else {
            ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
            settings.logMessage(channelMessage);
            if (!settings.filter(channelMessage)) {
                send(sender, channelMessage);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }
}
