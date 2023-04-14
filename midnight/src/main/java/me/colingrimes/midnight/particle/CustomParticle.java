package me.colingrimes.midnight.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class CustomParticle {

    protected final Particle particle;
    protected final Location location;
    protected final World world;
    protected final int count;
    protected Vector offset;
    protected double speed;
    protected Object data;

    protected CustomParticle(@Nonnull Builder<?> builder) {
        this.particle = builder.particle;
        this.location = builder.location;
        this.world = builder.location.getWorld();
        this.count = builder.count;
        this.offset = builder.offset;
        this.speed = builder.speed;
        this.data = builder.data;
    }

    /**
     * Spawns the particle at the location specified in the builder.
     */
    public abstract void spawn();

    public static abstract class Builder<T extends Builder<T>> {

        private final Particle particle;
        private final Location location;
        private final int count;
        private Vector offset;
        private double speed;
        private Object data;

        protected Builder(@Nonnull Particle particle, @Nonnull Location location, int count) {
            this.particle = Objects.requireNonNull(particle, "Particle is null.");
            this.location = Objects.requireNonNull(location, "Location is null.");
            this.count = count;
        }

        /**
         * Sets the offset of the particle.
         * @param offset the offset
         * @return the builder
         */
        @Nonnull
        public T offset(@Nonnull Vector offset) {
            this.offset = offset;
            return self();
        }

        /**
         * Sets the speed of the particle.
         * @param speed the speed
         * @return the builder
         */
        @Nonnull
        public T speed(double speed) {
            this.speed = speed;
            return self();
        }

        /**
         * Sets the data of the particle.
         * @param data the data
         * @return the builder
         */
        @Nonnull
        public T data(@Nonnull Object data) {
            this.data = data;
            return self();
        }

        /**
         * Returns the builder.
         * @return the builder
         */
        @Nonnull
        protected abstract T self();

        /**
         * Builds the particle.
         * @return the particle
         */
        @Nonnull
        public abstract CustomParticle build();
    }
}
