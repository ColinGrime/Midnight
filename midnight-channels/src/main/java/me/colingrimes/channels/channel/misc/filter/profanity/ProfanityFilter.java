package me.colingrimes.channels.channel.misc.filter.profanity;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.misc.ChatFilter;
import me.colingrimes.channels.config.Settings;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;

/**
 * An abstract chat filter that filters out messages containing profanity.
 */
public abstract class ProfanityFilter implements ChatFilter {

	/**
	 * Filters out profanity from the given message.
	 *
	 * @param message the message to filter
	 * @return true if the message contains profanity, false otherwise
	 */
	abstract boolean filterProfanity(@Nonnull ChannelMessage<?> message);

	@Override
	public boolean filter(@Nonnull ChannelMessage<?> message) {
		if (filterProfanity(message)) {
			message.getChatter().filter(Chatter::online).ifPresent(c -> Settings.PROFANITY_WARNING.send(c.player()));
			return true;
		} else {
			return false;
		}
	}
}
