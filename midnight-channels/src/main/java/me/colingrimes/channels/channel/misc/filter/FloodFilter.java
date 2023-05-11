package me.colingrimes.channels.channel.misc.filter;

import me.colingrimes.channels.channel.misc.ChatFilter;
import me.colingrimes.channels.config.Filters;
import me.colingrimes.midnight.cache.expiring.RollingWindowCache;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.UUID;

/**
 * A chat filter that prevents flooding by limiting the number
 * of messages a chatter can send within a certain time period.
 */
public class FloodFilter implements ChatFilter {

    private final RollingWindowCache<UUID> messageCache;

    public FloodFilter() {
        this.messageCache = new RollingWindowCache<>(Duration.ofMillis(Filters.FLOOD_TIME_WINDOW.get()));
    }

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        if (message.getChatter() == null) {
            return false;
        }

        UUID uuid = message.getChatter().getID();
        messageCache.increment(uuid);
        return messageCache.getCount(uuid) > Filters.FLOOD_MAX_MESSAGES.get();
    }
}
