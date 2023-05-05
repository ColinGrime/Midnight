package me.colingrimes.example.listener;

import me.colingrimes.example.ExamplePlugin;
import me.colingrimes.example.player.PlayerData;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.storage.file.composite.CompositeIdentifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PlayerListeners implements Listener {

	private final ExamplePlugin plugin;

	public PlayerListeners(@Nonnull ExamplePlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		PlayerData playerData = plugin.getPlayerManager().getPlayerData(event.getEntity().getUniqueId());
		playerData.incrementTimesKilled();
		Scheduler.ASYNC.call(() -> {
			plugin.getPlayerStorage().save(playerData);
			return null;
		});
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		Scheduler.ASYNC.call(() -> {
			plugin.getPlayerStorage().load(CompositeIdentifier.of("player-data/" + uuid));
			return null;
		});
	}
}
