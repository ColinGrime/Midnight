package me.colingrimes.channels.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.message.Message;

import static me.colingrimes.midnight.config.option.OptionFactory.message;

@Configuration
public interface Settings {

	Message<?> PLAYER_NOT_LOADED = message("messages.player-not-loaded", "&4&l❌ &cThis is awkward... Your chat data could not be loaded. Please relog.");
}
