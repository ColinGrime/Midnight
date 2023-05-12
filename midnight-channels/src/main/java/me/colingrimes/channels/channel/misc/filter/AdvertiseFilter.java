package me.colingrimes.channels.channel.misc.filter;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChatFilter;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A chat filter that filters out messages containing URLs.
 */
public class AdvertiseFilter implements ChatFilter {

    private final Pattern urlPattern = Pattern.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        Optional<Chatter> chatter = message.getChatter();
        String content = message.toText();

        if (chatter.isPresent() && urlPattern.matcher(content).find()) {
            chatter.filter(Chatter::online).ifPresent(c -> Settings.ADVERTISING_WARNING.send(c.player()));
            return true;
        } else {
            return false;
        }
    }
}
