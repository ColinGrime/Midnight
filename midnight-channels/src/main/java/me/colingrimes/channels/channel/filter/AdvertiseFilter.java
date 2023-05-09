package me.colingrimes.channels.channel.filter;

import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * A chat filter that filters out messages containing URLs.
 */
public class AdvertiseFilter implements ChatFilter {

    private final Pattern urlPattern = Pattern.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        String content = message.toText();
        return !urlPattern.matcher(content).find();
    }
}
