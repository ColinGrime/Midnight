package me.colingrimes.plugin.command.particle.clear;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.plugin.MidnightPlugin;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleClear implements Command<MidnightPlugin> {

	@Override
	public void execute(@Nonnull MidnightPlugin plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());

		// Check if a particle is selected.
		if (particle.isPresent()) {
			particle.get().stopSpawning();
			Messages.PARTICLE_CLEAR.send(sender);
		} else {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_CLEAR_USAGE);
		properties.setPlayerRequired(true);
	}
}
