package me.colingrimes.particles.command.particle;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.plugin.MidnightTemp;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;

public class Particle implements Command<MidnightTemp> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_USAGE);
	}
}
