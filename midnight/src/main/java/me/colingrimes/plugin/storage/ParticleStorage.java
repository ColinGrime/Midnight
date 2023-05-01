package me.colingrimes.plugin.storage;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.storage.file.YamlStorage;
import me.colingrimes.midnight.storage.file.composite.CompositeIdentifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ParticleStorage extends YamlStorage<ParticleEffect> {

	private final MidnightPlugin plugin;

	public ParticleStorage(@Nonnull MidnightPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	protected void process(@Nonnull ParticleEffect data) {
		plugin.getParticleManager().addParticle(data);
	}

	@Nonnull
	@Override
	protected Optional<CompositeIdentifier> getIdentifier(@Nullable ParticleEffect data) {
		if (data == null) {
			return Optional.of(new CompositeIdentifier("particles.yml", null));
		} else {
			return Optional.of(new CompositeIdentifier("particles.yml", data.getUUID().toString()));
		}
	}

	@Nonnull
	@Override
	protected Function<Map<String, Object>, ParticleEffect> getDeserializationFunction() {
		return ParticleEffect::deserialize;
	}
}
