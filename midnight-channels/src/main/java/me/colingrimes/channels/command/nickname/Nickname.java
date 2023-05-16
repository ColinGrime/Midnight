package me.colingrimes.channels.command.nickname;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;

import javax.annotation.Nonnull;

public class Nickname implements Command<MidnightChannels> {

	@Override
	public void execute(@Nonnull MidnightChannels plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		String nickname = args.get(0);

		if (nickname.length() > 16) {
			Messages.NICKNAME_TOO_LONG.send(sender);
		} else if (nickname.length() < 3) {
			Messages.NICKNAME_TOO_SHORT.send(sender);
		} else if (!nickname.matches("[a-zA-Z0-9_&]+")) {
			Messages.NICKNAME_NOT_ALPHANUMERIC.send(sender);
		} else if (nickname.matches(".*&[0-9a-f].*") && !sender.hasPermission("channels.nickname.color")) {
			Messages.NICKNAME_COLOR.send(sender);
		} else if (nickname.matches(".*&[k-o].*") && !sender.hasPermission("channels.nickname.format")) {
			Messages.NICKNAME_FORMAT.send(sender);
		} else {
			Chatter.of(sender.player()).setNickname(nickname);
			Messages.NICKNAME_CHANGED.replace("{nickname}", nickname).send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.NICKNAME_USAGE);
		properties.setPermission("channels.nickname");
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
