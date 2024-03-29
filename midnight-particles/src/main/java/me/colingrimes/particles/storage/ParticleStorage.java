package me.colingrimes.particles.storage;

import me.colingrimes.midnight.storage.file.YamlStorage;
import me.colingrimes.midnight.storage.file.composite.Identifier;
import me.colingrimes.particles.MidnightParticles;
import me.colingrimes.particles.particle.ParticleEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public class ParticleStorage extends YamlStorage<ParticleEffect> {

	private final MidnightParticles plugin;

	public ParticleStorage(@Nonnull MidnightParticles plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	protected void process(@Nonnull ParticleEffect data) {
		plugin.getParticleManager().addParticle(data);
	}

	@Nullable
	@Override
	protected String getDefaultFileName() {
		return "particles.yml";
	}

	@Override
	protected void configureIdentifier(@Nonnull Identifier identifier, @Nonnull ParticleEffect data) {
		identifier.setInternalPath(data.getUUID().toString());
	}

	@Nonnull
	@Override
	protected Function<Map<String, Object>, ParticleEffect> getDeserializationFunction() {
		return ParticleEffect::deserialize;
	}
}
