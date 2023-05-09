package me.colingrimes.particles.command.particle.list;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.util.bukkit.Locations;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.MidnightTemp;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.List;

public class ParticleList implements Command<MidnightTemp> {

	@Override
	public void execute(@Nonnull MidnightTemp plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		List<ParticleEffect> particles = plugin.getParticleManager().getParticles();
		int page = args.getIntOrDefault(0, 1);
		int totalPages = Math.max(1, (int) Math.ceil(particles.size() / 10.0));

		// Check for invalid page.
		if (page < 1 || page > totalPages) {
			Messages.INVALID_PAGE.send(sender);
			page = 1;
		}

		int startIndex = (page - 1) * 10;
		int endIndex = Math.min(startIndex + 10, particles.size());

		// Send the head message.
		Messages.PARTICLE_PAGE_HEADER
				.replace("{page}", page)
				.replace("{total}", totalPages)
				.send(sender);

		for (int i=startIndex; i<endIndex; i++) {
			ParticleEffect particle = particles.get(i);

			// Send the entry message.
			Messages.PARTICLE_PAGE_ENTRY
					.replace("{name}", Text.format(particle.getName()))
					.replace("{type}", Text.format(particle.getType().name()))
					.replace("{location}", Locations.toString(particle.getPoint().getPosition().toLocation()))
					.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_LIST_USAGE);
	}
}
