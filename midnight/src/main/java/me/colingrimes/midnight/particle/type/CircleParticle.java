package me.colingrimes.midnight.particle.type;

import me.colingrimes.midnight.particle.CustomParticle;
import org.bukkit.Location;
import org.bukkit.Particle;

import javax.annotation.Nonnull;

public class CircleParticle extends CustomParticle {

    private final double radius;
    private final int points;

    private CircleParticle(@Nonnull Builder builder) {
        super(builder);
        this.radius = builder.radius;
        this.points = builder.points;
    }

    @Override
    public void spawn() {
        double increment = 2 * Math.PI / points;
        double pitchRadians = Math.toRadians(location.getPitch());
        double yawRadians = Math.toRadians(location.getYaw());

        for (int i=0; i<points; i++) {
            double angle = i * increment;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);

            double rotatedX = x * Math.cos(yawRadians) - z * Math.sin(yawRadians);
            double rotatedY = x * Math.sin(pitchRadians) * Math.sin(yawRadians) + z * Math.sin(pitchRadians) * Math.cos(yawRadians);
            double rotatedZ = x * Math.cos(pitchRadians) - z * Math.sin(pitchRadians);

            Location particleLocation = location.clone().add(rotatedX, rotatedY, rotatedZ);
            world.spawnParticle(particle, particleLocation, count, offset.getX(), offset.getY(), offset.getZ(), speed, data);
        }
    }


    public static class Builder extends CustomParticle.Builder<Builder> {

        private double radius;
        private int points;

        public Builder(@Nonnull Particle particle, @Nonnull Location location, int count) {
            super(particle, location, count);
        }

        /**
         * Sets the radius of the circle.
         * @param radius the radius
         * @return the builder
         */
        @Nonnull
        public Builder radius(double radius) {
            this.radius = radius;
            return self();
        }

        /**
         * Sets the number of points in the circle.
         * @param points the number of points
         * @return the builder
         */
        @Nonnull
        public Builder points(int points) {
            this.points = points;
            return self();
        }

        @Nonnull
        @Override
        protected Builder self() {
            return this;
        }

        @Nonnull
        @Override
        public CircleParticle build() {
            return new CircleParticle(this);
        }
    }
}