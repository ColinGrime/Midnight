package me.colingrimes.midnight.particle.implementation;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleProperties;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public abstract class BaseParticleEffect implements ParticleEffect {

    private final ParticleProperties properties;
    private Point<Rotation> point;
    private Entity entity;
    private BukkitTask task;

    public BaseParticleEffect(@Nonnull Point<Rotation> point, @Nonnull ParticleProperties properties) {
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
        if (entity != null && !point.getPosition().toLocation().equals(entity.getLocation())) {
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
        task = Bukkit.getScheduler().runTaskTimer(MidnightPlugin.getInstance(), this::spawn, 0L, 10L);
    }

    @Override
    public void stopSpawning() {
        if (task != null) {
            task.cancel();
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
    public void updateProperty(@Nonnull ParticleProperty property, @Nonnull String value) {
        Object parsedValue = property.parseValue(value);
        if (parsedValue == null) {
            return;
        }

        switch (property) {
            case TYPE -> properties.setParticle((Particle) parsedValue);
            case COUNT -> properties.setCount((int) parsedValue);
            case OFFSET -> properties.setOffset((Vector) parsedValue);
            case SPEED -> properties.setSpeed((double) parsedValue);
            case COLOR -> properties.setColor((Color) parsedValue);
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

    @Nonnull
    protected Object data() {
        return properties.getData();
    }
}
