package me.colingrimes.midnight.particle;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ParticleManager {

    private final Map<UUID, ParticleEffect> particleEffects = new HashMap<>();
    private final Map<Player, ParticleEffect> selectedParticles = new HashMap<>();

    /**
     * Gets the particle effect with the given name.
     * @param name the name of the particle effect
     * @return the particle effect, if any
     */
    @Nonnull
    public Optional<ParticleEffect> getParticle(@Nonnull String name) {
        for (ParticleEffect particle : particleEffects.values()) {
            if (particle.getName().equalsIgnoreCase(name)) {
                return Optional.of(particle);
            }
        }
        return Optional.empty();
    }

    /**
     * Adds a particle effect.
     * @param particle the particle effect to add
     */
    public void addParticle(@Nonnull ParticleEffect particle) {
        particleEffects.put(particle.getUUID(), particle);
    }

    /**
     * Loads the particle effects.
     * @param particles the particle effects to load
     */
    public void loadParticles(@Nonnull Map<String, ParticleEffect> particles) {
        for (ParticleEffect particle : particles.values()) {
            addParticle(particle);
        }
    }

    /**
     * Gets the selected particle effect for a player.
     * @param player the player to get the selected particle effect for
     * @return the selected particle effect, if any
     */
    @Nonnull
    public Optional<ParticleEffect> getSelectedParticle(@Nonnull Player player) {
        return Optional.ofNullable(selectedParticles.get(player));
    }

    /**
     * Selects a particle effect for a player.
     * @param player the player that selected the particle effect
     * @param particleEffect the particle effect that was selected
     */
    public void selectParticle(@Nonnull Player player, @Nonnull ParticleEffect particleEffect) {
        if (getParticle(particleEffect.getName()).isEmpty()) {
            addParticle(particleEffect);
        }
        selectedParticles.put(player, particleEffect);
    }
}
