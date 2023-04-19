package me.colingrimes.plugin.command.particle.attach;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleAttach implements Command<Midnight>  {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> effect = plugin.getParticleManager().getSelectedParticle(sender.player());
		if (args.size() == 0) {
			effect.ifPresent(particle -> particle.attach(sender.player()));
			return;
		}

		args.getPlayer(0).ifPresent(player -> effect.ifPresent(particle -> particle.attach(player)));
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_ATTACH_USAGE);
		properties.setPlayerRequired(true);
	}
}
