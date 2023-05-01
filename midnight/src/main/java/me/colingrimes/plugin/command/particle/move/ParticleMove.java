package me.colingrimes.plugin.command.particle.move;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.util.bukkit.Locations;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleMove implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.sendTo(sender);
			return;
		}

		// Move the particle to the player's location if no coordinates are specified.
		if (args.size() == 0) {
			particle.get().setPoint(Point.of(sender.player()));
			Messages.PARTICLE_MOVE.sendTo(sender, Placeholders.of("{location}", Locations.toString(sender.location())));
			return;
		}

		Optional<Double> x = args.getDouble(0);
		Optional<Double> y = args.getDouble(1);
		Optional<Double> z = args.getDouble(2);

		if (x.isPresent() && y.isPresent() && z.isPresent()) {
			Location location = new Location(sender.world(), x.get(), y.get(), z.get());
			particle.get().setPoint(Point.of(location));
			Messages.PARTICLE_MOVE.sendTo(sender, Placeholders.of("{location}", Locations.toString(location)));
		} else {
			Messages.INVALID_LOCATION.sendTo(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_MOVE_USAGE);
		properties.setPlayerRequired(true);
	}
}
