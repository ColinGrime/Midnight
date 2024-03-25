package me.colingrimes.channels.listener;

import me.colingrimes.channels.ChannelAPI;
import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.channel.implementation.GlobalChannel;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class ChatListeners implements Listener {

	private final MidnightChannels plugin;

	public ChatListeners(@Nonnull MidnightChannels plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(@Nonnull AsyncPlayerChatEvent event) {
		Optional<Chatter> chatter = plugin.getChatManager().getChatter(event.getPlayer().getUniqueId());

		// This should never be called.
		if (chatter.isEmpty()) {
			Messages.PLAYER_NOT_LOADED.send(event.getPlayer());
			return;
		}

		// Re-direct to global channel if enabled.
		if (ChannelAPI.global().isEnabled()) {
			event.setCancelled(true);
			Scheduler.sync().run(() -> ((GlobalChannel) ChannelAPI.global()).send(event));
		}
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		Scheduler.async().execute(() -> {
			plugin.getChatterStorage().load(uuid);
		}).exceptionally((e) -> {
			Logger.severe("Failed to load chatter: " + uuid);
			e.printStackTrace();
			return null;
		});
	}

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		Chatter chatter = Chatter.of(event.getPlayer());
		Scheduler.async().execute(() -> {
			plugin.getChatterStorage().save(chatter);
		}).exceptionally((e) -> {
			Logger.severe("Failed to save chatter: " + chatter.getID());
			e.printStackTrace();
			return null;
		});
	}
}
