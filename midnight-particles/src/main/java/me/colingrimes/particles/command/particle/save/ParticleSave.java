package me.colingrimes.particles.command.particle.save;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;
import me.colingrimes.particles.particle.ParticleEffect;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleSave implements Command<MidnightParticles> {

	@Override
	public void execute(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());
		String name = args.get(0);

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
			return;
		}

		// Check if the particle name is valid.
		if (plugin.getParticleManager().getParticle(name).isPresent()) {
			Messages.PARTICLE_NAME_TAKEN.send(sender);
			return;
		} else {
			particle.get().setName(name);
		}

		Scheduler.ASYNC.call(() -> {
			plugin.getParticleStorage().save(particle.get());
			return null;
		}).thenRun(() -> {
			plugin.getParticleManager().addParticle(particle.get());
			Messages.PARTICLE_SAVE.replace("{name}", name).send(sender);
		}).exceptionally(ex -> {
			Messages.PARTICLE_NOT_SAVED.send(sender);
			ex.printStackTrace();
			return null;
		});
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_SAVE_USAGE);
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
