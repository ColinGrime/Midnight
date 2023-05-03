package me.colingrimes.example.command.lol;

import me.colingrimes.example.ExamplePlugin;
import me.colingrimes.example.menu.LolMenu;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;

import javax.annotation.Nonnull;

public class LolCommand implements Command<ExamplePlugin> {

	@Override
	public void execute(@Nonnull ExamplePlugin plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		new LolMenu(sender.player()).open();
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
