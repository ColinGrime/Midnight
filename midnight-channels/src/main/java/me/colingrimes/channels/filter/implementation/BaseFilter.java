package me.colingrimes.channels.filter.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.filter.ChatFilter;
import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseFilter implements ChatFilter {

    @Override
    public boolean filter(@Nonnull Message<?> message, @Nullable Chatter chatter) {
        if (chatter == null) {
            return false;
        }

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
