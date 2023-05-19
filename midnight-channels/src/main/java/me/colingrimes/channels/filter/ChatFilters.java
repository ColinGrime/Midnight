package me.colingrimes.channels.filter;

import me.colingrimes.channels.config.Messages;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.Players;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatFilters implements ChatFilter {

    private final Set<ChatFilter> filters = new HashSet<>();

    /**
     * Creates a new chat filter with all filters enabled.
     *
     * @return the chat filter
     */
    @Nonnull
    public static ChatFilters all() {
        return ChatFilters.of(List.of("ALL"));
    }

    /**
     * Creates a new chat filter based on a list of filter types.
     * If the list contains "ALL", all filters will be added.
     *
     * @param types the types of filters to add
     * @return the chat filter
     */
    @Nonnull
    public static ChatFilters of(@Nullable List<String> types) {
        ChatFilters filters = new ChatFilters();
        if (types == null) {
            return filters;
        }

        types = types.stream().map(String::toUpperCase).toList();
        if (types.contains("ALL")) {
            Arrays.stream(ChatFilterType.values()).map(ChatFilterType::getFilter).forEach(filters::add);
        } else {
            types.stream().map(ChatFilterType::valueOf).forEach(filters::add);
        }

        return filters;
    }

    /**
     * Adds the specified filter types.
     *
     * @param types the types of filters to add
     * @return this instance
     */
    @Nonnull
    public ChatFilters add(@Nonnull ChatFilterType... types) {
        Arrays.stream(types).forEach(t -> filters.add(t.getFilter()));
        return this;
    }

    /**
     * Adds the specified custom filters.
     *
     * @param filters the custom filters to add
     * @return this instance
     */
    @Nonnull
    public ChatFilters add(@Nonnull ChatFilter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        return this;
    }

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        boolean doFilter = false;

        for (ChatFilter filter : filters) {
            if (filter.filter(message)) {
                doFilter = true;
                break;
            }
        }

        if (!doFilter) {
            return false;
        }

        // Send the filtered message to all players with the filtered permission.
        if (message.getChatter().isPresent()) {
            Placeholders placeholders = Placeholders
                    .of("{player}", message.getChatter().get().getName())
                    .add("{message}", message.toText());
            Players.all().stream()
                    .filter(p -> p.hasPermission("channels.filtered"))
                    .forEach(Messages.MESSAGE_FILTERED.replace(placeholders)::send);
        }

        return true;
    }
}
