package me.colingrimes.plugin.command.particle.modify;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParticleModify implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> effect = plugin.getParticleManager().getSelectedParticle(sender.player());
		if (effect.isEmpty()) {
			return;
		}

		ParticleProperty property = ParticleProperty.fromString(args.get(0));
		effect.get().updateProperty(property, args.get(1));
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Arrays.stream(ParticleProperty.values()).map(p -> p.name().toLowerCase()).collect(Collectors.toList());
		} else {
			return null;
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_MODIFY_USAGE);
		properties.setArgumentsRequired(2);
		properties.setPlayerRequired(true);
	}
}
