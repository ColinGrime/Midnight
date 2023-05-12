package me.colingrimes.channels.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.message.Message;

import static me.colingrimes.midnight.config.option.OptionFactory.message;

@Configuration
public interface Settings {

	Message<?> ADVERTISING_WARNING = message("messages.advertising-warning", "&4&l❌ &cAdvertising is not allowed here. Please respect our rules.");
	Message<?> CHANNEL_ACCESS_DENIED = message("messages.channel-access-denied", "&4&l❌ &cYou do not have access to this channel.");
	Message<?> CHANNEL_DISABLED_MESSAGE = message("messages.channel-disabled-message", "&4&l❌ &cThis channel is currently disabled. Please try again later.");
	Message<?> MESSAGE_FILTERED = message("messages.message-filtered", "&4&l❌ &cMessage from &4{player} &cwas filtered: &7{message}");
	Message<?> MUTED_MESSAGE = message("messages.muted-message", "&4&l❌ &cYou're currently muted and cannot send messages.");
	Message<?> PLAYER_NOT_LOADED = message("messages.player-not-loaded", "&4&l❌ &cThis is awkward... Your chat data could not be loaded. Please relog.");
	Message<?> PROFANITY_WARNING = message("messages.profanity-warning", "&4&l❌ &cPlease watch your language. Profanity is not tolerated.");
	Message<?> RAPID_FIRE_WARNING = message("messages.rapid-fire-warning", "&4&l❌ &cYou're sending messages too quickly. Please slow down.");
	Message<?> REPLY_FAILURE = message("messages.reply-failure", "&4&l❌ &cYou have nobody to reply to.");
	Message<?> SPAM_WARNING = message("messages.spam-warning", "&4&l❌ &cPlease refrain from spamming the chat.");
}
