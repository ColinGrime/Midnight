package me.colingrimes.midnight.particle.implementation;

import me.colingrimes.midnight.model.Point;
import me.colingrimes.midnight.model.Rotation;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.implementation.type.CircleParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleEffectType;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public final class ParticleEffectFactory {

    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull Player player) {
        return create(type, player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
    }

    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull World world, double x, double y, double z) {
        return create(type, world, x, y, z, 0, 0, 0);
    }

    @Nonnull
    public static ParticleEffect create(@Nonnull ParticleEffectType type, @Nonnull Player player, double pitch, double yaw, double roll) {
        return create(type, player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), pitch, yaw, roll);
    }

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
