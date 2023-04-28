package me.colingrimes.plugin.command.display;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.plugin.Midnight;

import javax.annotation.Nonnull;

public class Display implements Command<Midnight> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setArgumentsRequired(3);
	}
}
