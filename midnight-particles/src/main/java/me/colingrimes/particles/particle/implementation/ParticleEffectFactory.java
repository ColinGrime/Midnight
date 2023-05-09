package me.colingrimes.particles.particle.implementation;

import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.particles.particle.ParticleEffect;
import me.colingrimes.particles.particle.implementation.type.CircleParticleEffect;
import me.colingrimes.particles.particle.util.ParticleEffectType;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public final class ParticleEffectFactory {

    /**
     * Creates a particle effect at the player's location.
     * @param type the type of particle effect to create
     * @param player the player to create the particle effect at
     * @return the particle effect
     */
    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull Player player) {
        return create(type, player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
    }

    /**
     * Creates a particle effect at the given location.
     * @param type the type of particle effect to create
     * @param world the world to create the particle effect in
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the particle effect
     */
    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull World world, double x, double y, double z) {
        return create(type, world, x, y, z, 0, 0, 0);
    }

    /**
     * Creates a particle effect at the player's location with the given rotation.
     * @param type the type of particle effect to create
     * @param player the player to create the particle effect at
     * @param pitch the pitch
     * @param yaw the yaw
     * @param roll the roll
     * @return the particle effect
     */
    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull Player player, double pitch, double yaw, double roll) {
        return create(type, player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), pitch, yaw, roll);
    }

    /**
     * Creates a particle effect at the given location with the given rotation.
     * @param type the type of particle effect to create
     * @param world the world to create the particle effect in
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param pitch the pitch
     * @param yaw the yaw
     * @param roll the roll
     * @return the particle effect
     */
    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull World world, double x, double y, double z, double pitch, double yaw, double roll) {
        Point<Rotation> point = Point.of(world, x, y, z, Rotation.of(pitch, yaw, roll));

        return switch (type) {
            case CIRCLE -> new CircleParticleEffect(point);
            default -> throw new IllegalArgumentException("Unsupported particle effect type: " + type);
        };
    }

    private ParticleEffectFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
