package me.colingrimes.plugin.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Message;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration
public interface Settings {

	Message<String> PERMISSION_DENIED = message("no-permission", "&cYou do not have permission.");
	Message<String> INVALID_SENDER = message("invalid-sender", "&cYou must be a player to use this command.");
}
