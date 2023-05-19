package me.colingrimes.channels.event;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChannelType;
import me.colingrimes.midnight.message.Message;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Event that is fired when a message is sent in a channel.
 */
public class ChannelMessageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final Channel channel;
    private final Chatter sender;
    private final Set<Chatter> recipients;
    private Message<?> message;

    /**
     * Constructs a new ChannelMessageEvent.
     *
     * @param channel    the channel in which the message is sent
     * @param sender     the sender of the message
     * @param recipients the recipients of the message
     * @param message    the message that is sent
     */
    public ChannelMessageEvent(@Nonnull Channel channel, @Nonnull Chatter sender, @Nonnull Set<Chatter> recipients, @Nonnull Message<?> message) {
        this.channel = channel;
        this.sender = sender;
        this.recipients = recipients;
        this.message = message;
    }

    /**
     * Gets the channel in which the message is sent.
     *
     * @return the channel
     */
    @Nonnull
    public Channel getChannel() {
        return channel;
    }

    /**
     * Gets the type of the channel in which the message is sent.
     *
     * @return the channel type
     */
    @Nonnull
    public ChannelType getType() {
        return channel.getType();
    }

    /**
     * Gets the sender of the message.
     *
     * @return the sender
     */
    @Nonnull
    public Chatter getSender() {
        return sender;
    }

    /**
     * Gets the recipients of the message.
     *
     * @return the recipients
     */
    @Nonnull
    public Set<Chatter> getRecipients() {
        return recipients;
    }

    /**
     * Gets the message that is sent.
     *
     * @return the message
     */
    @Nonnull
    public Message<?> getMessage() {
        return message;
    }

    /**
     * Sets the message that is sent.
     *
     * @param message the message
     */
    public void setMessage(@Nonnull Message<?> message) {
        this.message = message;
    }

    /**
     * Whether the event is cancelled.
     *
     * @return true if the event is cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of the event.
     *
     * @param cancel true if you wish to cancel the event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
