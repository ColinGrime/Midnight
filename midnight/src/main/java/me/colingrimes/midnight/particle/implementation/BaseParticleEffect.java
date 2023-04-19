package me.colingrimes.midnight.particle.implementation;

import me.colingrimes.midnight.model.Point;
import me.colingrimes.midnight.model.Position;
import me.colingrimes.midnight.model.Rotation;
import me.colingrimes.midnight.particle.ParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleProperties;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public abstract class BaseParticleEffect implements ParticleEffect {

    private final ParticleProperties properties = new ParticleProperties();
    private Point<Rotation> point;
    private BukkitTask task;

    public BaseParticleEffect(@Nonnull Point<Rotation> point) {
        this.point = point;
    }

    @Override
    public abstract void spawn();

    @Override
    public void startSpawning() {
        // Don't start another task if one is already running.
        if (task != null && !task.isCancelled()) {
            return;
        }

        // Start the particle spawning task.
        task = Bukkit.getScheduler().runTaskTimer(MidnightPlugin.getInstance(), this::spawn, 0L, 5L);
    }

    @Override
    public void stopSpawning() {
        if (task != null) {
            task.cancel();
            task = null;
        }
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

        switch (property) {
            case PARTICLE_TYPE -> properties.setParticle((Particle) parsedValue);
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
            case PARTICLE_TYPE -> properties.getParticle();
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