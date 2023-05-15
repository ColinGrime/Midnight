package me.colingrimes.channels.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.message.Message;

import static me.colingrimes.midnight.config.option.OptionFactory.message;

@Configuration("messages.yml")
public interface Messages {

	/**************************************************
	 *                Success Messages                *
	 **************************************************/
	Message<?> IGNORED = message("success.ignored", "&2&l✓ &aYou have ignored &2{player}&a.");
	Message<?> NICKNAME_CHANGED = message("success.nickname-changed", "&2&l✓ &aNickname has been changed to &2{nickname}&a.");
	Message<?> UNIGNORED = message("success.unignored", "&2&l✓ &aYou have unignored &2{player}&a.");

	/**************************************************
	 *                Failure Messages                *
	 **************************************************/
	Message<?> ADVERTISING_WARNING = message("failure.advertising-warning", "&4&l❌ &cAdvertising is not allowed here. Please respect our rules.");
	Message<?> CHANNEL_ACCESS_DENIED = message("failure.channel-access-denied", "&4&l❌ &cYou do not have access to this channel.");
	Message<?> CHANNEL_DISABLED = message("failure.channel-disabled", "&4&l❌ &cThis channel is currently disabled. Please try again later.");
	Message<?> MESSAGE_FILTERED = message("failure.message-filtered", "&4&l❌ &cMessage from &4{player} &cwas filtered: &7{message}");
	Message<?> MESSAGE_SELF = message("failure.message-self", "&4&l❌ &cYou cannot send messages to yourself.");
	Message<?> CURRENTLY_MUTED = message("failure.muted-message", "&4&l❌ &cYou're currently muted and cannot send messages.");
	Message<?> NICKNAME_NOT_ALPHANUMERIC = message("failure.nickname-not-alphanumeric", "&4&l❌ &cName must be alphanumeric.");
	Message<?> NICKNAME_TOO_LONG = message("failure.nickname-too-long", "&4&l❌ &cName must be less than 16 characters.");
	Message<?> NICKNAME_TOO_SHORT = message("failure.nickname-too-short", "&4&l❌ &cName must be more than 3 characters.");
	Message<?> NOBODY_TO_REPLY_TO = message("failure.nobody-to-reply-to", "&4&l❌ &cYou have nobody to reply to.");
	Message<?> PLAYER_NOT_FOUND = message("failure.player-not-found", "&4&l❌ &cThe specified player was not found.");
	Message<?> PLAYER_NOT_LOADED = message("failure.player-not-loaded", "&4&l❌ &cThis is awkward... Your chat data could not be loaded. Please relog.");
	Message<?> PROFANITY_WARNING = message("failure.profanity-warning", "&4&l❌ &cPlease watch your language. Profanity is not tolerated.");
	Message<?> RAPID_FIRE_WARNING = message("failure.rapid-fire-warning", "&4&l❌ &cYou're sending messages too quickly. Please slow down.");
	Message<?> SPAM_WARNING = message("failure.spam-warning", "&4&l❌ &cPlease refrain from spamming the chat.");

	/**************************************************
	 *                 Usage Messages                 *
	 **************************************************/
	Message<?> NICKNAME_USAGE = message("usage.nickname",
			"&eUsage: &a/nickname <name>",
			"&a► &7Set your nickname to the specified name."
	);
}
