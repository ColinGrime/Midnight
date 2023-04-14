package me.colingrimes.example.command;

import me.colingrimes.midnight.command.annotation.Command;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Commands {

	@Command("echooo say")
	public void onCommand(@Nonnull Player player, @Nonnull String echo) {
		player.sendMessage(echo);
	}
}
