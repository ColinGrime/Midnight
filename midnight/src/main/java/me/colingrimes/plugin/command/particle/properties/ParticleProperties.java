package me.colingrimes.plugin.command.particle.properties;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleProperties implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.sendTo(sender);
			return;
		}

		me.colingrimes.midnight.particle.util.ParticleProperties properties = particle.get().getProperties();
		Vector vector = properties.getOffset();

		// Create the placeholders.
		Placeholders placeholders = Placeholders
				.of("{particle}", Text.format(properties.getParticle().name()))
				.add("{count}", properties.getCount())
				.add("{offset_x}", vector.getX())
				.add("{offset_y}", vector.getY())
				.add("{offset_z}", vector.getZ())
				.add("{speed}", properties.getSpeed())
				.add("{data}", properties.getData());

		Messages.PARTICLE_PROPERTIES.sendTo(sender, placeholders);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_PROPERTIES_USAGE);
		properties.setPlayerRequired(true);
	}
}
