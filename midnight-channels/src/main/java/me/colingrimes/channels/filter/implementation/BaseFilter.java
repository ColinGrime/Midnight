package me.colingrimes.channels.filter.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.filter.ChatFilter;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;

public abstract class BaseFilter implements ChatFilter {

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        if (message.getChatter().isEmpty()) {
            return false;
        }

        Chatter chatter = message.getChatter().get();
        if (getType() != null && chatter.hasPermission(getType().getBypassPermission())) {
            return false;
        }

        return filterMessage(message.toText(), chatter);
    }

    /**
     * Filters text based on its content.
     *
     * @param text    the text of the message
     * @param chatter the chatter who sent the message
     * @return true if the message should be filtered, false otherwise
     */
    abstract boolean filterMessage(@Nonnull String text, @Nonnull Chatter chatter);
}
