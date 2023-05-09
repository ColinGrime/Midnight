package me.colingrimes.particles.command.particle.create;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.implementation.ParticleEffectFactory;
import me.colingrimes.midnight.particle.util.ParticleEffectType;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.MidnightTemp;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParticleCreate implements Command<MidnightTemp> {

	@Override
	public void execute(@Nonnull MidnightTemp plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Player player = sender.player();
		Optional<ParticleEffectType> particleType = ParticleEffectType.fromString(args.get(0));
		Optional<ParticleEffect> selectedParticle = plugin.getParticleManager().getSelectedParticle(player);

		// Check if the particle type does not exist.
		if (particleType.isEmpty()) {
			Messages.PARTICLE_NOT_FOUND.send(sender);
			return;
		}

		// Clear all selected particles.
		if (selectedParticle.isPresent()) {
			selectedParticle.get().stopSpawning();
			plugin.getParticleManager().removeSelectedParticle(player);
		}

		ParticleEffect particle = ParticleEffectFactory.create(particleType.get(), player);
		particle.startSpawning();
		plugin.getParticleManager().selectParticle(player, particle);
		Messages.PARTICLE_CREATE.replace("{type}", Text.format(particleType.get().name())).send(sender);

		// Attach the particle if the player is specified.
		if (args.getPlayer(1).isPresent()) {
			particle.attach(args.getPlayer(1).get());
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull MidnightTemp plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Arrays.stream(ParticleEffectType.values()).map(p -> p.name().toLowerCase()).collect(Collectors.toList());
		}

		return null;
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_CREATE_USAGE);
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}