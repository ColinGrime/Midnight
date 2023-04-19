package me.colingrimes.midnight.particle;

import me.colingrimes.midnight.model.Point;
import me.colingrimes.midnight.model.Rotation;
import me.colingrimes.midnight.particle.util.ParticleProperties;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

public interface ParticleEffect {

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
}
