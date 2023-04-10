package me.colingrimes.example.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration
public interface Settings {
	Option<String> example = string("example", "Hello World!");
}
