package me.colingrimes.plugin.command.particle.list;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.util.Locations;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;

import javax.annotation.Nonnull;
import java.util.List;

public class ParticleList implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		List<ParticleEffect> particles = plugin.getParticleManager().getParticles();
		int page = args.getIntOrDefault(0, 1);
		int totalPages = (int) Math.ceil(particles.size() / 10.0);

		// Check for invalid page.
		if (page < 1 || page > totalPages) {
			Messages.INVALID_PAGE.sendTo(sender);
			page = 1;
		}

		int startIndex = (page - 1) * 10;
		int endIndex = Math.min(startIndex + 10, particles.size());

		// Send the head message.
		Placeholders header = Placeholders.of("{page}", page).add("{total}", totalPages);
		Messages.PARTICLE_PAGE_HEADER.sendTo(sender, header);

		for (int i=startIndex; i<endIndex; i++) {
			ParticleEffect effect = particles.get(i);

			// Send the entry message.
			Placeholders entry = Placeholders
					.of("{name}", Text.format(effect.getName()))
					.add("{type}", Text.format(effect.getType().name()))
					.add("{location}", Locations.toString(effect.getPoint().getPosition().toLocation()));
			Messages.PARTICLE_PAGE_ENTRY.sendTo(sender, entry);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.PARTICLE_LIST_USAGE);
	}
}
