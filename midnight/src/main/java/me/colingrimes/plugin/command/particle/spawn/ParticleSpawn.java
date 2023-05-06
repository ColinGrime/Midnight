package me.colingrimes.plugin.command.particle.spawn;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleSpawn implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 0) {
			if (sender.isPlayer()) {
				Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());
				if (particle.isPresent()) {
					particle.get().startSpawning();
					Messages.PARTICLE_SPAWN.send(sender);
					return;
				} else {
					Messages.PARTICLE_NOT_SELECTED.send(sender);
				}
			} else {
				Messages.INVALID_SENDER.send(sender);
			}
			return;
		}

		String name = args.get(0);
		Optional<ParticleEffect> particle = plugin.getParticleManager().getParticle(name);

		// Check if the particle exists.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_FOUND.send(sender);
			return;
		}

		particle.get().startSpawning();
		Messages.PARTICLE_SPAWN.replace("{name}", Text.format(name)).send(sender);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_SPAWN_USAGE);
	}
}
