package me.colingrimes.midnight.particle;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ParticleManager {

    private final Map<Player, ParticleEffect> selectedParticles = new HashMap<>();

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
        selectedParticles.put(player, particleEffect);
    }
}
