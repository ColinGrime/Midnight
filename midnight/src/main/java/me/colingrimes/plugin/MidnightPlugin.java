package me.colingrimes.plugin;

import me.colingrimes.plugin.listener.ArmorEquipListeners;
import me.colingrimes.plugin.listener.DisplayListeners;
import me.colingrimes.plugin.listener.InventoryListener;
import me.colingrimes.plugin.listener.MenuListeners;
import me.colingrimes.plugin.manager.DisplayManager;
import me.colingrimes.plugin.manager.ParticleManager;
import me.colingrimes.plugin.storage.ParticleStorage;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class MidnightPlugin extends me.colingrimes.midnight.Midnight {

	private static MidnightPlugin instance;

	// Managers:
	private ParticleManager particleManager;
	private DisplayManager displayManager;

	// Storages:
	private ParticleStorage particleStorage;

	@Override
	public void onLoad() {
		if (instance == null) {
			instance = this;
		}

		particleManager = new ParticleManager();
		displayManager = new DisplayManager();
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
		listeners.add(new InventoryListener());
		listeners.add(new ArmorEquipListeners());
		listeners.add(new MenuListeners(this));
		listeners.add(new DisplayListeners(this));
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
	 * Gets the particle manager.
	 * @return particle manager
	 */
	@Nonnull
	public ParticleManager getParticleManager() {
		return particleManager;
	}

	/**
	 * Gets the particle storage.
	 * @return the particle storage
	 */
	@Nonnull
	public ParticleStorage getParticleStorage() {
		return particleStorage;
	}

	/**
	 * Gets the display manager.
	 * @return display manager
	 */
	@Nonnull
	public DisplayManager getDisplayManager() {
		return displayManager;
	}
}
