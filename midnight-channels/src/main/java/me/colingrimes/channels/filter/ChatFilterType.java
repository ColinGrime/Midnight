package me.colingrimes.channels.filter;

import me.colingrimes.channels.filter.implementation.AdvertiseFilter;
import me.colingrimes.channels.filter.implementation.FloodFilter;
import me.colingrimes.channels.filter.implementation.ProfanityFilter;
import me.colingrimes.channels.filter.implementation.SpamFilter;

import javax.annotation.Nonnull;

public enum ChatFilterType {

    PROFANITY(new ProfanityFilter(), "channels.filter.profanity"),
    ADVERTISE(new AdvertiseFilter(), "channels.filter.advertise"),
    SPAM(new SpamFilter(), "channels.filter.spam"),
    FLOOD(new FloodFilter(), "channels.filter.flood");

    private final ChatFilter filter;
    private final String bypassPermission;

    ChatFilterType(@Nonnull ChatFilter filter, @Nonnull String bypassPermission) {
        this.filter = filter;
        this.bypassPermission = bypassPermission;
    }

    /**
     * Gets the filter associated with this type.
     *
     * @return the filter
     */
    @Nonnull
    public ChatFilter getFilter() {
        return filter;
    }

    /**
     * Gets the bypass permission associated with this type.
     *
     * @return the bypass permission
     */
    @Nonnull
    public String getBypassPermission() {
        return bypassPermission;
    }
}
