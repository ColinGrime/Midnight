package me.colingrimes.midnight.channel.filter;

import me.colingrimes.midnight.cache.expiring.RollingWindowCache;
import me.colingrimes.midnight.message.implementation.ChannelMessage;
import me.colingrimes.plugin.config.Filters;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.UUID;

/**
 * A chat filter that prevents flooding by limiting the number
 * of messages a participant can send within a certain time period.
 */
public class FloodFilter implements ChatFilter {

    private final RollingWindowCache<UUID> messageCache;

    public FloodFilter() {
        this.messageCache = new RollingWindowCache<>(Duration.ofMillis(Filters.FLOOD_TIME_WINDOW.get()));
    }

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        UUID uuid = message.getParticipant().getID();
        messageCache.increment(uuid);
        return messageCache.getCount(uuid) <= Filters.FLOOD_MAX_MESSAGES.get();
    }
}
