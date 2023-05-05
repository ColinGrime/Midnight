package me.colingrimes.example.command.deaths;

import me.colingrimes.example.ExamplePlugin;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.util.CommandProperties;
import me.colingrimes.midnight.command.util.Sender;
import me.colingrimes.midnight.command.util.argument.ArgumentList;

import javax.annotation.Nonnull;
import java.util.UUID;

public class Deaths implements Command<ExamplePlugin> {

    @Override
    public void execute(@Nonnull ExamplePlugin plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
        UUID uuid = sender.player().getUniqueId();
        sender.message("You have died " + plugin.getPlayerManager().getPlayerData(uuid).getTimesKilled() + " times.");
    }

    @Override
    public void configureProperties(@Nonnull CommandProperties properties) {
        properties.setPlayerRequired(true);
    }
}
