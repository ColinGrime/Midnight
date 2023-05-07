package me.colingrimes.plugin.command.particle;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.plugin.MidnightPlugin;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;

public class Particle implements Command<MidnightPlugin> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_USAGE);
	}
}
