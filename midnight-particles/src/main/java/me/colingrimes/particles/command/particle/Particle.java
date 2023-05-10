package me.colingrimes.particles.command.particle;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;

import javax.annotation.Nonnull;

public class Particle implements Command<MidnightParticles> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_USAGE);
	}
}
