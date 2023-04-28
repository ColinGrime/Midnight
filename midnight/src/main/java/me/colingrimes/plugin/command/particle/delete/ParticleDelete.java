package me.colingrimes.plugin.command.particle.delete;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;

public class ParticleDelete implements Command<Midnight> {



	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_DELETE_USAGE);
	}
}
