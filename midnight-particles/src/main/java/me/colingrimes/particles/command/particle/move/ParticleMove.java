package me.colingrimes.particles.command.particle.move;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.util.bukkit.Locations;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;
import me.colingrimes.particles.particle.ParticleEffect;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleMove implements Command<MidnightParticles> {

	@Override
	public void execute(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
			return;
		}

		// Move the particle to the player's location if no coordinates are specified.
		if (args.size() == 0) {
			particle.get().setPoint(Point.of(sender.player(), Rotation.create()));
			Messages.PARTICLE_MOVE.replace("{location}", Locations.toString(sender.location())).send(sender);
			return;
		}

		Optional<Double> x = args.getDouble(0);
		Optional<Double> y = args.getDouble(1);
		Optional<Double> z = args.getDouble(2);

		if (x.isPresent() && y.isPresent() && z.isPresent()) {
			Location location = new Location(sender.world(), x.get(), y.get(), z.get());
			particle.get().setPoint(Point.of(location, Rotation.create()));
			Messages.PARTICLE_MOVE.replace("{location}", Locations.toString(location)).send(sender);
		} else {
			Messages.INVALID_LOCATION.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_MOVE_USAGE);
		properties.setPlayerRequired(true);
	}
}
