package me.colingrimes.plugin.command.display.title;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.plugin.Midnight;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DisplayTitle implements Command<Midnight> {

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<Player> player = args.getPlayer(0);
		if (player.isEmpty()) {
			Messages.PLAYER_NOT_FOUND.sendTo(sender);
			return;
		}

		me.colingrimes.midnight.display.implementation.Title title = plugin.display().createTitle(args.get(1));
		args.getOptional(2).ifPresent(title::setSubtitle);
		args.getInt(3).ifPresent(title::setFadeInTime);
		args.getInt(4).ifPresent(title::setStayTime);
		args.getInt(5).ifPresent(title::setFadeOutTime);

		title.show(player.get());
		Messages.TITLE_SHOW.sendTo(sender, Placeholders.of("{player}", player.get().getName()));
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.TITLE_USAGE);
		properties.setArgumentsRequired(2);
	}
}
