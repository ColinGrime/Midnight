package me.colingrimes.channels.command.unignore;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.midnight.util.misc.UUIDs;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Unignore implements Command<MidnightChannels> {

	@Override
	public void execute(@Nonnull MidnightChannels plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		String name = args.get(0);
		Optional<UUID> uuid = UUIDs.fromName(name);

		if (name.equalsIgnoreCase(sender.player().getName())) {
			Messages.UNIGNORE_SELF.send(sender);
		} else if (uuid.isEmpty()) {
			Messages.PLAYER_NOT_FOUND.send(sender);
		} else if (Chatter.of(sender.player()).unignore(uuid.get())) {
			Messages.UNIGNORED.replace("{player}", name).send(sender);
		} else {
			Messages.NOT_IGNORING.replace("{player}", name).send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull MidnightChannels plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Players.mapList(Player::getName);
		} else {
			return null;
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.UNIGNORE_USAGE);
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
