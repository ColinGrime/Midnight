package me.colingrimes.particles;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.particles.manager.ParticleManager;
import me.colingrimes.particles.storage.ParticleStorage;

import javax.annotation.Nonnull;

public class MidnightParticles extends Midnight {

	private final ParticleManager particleManager = new ParticleManager();
	private ParticleStorage particleStorage;

	@Override
	public void onLoad() {
		particleStorage = new ParticleStorage(this);
	}

	/**
	 * Gets the particle manager.
	 *
	 * @return the particle manager
	 */
	@Nonnull
	public ParticleManager getParticleManager() {
		return particleManager;
	}

	/**
	 * Gets the particle storage.
	 *
	 * @return the particle storage
	 */
	@Nonnull
	public ParticleStorage getParticleStorage() {
		return particleStorage;
	}
}
