package me.colingrimes.plugin.command.particle.save;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;

public class ParticleSave implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());
		String name = args.get(0);

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.sendTo(sender);
			return;
		}

		// Check if the particle name is valid.
		if (plugin.getParticleManager().getParticle(name).isPresent()) {
			Messages.PARTICLE_NAME_TAKEN.sendTo(sender);
			return;
		}

		Scheduler.ASYNC.run(() -> {
			try {
				particle.get().setName(name);
				plugin.getParticleStorage().save(particle.get());
				Messages.PARTICLE_SAVE.sendTo(sender);
			} catch (IOException e) {
				Messages.PARTICLE_NOT_SAVED.sendTo(sender);
				e.printStackTrace();
			}
		});
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_SAVE_USAGE);
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
