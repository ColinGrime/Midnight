package me.colingrimes.particles.command.particle.modify;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.MidnightTemp;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.Particle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParticleModify implements Command<MidnightTemp> {

	@Override
	public void execute(@Nonnull MidnightTemp plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> particle = plugin.getParticleManager().getSelectedParticle(sender.player());
		Optional<ParticleProperty> property = ParticleProperty.fromString(args.get(0));

		// Check if a particle is selected.
		if (particle.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
			return;
		}

		// Check if the property is valid.
		if (property.isEmpty()) {
			Messages.INVALID_PROPERTY.send(sender);
			return;
		}

		// Attempt to update the property.
		try {
			particle.get().updateProperty(property.get(), args.get(1));
			Messages.PARTICLE_MODIFY
					.replace("{property}", Text.format(args.get(0)))
					.replace("{value}", Text.format(args.get(1)))
					.send(sender);
		} catch (UnsupportedOperationException e) {
			Messages.INVALID_PROPERTY_VALUE.send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull MidnightTemp plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Arrays.stream(ParticleProperty.values()).map(p -> p.name().toLowerCase()).collect(Collectors.toList());
		}

		Optional<ParticleProperty> property = ParticleProperty.fromString(args.get(0));
		if (property.isPresent() && property.get() == ParticleProperty.TYPE) {
			return Arrays.stream(Particle.values()).map(p -> p.name().toLowerCase()).collect(Collectors.toList());
		}

		return null;
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_MODIFY_USAGE);
		properties.setArgumentsRequired(2);
		properties.setPlayerRequired(true);
	}
}
