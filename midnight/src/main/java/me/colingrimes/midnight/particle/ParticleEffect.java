package me.colingrimes.midnight.particle;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.particle.implementation.type.CircleParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleEffectType;
import me.colingrimes.midnight.particle.util.ParticleProperties;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import me.colingrimes.midnight.serialize.Serializable;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public interface ParticleEffect extends Serializable {

    /**
     * Gets the type of the particle effect.
     * @return the type of the particle effect
     */
    @Nonnull
    ParticleEffectType getType();

    /**
     * Spawns the particle effect.
     */
    void spawn();

    /**
     * Starts spawning particles at a fixed interval.
     */
    void startSpawning();

    /**
     * Stops spawning particles.
     */
    void stopSpawning();

    /**
     * Attaches the particle effect to the specified entity.
     * @param entity the entity to attach to
     */
    void attach(@Nonnull Entity entity);

    /**
     * Detaches the particle effect from the entity it is attached to.
     */
    void detach();

    /**
     * Gets the point where the particle effect is located.
     * @return the point of the particle effect
     */
    @Nonnull
    Point<Rotation> getPoint();

    /**
     * Sets the point where the particle effect is located.
     * @param point the point of the particle effect
     */
    void setPoint(@Nonnull Point<Rotation> point);

    /**
     * Gets the properties of the particle effect.
     * @return the properties of the particle effect
     */
    @Nonnull
    ParticleProperties getProperties();

    /**
     * Updates the property of the particle effect with the specified value.
     * @param property the property to update
     * @param value the new value for the property
     * @throws UnsupportedOperationException if the property is not supported by the particle effect
     */
    void updateProperty(@Nonnull ParticleProperty property, @Nonnull String value) throws UnsupportedOperationException;

    /**
     * Gets the current value of the specified property.
     * @param property the property to get the value for
     * @return the current value of the property
     * @throws UnsupportedOperationException if the property is not supported by the particle effect
     */
    @Nonnull
    Object getProperty(@Nonnull ParticleProperty property) throws UnsupportedOperationException;

    @Nonnull
    @Override
    default Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType().name());
        map.put("point", getPoint().serialize());
        map.put("properties", getProperties().serialize());
        return map;
    }

    /**
     * Deserializes a particle effect from the specified map.
     * @param map the map to deserialize from
     * @return the deserialized particle effect
     */
    @Nonnull
    static ParticleEffect deserialize(@Nonnull Map<String, Object> map) {
        Preconditions.checkArgument(map.containsKey("type"));
        Preconditions.checkArgument(ParticleEffectType.fromString((String) map.get("type")).isPresent());
        ParticleEffectType type = ParticleEffectType.fromString((String) map.get("type")).get();

        return switch (type) {
            case CIRCLE -> CircleParticleEffect.deserialize(map);
            default -> throw new IllegalArgumentException("Unknown particle effect type: " + type.name());
        };
    }
}
