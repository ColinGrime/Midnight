package me.colingrimes.particles.command.particle.rotate;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.config.Messages;
import me.colingrimes.particles.particle.ParticleEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ParticleRotate implements Command<MidnightParticles> {

	@Override
	public void execute(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<ParticleEffect> effect = plugin.getParticleManager().getSelectedParticle(sender.player());
		if (effect.isEmpty()) {
			Messages.PARTICLE_NOT_SELECTED.send(sender);
			return;
		}

		Rotation rotation = effect.get().getPoint().getDirection();
		double value = args.getDoubleOrDefault(1, 0);

		switch (args.getLowercase(0)) {
			case "yaw" -> rotation = Rotation.of(value, rotation.getPitch(), rotation.getRoll());
			case "pitch" -> rotation = Rotation.of(rotation.getYaw(), value, rotation.getRoll());
			case "roll" -> rotation = Rotation.of(rotation.getYaw(), rotation.getPitch(), value);
			default -> Messages.INVALID_ROTATION.send(sender);
		}

		effect.get().setPoint(Point.of(effect.get().getPoint().getPosition(), rotation));
		Messages.PARTICLE_ROTATE.send(sender);
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull MidnightParticles plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return List.of("yaw", "pitch", "roll");
		} else {
			return null;
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_ROTATE_USAGE);
		properties.setArgumentsRequired(2);
		properties.setPlayerRequired(true);
	}
}
