package me.colingrimes.plugin.command.particle.detach;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleDetach implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
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
