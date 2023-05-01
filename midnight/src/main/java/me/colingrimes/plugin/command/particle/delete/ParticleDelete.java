package me.colingrimes.plugin.command.particle.delete;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleDelete implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		String name = args.get(0);
		Optional<ParticleEffect> particle = plugin.getParticleManager().getParticle(name);

		// Check if the particle exists.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_FOUND.sendTo(sender);
			return;
		}

		Scheduler.ASYNC.call(() -> {
			plugin.getParticleStorage().delete(particle.get());
			return null;
		}).thenRun(() -> {
			plugin.getParticleManager().deleteParticle(particle.get());
			Messages.PARTICLE_DELETE.sendTo(sender, Placeholders.of("{name}", Text.format(name)));
		}).exceptionally(ex -> {
			Messages.PARTICLE_NOT_DELETED.sendTo(sender);
			ex.printStackTrace();
			return null;
		});
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_DELETE_USAGE);
		properties.setArgumentsRequired(1);
	}
}
