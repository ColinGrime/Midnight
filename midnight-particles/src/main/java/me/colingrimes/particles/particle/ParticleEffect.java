package me.colingrimes.particles.particle;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.util.misc.Validate;
import me.colingrimes.particles.particle.implementation.type.CircleParticleEffect;
import me.colingrimes.particles.particle.implementation.type.SquareParticleEffect;
import me.colingrimes.particles.particle.util.ParticleEffectType;
import me.colingrimes.particles.particle.util.ParticleProperties;
import me.colingrimes.particles.particle.util.ParticleProperty;
import me.colingrimes.midnight.serialize.Serializable;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface ParticleEffect extends Serializable {

    /**
     * Gets the type of the particle effect.
     *
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
     *
     * @param entity the entity to attach to
     */
    void attach(@Nonnull Entity entity);

    /**
     * Detaches the particle effect from the entity it is attached to.
     */
    void detach();

    /**
     * Gets the UUID of the particle effect.
     *
     * @return the UUID of the particle effect
     */
    @Nonnull
    UUID getUUID();

    /**
     * Gets the name of the particle effect.
     *
     * @return the name of the particle effect
     */
    @Nonnull
    String getName();

    /**
     * Sets the name of the particle effect.
     *
     * @param name the name of the particle effect
     */
    void setName(@Nonnull String name);

    /**
     * Gets the point where the particle effect is located.
     *
     * @return the point of the particle effect
     */
    @Nonnull
    Point<Rotation> getPoint();

    /**
     * Sets the point where the particle effect is located.
     *
     * @param point the point of the particle effect
     */
    void setPoint(@Nonnull Point<Rotation> point);

    /**
     * Gets the properties of the particle effect.
     *
     * @return the properties of the particle effect
     */
    @Nonnull
    ParticleProperties getProperties();

    /**
     * Sets the properties of the particle effect.
     *
     * @param properties the properties of the particle effect
     */
    void setProperties(@Nonnull ParticleProperties properties);

    /**
     * Updates the property of the particle effect with the specified value.
     *
     * @param property the property to update
     * @param value    the new value for the property
     * @throws UnsupportedOperationException if the property is not supported by the particle effect
     */
    void updateProperty(@Nonnull ParticleProperty property, @Nonnull String value) throws UnsupportedOperationException;

    /**
     * Gets the current value of the specified property.
     *
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
        map.put("uuid", getUUID().toString());
        map.put("name", getName());
        map.put("point", getPoint().serialize());
        map.put("properties", getProperties().serialize());
        return map;
    }

    @Nonnull
    static ParticleEffect deserialize(@Nonnull Map<String, Object> map) {
        Validate.checkMap(map, "type");
        Preconditions.checkArgument(ParticleEffectType.fromString((String) map.get("type")).isPresent());

        return switch (ParticleEffectType.fromString((String) map.get("type")).get()) {
            case CIRCLE -> CircleParticleEffect.deserialize(map);
            case SQUARE -> SquareParticleEffect.deserialize(map);
        };
    }
}
