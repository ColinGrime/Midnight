package me.colingrimes.example;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.colingrimes.example.listener.PlayerListeners;
import me.colingrimes.example.player.PlayerManager;
import me.colingrimes.example.storage.PlayerStorage;
import me.colingrimes.midnight.MidnightPlugin;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class ExamplePlugin extends MidnightPlugin {

	private PlayerStorage playerStorage;
	private PlayerManager playerManager;

	@Override
	protected void load() {
		playerStorage = new PlayerStorage(this);
		playerManager = new PlayerManager();
	}

	@Override
	protected void enable() {
		depend(WorldEditPlugin.class, "WorldEdit");
	}

	@Override
	protected void registerListeners(@Nonnull List<Listener> listeners) {
		listeners.add(new PlayerListeners(this));
	}

	/**
	 * Gets the player storage.
	 * @return the player storage
	 */
	@Nonnull
	public PlayerStorage getPlayerStorage() {
		return playerStorage;
	}

	/**
	 * Gets the player manager.
	 * @return the player manager
	 */
	@Nonnull
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
}
