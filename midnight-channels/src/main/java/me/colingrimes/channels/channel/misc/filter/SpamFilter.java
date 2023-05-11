package me.colingrimes.channels.channel.misc.filter;

import me.colingrimes.channels.channel.misc.ChatFilter;
import me.colingrimes.channels.config.Filters;
import me.colingrimes.midnight.cache.ExpiringCache;
import me.colingrimes.midnight.cache.expiring.SimpleExpiringCache;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.UUID;

/**
 * A chat filter that prevents spamming by checking if a chatter
 * sends the same message within a certain time period.
 */
public class SpamFilter implements ChatFilter {

    private final ExpiringCache<UUID, String> messageCache;

    public SpamFilter() {
        this.messageCache = new SimpleExpiringCache<>(Duration.ofMillis(Filters.SPAM_TIME_WINDOW.get()));
    }

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        if (message.getChatter() == null) {
            return false;
        }

        UUID uuid = message.getChatter().getID();
        String content = message.toText();

        String lastMessage = messageCache.get(uuid);
        if (lastMessage != null && lastMessage.equals(content)) {
            return true;
        }

        messageCache.put(uuid, content);
        return false;
    }
}
