package me.colingrimes.channels.channel.misc.filter;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChatFilter;
import me.colingrimes.channels.config.Filters;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.midnight.cache.expiring.RollingWindowCache;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Optional;
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
        Optional<Chatter> chatter = message.getChatter();
        if (chatter.isEmpty()) {
            return false;
        }

        UUID uuid = chatter.get().getID();
        messageCache.increment(uuid);

        if (messageCache.getCount(uuid) > Filters.FLOOD_MAX_MESSAGES.get()) {
            chatter.filter(Chatter::online).ifPresent(c -> Settings.RAPID_FIRE_WARNING.send(c.player()));
            return true;
        } else {
            return false;
        }
    }
}
