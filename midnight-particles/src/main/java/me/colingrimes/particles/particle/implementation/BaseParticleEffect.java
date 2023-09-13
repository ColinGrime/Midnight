package me.colingrimes.particles.particle.implementation;

import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.task.Task;
import me.colingrimes.particles.particle.ParticleEffect;
import me.colingrimes.particles.particle.util.ParticleProperties;
import me.colingrimes.particles.particle.util.ParticleProperty;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public abstract class BaseParticleEffect implements ParticleEffect {

    private final UUID uuid;
    private String name;
    private Point<Rotation> point;
    private ParticleProperties properties;
    private Entity entity;
    private Task task;

    /**
     * Constructor for creating new particle effects.
     *
     * @param point      the point to spawn the particle effect at
     * @param properties the properties of the particle effect
     */
    public BaseParticleEffect(@Nonnull Point<Rotation> point, @Nonnull ParticleProperties properties) {
        this(UUID.randomUUID(), "Unnamed", point, properties);
    }

    /**
     * Constructor for deserializing particle effects.
     *
     * @param uuid       the UUID of the particle effect
     * @param name       the name of the particle effect
     * @param point      the point to spawn the particle effect at
     * @param properties the properties of the particle effect
     */
    public BaseParticleEffect(@Nonnull UUID uuid, @Nonnull String name, @Nonnull Point<Rotation> point, @Nonnull ParticleProperties properties) {
        this.uuid = uuid;
        this.name = name;
        this.point = point;
        this.properties = properties;
    }

    /**
     * Spawns a particle at the effect's point.
     */
    protected abstract void spawnParticle();

    @Override
    public void spawn() {
        // If the entity has moved, update the point's position.
        if (entity != null && !entity.getLocation().equals(point.getPosition().toLocation())) {
            setPoint(point.setPosition(Position.of(entity.getLocation())));
        }

        // Spawn the particle.
        spawnParticle();
    }

    @Override
    public void startSpawning() {
        // Don't start another task if one is already running.
        if (task != null && !task.isCancelled()) {
            return;
        }

        // Start the particle spawning task.
        task = Scheduler.SYNC.runRepeating(this::spawn, 0L, 10L);
    }

    @Override
    public void stopSpawning() {
        if (task != null) {
            task.stop();
            task = null;
        }
    }

    @Override
    public void attach(@Nonnull Entity entity) {
        this.entity = entity;
    }

    @Override
    public void detach() {
        this.entity = null;
    }

    @Nonnull
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public Point<Rotation> getPoint() {
        return point;
    }

    @Override
    public void setPoint(@Nonnull Point<Rotation> point) {
        this.point = point;
    }

    @Nonnull
    @Override
    public ParticleProperties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(@Nonnull ParticleProperties properties) {
        this.properties = properties;
    }

    @Override
    public void updateProperty(@Nonnull ParticleProperty property, @Nonnull String value) {
        Object parsedValue = property.parseValue(value);
        if (parsedValue != null) {
            updatePropertyValue(property, parsedValue);
        }
    }

    /**
     * Updates the property of the particle effect with the specified value.
     *
     * @param property the property to update
     * @param value    the new value for the property
     * @throws UnsupportedOperationException if the property is not supported by the particle effect
     */
    protected void updatePropertyValue(@Nonnull ParticleProperty property, @Nonnull Object value) {
        switch (property) {
            case TYPE -> properties.setParticle((Particle) value);
            case COUNT -> properties.setCount((int) value);
            case OFFSET -> properties.setOffset((Vector) value);
            case SPEED -> properties.setSpeed((double) value);
            case COLOR -> properties.setColor((Color) value);
            default -> throw new UnsupportedOperationException("This property is not supported by this particle effect.");
        }
    }

    @Nonnull
    @Override
    public Object getProperty(@Nonnull ParticleProperty property) {
        return switch (property) {
            case TYPE -> properties.getParticle();
            case COUNT -> properties.getCount();
            case OFFSET -> properties.getOffset();
            case SPEED -> properties.getSpeed();
            case COLOR -> properties.getData();
            default -> throw new UnsupportedOperationException("This property is not supported by this particle effect.");
        };
    }

    protected void spawnParticle(@Nonnull Location location) {
        world().spawnParticle(
                particle(),
                location,
                count(),
                offset().getX(),
                offset().getY(),
                offset().getZ(),
                speed(),
                data());
    }

    @Nonnull
    protected Position position() {
        return point.getPosition();
    }

    @Nonnull
    protected Rotation rotation() {
        return point.getDirection();
    }

    @Nonnull
    protected World world() {
        return point.getWorld();
    }

    protected double x() {
        return point.getX();
    }

    protected double y() {
        return point.getY();
    }

    protected double z() {
        return point.getZ();
    }

    protected double yaw() {
        return rotation().getYaw();
    }

    protected double pitch() {
        return rotation().getPitch();
    }

    protected double roll() {
        return rotation().getRoll();
    }

    @Nonnull
    protected Particle particle() {
        return properties.getParticle();
    }

    protected int count() {
        return properties.getCount();
    }

    @Nonnull
    protected Vector offset() {
        return properties.getOffset();
    }

    protected double speed() {
        return properties.getSpeed();
    }

    @Nullable
    protected Object data() {
        return properties.getData();
    }
}
