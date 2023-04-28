package me.colingrimes.plugin.command.display.bossbar;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Optional;

public class DisplayBossBar implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<Player> player = args.getPlayer(0);
		if (player.isEmpty()) {
			Messages.PLAYER_NOT_FOUND.sendTo(sender);
			return;
		}

		String text = args.get(1);
		Display display = plugin.display().createBossBar(text);
		Optional<Duration> duration = args.getDuration(2);

		if (duration.isPresent()) {
			display.show(player.get(), (int) duration.get().toSeconds());
		} else {
			display.show(player.get());
		}

		Messages.BOSSBAR_SHOW.sendTo(sender, Placeholders.of("{player}", player.get().getName()));
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.BOSSBAR_USAGE);
		properties.setArgumentsRequired(2);
	}
}
