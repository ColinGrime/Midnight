package me.colingrimes.particles.command.particle.attach;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;
import me.colingrimes.particles.particle.ParticleEffect;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleAttach implements Command<MidnightParticles>  {

	@Override
	public void execute(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());
		Optional<Player> target = args.getPlayer(0);

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
			return;
		}

		// Attach to self if no player is specified.
		if (args.size() == 0) {
			particle.get().attach(sender.player());
			Messages.PARTICLE_ATTACH_SELF.send(sender);
			return;
		}

		// Attach to player if they exist.
		if (target.isPresent()) {
			particle.get().attach(target.get());
			Messages.PARTICLE_ATTACH_PLAYER.replace("{player}", target.get().getName()).send(sender);
		} else {
			Messages.PLAYER_NOT_FOUND.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_ATTACH_USAGE);
		properties.setPlayerRequired(true);
	}
}
