package me.colingrimes.example.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("other.yml")
public interface OtherSettings {

	Option<Integer> exampleNumber = option("exampleNumber", 5);
	Option<Boolean> exampleBoolean = option("exampleBoolean", true);
}
