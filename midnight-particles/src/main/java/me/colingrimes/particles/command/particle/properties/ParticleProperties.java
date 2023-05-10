package me.colingrimes.particles.command.particle.properties;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;
import me.colingrimes.particles.particle.ParticleEffect;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ParticleProperties implements Command<MidnightParticles> {

	@Override
	public void execute(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
			return;
		}

		me.colingrimes.particles.particle.util.ParticleProperties properties = particle.get().getProperties();
		Vector vector = properties.getOffset();

		// Create the placeholders.
		Placeholders placeholders = Placeholders
				.of("{particle}", Text.format(properties.getParticle().name()))
				.add("{count}", properties.getCount())
				.add("{offset_x}", vector.getBlockX())
				.add("{offset_y}", vector.getBlockY())
				.add("{offset_z}", vector.getBlockZ())
				.add("{speed}", properties.getSpeed());

		Object data = properties.getData();
		if (data != null) {
			if (data instanceof Particle.DustOptions dust) {
				placeholders.add("{data}", dust.getColor().asRGB());
			} else {
				placeholders.add("{data}", data.toString());
			}
		} else {
			placeholders.add("{data}", "None");
		}

		Messages.PARTICLE_PROPERTIES.replace(placeholders).send(sender);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_PROPERTIES_USAGE);
		properties.setPlayerRequired(true);
	}
}
