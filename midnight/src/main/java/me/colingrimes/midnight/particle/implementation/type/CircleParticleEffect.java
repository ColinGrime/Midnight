package me.colingrimes.midnight.particle.implementation.type;

import me.colingrimes.midnight.model.Point;
import me.colingrimes.midnight.model.Position;
import me.colingrimes.midnight.model.Rotation;
import me.colingrimes.midnight.particle.implementation.BaseParticleEffect;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class CircleParticleEffect extends BaseParticleEffect {

    private double radius;
    private int points;

    public CircleParticleEffect(@Nonnull Point<Rotation> point) {
        this(point, 5, 100);
    }

    public CircleParticleEffect(@Nonnull Point<Rotation> point, double radius, int points) {
        super(point);
        this.radius = radius;
        this.points = points;
    }

    @Override
    public void spawn() {
        double increment = 2 * Math.PI / points;

        for (int i = 0; i < points; i++) {
            double angle = i * increment;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);

            // Create a Position object (x, y, z) representing the point on the circle.
            Position circlePoint = Position.of(world(), x, 0, z);

            // Apply the full rotation (pitch, yaw, and roll) to the point on the circle.
            Position rotatedPosition = circlePoint.rotate(rotation());

            // Translate the rotated position to the final position by adding the effect's position
            Position finalPosition = position().add(rotatedPosition.getX(), rotatedPosition.getY(), rotatedPosition.getZ());

            // Spawn each particle.
            Location location = finalPosition.toLocation();
            spawnParticle(location);
        }
    }


    @Override
    public void updateProperty(@Nonnull ParticleProperty property, @Nonnull String value) {
        Object parsedValue = property.parseValue(value);

        switch (property) {
            case RADIUS -> radius = (double) parsedValue;
            case POINTS -> points = (int) parsedValue;
            default -> super.updateProperty(property, value);
        }
    }

    @Nonnull
    @Override
    public Object getProperty(@Nonnull ParticleProperty property) {
        return switch (property) {
            case RADIUS -> radius;
            case POINTS -> points;
            default -> super.getProperty(property);
        };
    }
}
