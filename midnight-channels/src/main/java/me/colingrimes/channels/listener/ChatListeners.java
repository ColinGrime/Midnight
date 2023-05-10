package me.colingrimes.channels.listener;

import me.colingrimes.channels.MidnightChannels;
import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.channels.config.Settings;
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

public class ChatListeners implements Listener {

	private final MidnightChannels plugin;

	public ChatListeners(@Nonnull MidnightChannels plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChat(@Nonnull AsyncPlayerChatEvent event) {
		Optional<Participant> participant = plugin.getChannelManager().getParticipant(event.getPlayer().getUniqueId());

		// This should never be called.
		if (participant.isEmpty()) {
			Settings.PLAYER_NOT_LOADED.send(event.getPlayer());
			return;
		}

		// Re-direct to channeling system if active.
		Channel activeChannel = participant.get().getActiveChannel();
		if (activeChannel != null) {
			activeChannel.send(participant.get(), event.getMessage());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		Scheduler.ASYNC.execute(() -> {
			plugin.getParticipantStorage().load(event.getPlayer().getUniqueId());
		}).exceptionally((e) -> {
			Logger.severe("Failed to load participant: " + event.getPlayer().getUniqueId());
			e.printStackTrace();
			return null;
		});
	}

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		Scheduler.ASYNC.execute(() -> {
			plugin.getParticipantStorage().save(plugin.getChannelManager().getParticipant(event.getPlayer()));
		}).exceptionally((e) -> {
			Logger.severe("Failed to save participant: " + event.getPlayer().getUniqueId());
			e.printStackTrace();
			return null;
		});
	}
}
