package me.colingrimes.channels.channel.implementation;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelSettings;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.channels.event.ChannelMessageEvent;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.Common;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

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

    @Override
    public void broadcast(@Nonnull Message<?> message) {
        if (!enabled) {
            return;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, null, message);
        settings.logMessage(channelMessage);
        getRecipients().forEach(r -> r.send(channelMessage));
    }

    @Override
    public boolean send(@Nonnull Chatter sender, @Nonnull Message<?> message) {
        if (!enabled) {
            Messages.CHANNEL_DISABLED.send(sender.player());
            return false;
        } else if (!hasAccess(sender)) {
            Messages.CHANNEL_ACCESS_DENIED.send(sender.player());
            return false;
        } else if (sender.isMuted()) {
            Messages.CURRENTLY_MUTED.send(sender.player());
            return false;
        }

        ChannelMessage<?> channelMessage = new ChannelMessage<>(this, sender, message);
        settings.logMessage(channelMessage);
        if (settings.filter(channelMessage)) {
            return false;
        }

        Set<Chatter> recipients = new HashSet<>(getRecipients());
        recipients.removeIf(r -> !r.online());

        // You cannot ignore staff, staff cannot ignore you.
        if (!sender.hasPermission("channels.staff")) {
            recipients.removeIf(r -> r.isIgnoring(sender.getID()) && !r.hasPermission("channels.staff"));
        }

        ChannelMessageEvent channelMessageEvent = new ChannelMessageEvent(this, sender, recipients, message);
        Common.call(channelMessageEvent);

        if (!channelMessageEvent.isCancelled()) {
            Message<?> formattedMessage = settings.getFormattedMessage(sender, channelMessageEvent.getMessage());
            recipients.forEach(r -> r.send(formattedMessage));
            return true;
        } else {
            return false;
        }
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
