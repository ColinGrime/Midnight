package me.colingrimes.midnight.channel.filter.profanity;

import me.colingrimes.midnight.message.implementation.ChannelMessage;
import me.colingrimes.plugin.config.Filters;

import javax.annotation.Nonnull;

/**
 * A basic profanity filter that filters out messages containing banned words.
 */
public class BasicProfanityFilter extends ProfanityFilter {

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        String content = message.toText().toLowerCase();
        return Filters.PROFANITY_LIST.get().stream().noneMatch(content::contains);
    }
}
