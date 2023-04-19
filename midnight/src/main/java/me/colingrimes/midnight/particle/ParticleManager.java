package me.colingrimes.midnight.particle;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ParticleManager {

    private final Map<Player, ParticleEffect> selectedParticles = new HashMap<>();

    @Nonnull
    public Optional<ParticleEffect> getSelectedParticle(@Nonnull Player player) {
        return Optional.ofNullable(selectedParticles.get(player));
    }

    public void selectParticle(@Nonnull Player player, @Nonnull ParticleEffect particleEffect) {
        selectedParticles.put(player, particleEffect);
    }
}
