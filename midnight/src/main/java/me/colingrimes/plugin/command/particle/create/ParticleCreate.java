package me.colingrimes.plugin.command.particle.create;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.implementation.ParticleEffectFactory;
import me.colingrimes.midnight.particle.util.ParticleEffectType;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;

public class ParticleCreate implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		ParticleEffectType type = ParticleEffectType.fromString(args.get(0));
		ParticleEffect effect = ParticleEffectFactory.create(type, sender.player());

		effect.startSpawning();
		plugin.getParticleManager().selectParticle(sender.player(), effect);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_CREATE_USAGE);
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
