package me.colingrimes.plugin;

import me.colingrimes.midnight.listener.MenuListeners;
import me.colingrimes.plugin.storage.ParticleStorage;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class MidnightPlugin extends me.colingrimes.midnight.Midnight {

	private static MidnightPlugin instance;
	private ParticleStorage particleStorage;

	@Override
	public void onLoad() {
		if (instance == null) {
			instance = this;
		}
	}

	@Override
	protected void enable() {
		particleStorage = new ParticleStorage(this);

		try {
			particleStorage.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void registerListeners(@Nonnull List<Listener> listeners) {
		listeners.add(new MenuListeners(this));
	}

	/**
	 * Gets the instance of the plugin.
	 * @return instance of the plugin
	 */
	@Nonnull
	public static MidnightPlugin getInstance() {
		return instance;
	}

	/**
	 * Gets the particle storage.
	 * @return the particle storage
	 */
	@Nonnull
	public ParticleStorage getParticleStorage() {
		return particleStorage;
	}
}
