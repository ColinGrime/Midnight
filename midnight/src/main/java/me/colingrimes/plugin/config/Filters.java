package me.colingrimes.plugin.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.MessageOption;
import me.colingrimes.midnight.config.option.Option;

import java.util.List;

import static me.colingrimes.midnight.config.option.OptionFactory.message;
import static me.colingrimes.midnight.config.option.OptionFactory.option;

@Configuration("filters.yml")
public interface Filters {

    /**************************************************
     *                  Spam Filter                   *
     **************************************************/
    Option<Integer> SPAM_TIME_WINDOW = option("spam-filter.time-window", 2);

    /**************************************************
     *                 Flood Filter                   *
     **************************************************/
    Option<Integer> FLOOD_MAX_MESSAGES = option("flood-filter.max-messages", 8);
    Option<Integer> FLOOD_TIME_WINDOW = option("flood-filter.time-window", 5);

    /**************************************************
     *                Profanity Filter                *
     **************************************************/
    Option<Double> PROFANITY_MAX_ALLOWED_SIMILARITY_DISTANCE = option("profanity-filter.max-allowed-similarity-distance", 0.8);
    MessageOption<List<String>> PROFANITY_LIST = message("profanity-filter.swears");
}
