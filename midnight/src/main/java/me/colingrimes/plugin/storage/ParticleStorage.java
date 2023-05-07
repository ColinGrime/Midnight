package me.colingrimes.plugin.storage;

import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.storage.file.YamlStorage;
import me.colingrimes.midnight.storage.file.composite.CompositeIdentifier;
import me.colingrimes.plugin.MidnightPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
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

	@Nullable
	@Override
	protected CompositeIdentifier getIdentifier(@Nullable ParticleEffect data) {
		if (data == null) {
			return CompositeIdentifier.of("particles.yml");
		} else {
			return CompositeIdentifier.of("particles.yml", data.getUUID().toString());
		}
	}

	@Nonnull
	@Override
	protected Function<Map<String, Object>, ParticleEffect> getDeserializationFunction() {
		return ParticleEffect::deserialize;
	}
}
