package me.colingrimes.plugin.command.particle.move;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.model.Point;
import me.colingrimes.midnight.model.Rotation;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.plugin.Midnight;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleMove implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> effect = plugin.getParticleManager().getSelectedParticle(sender.player());
		if (effect.isEmpty()) {
			sender.message("&cYou must select a particle first!");
			return;
		} else if (args.size() == 0) {
			effect.get().setPoint(Point.of(sender.player()));
			return;
		}

		Optional<Double> x = args.getDouble(0);
		Optional<Double> y = args.getDouble(1);
		Optional<Double> z = args.getDouble(2);

		if (x.isPresent() && y.isPresent() && z.isPresent()) {
			effect.get().setPoint(Point.of(sender.world(), x.get(), y.get(), z.get(), Rotation.of(0, 0, 0)));
		} else {
			sender.message("&cInvalid coordinates!");
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
