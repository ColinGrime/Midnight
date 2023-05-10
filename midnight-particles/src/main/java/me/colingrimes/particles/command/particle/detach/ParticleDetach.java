package me.colingrimes.particles.command.particle.detach;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;
import me.colingrimes.particles.particle.ParticleEffect;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleDetach implements Command<MidnightParticles> {

	@Override
	public void execute(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());

		if (particle.isPresent()) {
			particle.get().detach();
			Messages.PARTICLE_DETACH_SELF.send(sender);
		} else {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_DETACH_USAGE);
		properties.setPlayerRequired(true);
	}
}
