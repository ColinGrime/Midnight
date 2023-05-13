package me.colingrimes.channels.filter.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.channels.filter.ChatFilterType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * A chat filter that filters out messages containing URLs and IP addresses.
 */
public class AdvertiseFilter extends BaseFilter {

    private final Pattern urlPattern = Pattern.compile("^(https?:\\/\\/)?([\\w\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w\\.-]*)*\\/?$");
    private final Pattern ipPattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$");

    @Override
    boolean filterMessage(@Nonnull String text, @Nonnull Chatter chatter) {
        if (urlPattern.matcher(text).find() || ipPattern.matcher(text).find()) {
            chatter.send(Settings.ADVERTISING_WARNING);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public ChatFilterType getType() {
        return ChatFilterType.ADVERTISE;
    }
}
