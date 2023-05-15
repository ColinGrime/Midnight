package me.colingrimes.channels.filter;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.Players;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class ChatFilters implements ChatFilter {

    private final Set<ChatFilter> filters = new HashSet<>();

    /**
     * Adds a filter based on filter type.
     *
     * @param type the type of filter to add
     * @return this instance
     */
    @Nonnull
    public ChatFilters add(@Nonnull ChatFilterType type) {
        filters.add(type.getFilter());
        return this;
    }

    /**
     * Adds a custom filter.
     *
     * @param filter the custom filter to add
     * @return this instance
     */
    @Nonnull
    public ChatFilters add(@Nonnull ChatFilter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public boolean filter(@Nonnull Message<?> message, @Nullable Chatter chatter) {
        boolean doFilter = false;

        for (ChatFilter filter : filters) {
            if (filter.filter(message, chatter)) {
                doFilter = true;
                break;
            }
        }

        if (!doFilter) {
            return false;
        }

        // Send the filtered message to all players with the filtered permission.
        if (chatter != null) {
            Placeholders placeholders = Placeholders
                    .of("{player}", chatter.getName())
                    .add("{message}", message.toText());
            Players.all().stream()
                    .filter(p -> p.hasPermission("channels.filtered"))
                    .forEach(Messages.MESSAGE_FILTERED.replace(placeholders)::send);
        }

        return true;
    }
}
