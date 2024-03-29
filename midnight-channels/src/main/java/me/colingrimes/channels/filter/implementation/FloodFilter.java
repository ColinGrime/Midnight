package me.colingrimes.channels.filter.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.config.Filters;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.channels.filter.ChatFilterType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.UUID;

/**
 * A chat filter that prevents flooding by limiting the number
 * of messages a chatter can send within a certain time period.
 */
public class FloodFilter extends BaseFilter {

//    private RollingWindowCache<UUID> messageCache;

    @Override
    boolean filterMessage(@Nonnull String text, @Nonnull Chatter chatter) {
//        if (messageCache == null) {
//            messageCache = new RollingWindowCache<>(Duration.ofSeconds(Filters.FLOOD_TIME_WINDOW.get()));
//        }
//
//        UUID uuid = chatter.getID();
//        messageCache.increment(uuid);
//
//        if (messageCache.getCount(uuid) > Filters.FLOOD_MAX_MESSAGES.get()) {
//            chatter.send(Messages.RAPID_FIRE_WARNING);
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    @Nullable
    @Override
    public ChatFilterType getType() {
        return ChatFilterType.FLOOD;
    }
}
