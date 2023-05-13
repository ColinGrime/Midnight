package me.colingrimes.channels.channel.misc.filter;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChatFilter;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A chat filter that filters out messages containing URLs and IP addresses.
 */
public class AdvertiseFilter implements ChatFilter {

    private final Pattern urlPattern = Pattern.compile("^(https?:\\/\\/)?([\\w\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w\\.-]*)*\\/?$");
    private final Pattern ipPattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$");

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        Optional<Chatter> chatter = message.getChatter();
        String content = message.toText();

        if (chatter.isPresent() && (urlPattern.matcher(content).find() || ipPattern.matcher(content).find())) {
            chatter.filter(Chatter::online).ifPresent(c -> Settings.ADVERTISING_WARNING.send(c.player()));
            return true;
        } else {
            return false;
        }
    }
}
