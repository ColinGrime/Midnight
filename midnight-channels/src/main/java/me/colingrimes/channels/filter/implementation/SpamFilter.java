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
 * A chat filter that prevents spamming by checking if a chatter
 * sends the same message within a certain time period.
 */
public class SpamFilter extends BaseFilter {

//    private ExpiringCache<UUID, String> messageCache;

    @Override
    boolean filterMessage(@Nonnull String text, @Nonnull Chatter chatter) {
//        if (messageCache == null) {
//            messageCache = new SimpleExpiringCache<>(Duration.ofSeconds(Filters.SPAM_TIME_WINDOW.get()));
//        }
//
//        UUID uuid = chatter.getID();
//        String lastMessage = messageCache.get(uuid);
//
//        if (lastMessage != null && lastMessage.equals(text)) {
//            Messages.SPAM_WARNING.send(chatter.player());
//            return true;
//        } else {
//            messageCache.put(uuid, text);
//            return false;
//        }
        return false;
    }

    @Nullable
    @Override
    public ChatFilterType getType() {
        return ChatFilterType.SPAM;
    }
}
