package me.colingrimes.example.listener;

import me.colingrimes.example.config.OtherSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListeners implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		event.getPlayer().sendMessage(String.valueOf(OtherSettings.exampleNumber.get()));
	}
}
