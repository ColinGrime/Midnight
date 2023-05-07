package me.colingrimes.midnight.channel.filter;

import me.colingrimes.midnight.cache.expiring.SimpleExpiringCache;
import me.colingrimes.midnight.message.implementation.ChannelMessage;
import me.colingrimes.plugin.config.Filters;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.UUID;

/**
 * A chat filter that prevents spamming by checking if a participant
 * sends the same message within a certain time period.
 */
public class SpamFilter implements ChatFilter {

    private final SimpleExpiringCache<UUID, String> messageCache;

    public SpamFilter() {
        this.messageCache = new SimpleExpiringCache<>(Duration.ofMillis(Filters.SPAM_TIME_WINDOW.get()));
    }

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        UUID uuid = message.getParticipant().getID();
        String content = message.toText();

        String lastMessage = messageCache.get(uuid);
        if (lastMessage != null && lastMessage.equals(content)) {
            return false;
        }

        messageCache.put(uuid, content);
        return true;
    }
}
