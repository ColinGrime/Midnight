package me.colingrimes.plugin.storage;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.storage.file.JsonStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class ParticleStorage extends JsonStorage<ParticleEffect> {

	private final MidnightPlugin plugin;

	public ParticleStorage(@Nonnull MidnightPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Nonnull
	@Override
	public Optional<String> getIdentifier(@Nullable ParticleEffect data) {
		if (data == null) {
			return Optional.empty();
		}
		return Optional.of(data.getUUID().toString());
	}

	@Override
	protected void loadData(@Nonnull Map<String, ParticleEffect> dataMap) {
		plugin.getParticleManager().loadParticles(dataMap);
	}
}
