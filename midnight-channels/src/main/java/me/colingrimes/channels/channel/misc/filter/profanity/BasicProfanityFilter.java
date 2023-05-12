package me.colingrimes.channels.channel.misc.filter.profanity;

import me.colingrimes.channels.config.Filters;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;

/**
 * A basic profanity filter that filters out messages containing banned words.
 */
public class BasicProfanityFilter extends ProfanityFilter {

    @Override
    public boolean filterProfanity(@Nonnull ChannelMessage<?> message) {
        String content = message.toText().toLowerCase();
        return Filters.PROFANITY_LIST.get().stream().anyMatch(content::contains);
    }
}
