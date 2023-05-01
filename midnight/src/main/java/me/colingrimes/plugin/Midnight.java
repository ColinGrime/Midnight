package me.colingrimes.plugin;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.plugin.storage.ParticleStorage;

import javax.annotation.Nonnull;

public class Midnight extends MidnightPlugin {

	private ParticleStorage particleStorage;

	@Override
	protected void load() {
		particleStorage = new ParticleStorage(this);

		try {
			particleStorage.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
