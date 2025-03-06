package me.colingrimes.midnight.particle.implementation.type;

import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.particle.util.ParticleEffectType;
import me.colingrimes.midnight.particle.util.ParticleProperties;
import me.colingrimes.midnight.particle.util.ParticleProperty;
import me.colingrimes.midnight.util.misc.Validator;
import me.colingrimes.midnight.particle.implementation.BaseParticleEffect;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class CircleParticleEffect extends BaseParticleEffect {

    private double radius;
    private int points;

    /**
     * Constructor for creating simple circle particle effects.
     *
     * @param point the point to spawn the particle effect at
     */
    public CircleParticleEffect(@Nonnull Point<Rotation> point) {
        this(point, 5, 100);
    }

    /**
     * Constructor for creating more complex circle particle effects.
     *
     * @param point  the point to spawn the particle effect at
     * @param radius the radius of the circle
     * @param points the number of points on the circle
     */
    public CircleParticleEffect(@Nonnull Point<Rotation> point, double radius, int points) {
        super(point, ParticleProperties.create());
        this.radius = radius;
        this.points = points;
    }

    /**
     * Constructor for deserializing particle effects.
     *
     * @param uuid       the UUID of the particle effect
     * @param name       the name of the particle effect
     * @param point      the point to spawn the particle effect at
     * @param properties the properties of the particle effect
     * @param radius     the radius of the circle
     * @param points     the number of points on the circle
     */
    public CircleParticleEffect(@Nonnull UUID uuid, @Nonnull String name, @Nonnull Point<Rotation> point, @Nonnull ParticleProperties properties, double radius, int points) {
        super(uuid, name, point, properties);
        this.radius = radius;
        this.points = points;
    }

    @Nonnull
    @Override
    public ParticleEffectType getType() {
        return ParticleEffectType.CIRCLE;
    }

    @Override
    public void spawnParticle() {
        double increment = 2 * Math.PI / points;

        for (int i=0; i<points; i++) {
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
            spawnParticle(finalPosition.toLocation());
        }
    }

    @Override
    protected void updatePropertyValue(@Nonnull ParticleProperty property, @Nonnull Object value) {
        switch (property) {
            case RADIUS -> radius = (double) value;
            case POINTS -> points = (int) value;
            default -> super.updatePropertyValue(property, value);
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

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("radius", radius);
        map.put("points", points);
        return map;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static CircleParticleEffect deserialize(@Nonnull Map<String, Object> map) {
        Validator.checkMap(map, "uuid", "name", "point", "properties", "radius", "points");

        UUID uuid = UUID.fromString((String) map.get("uuid"));
        String name = (String) map.get("name");
        Point<Rotation> point = Point.deserialize((Map<String, Object>) map.get("point"));
        ParticleProperties properties = ParticleProperties.deserialize((Map<String, Object>) map.get("properties"));
        double radius = (double) map.getOrDefault("radius", 5.0);
        int points = (int) map.getOrDefault("points", 100);

        return new CircleParticleEffect(uuid, name, point, properties, radius, points);
    }
}
