package me.colingrimes.particles.particle.implementation.type;

import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.midnight.util.misc.Validate;
import me.colingrimes.particles.particle.implementation.BaseParticleEffect;
import me.colingrimes.particles.particle.util.ParticleEffectType;
import me.colingrimes.particles.particle.util.ParticleProperties;
import me.colingrimes.particles.particle.util.ParticleProperty;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class SquareParticleEffect extends BaseParticleEffect {

    private double length;
    private int pointsPerSide;

    /**
     * Constructor for creating simple square particle effects.
     *
     * @param point the point to spawn the particle effect at
     */
    public SquareParticleEffect(@Nonnull Point<Rotation> point) {
        this(point, 5, 20);
    }

    /**
     * Constructor for creating more complex square particle effects.
     *
     * @param point  the point to spawn the particle effect at
     * @param length the length of the square
     * @param pointsPerSide the number of points on each side of the square
     */
    public SquareParticleEffect(@Nonnull Point<Rotation> point, double length, int pointsPerSide) {
        super(point, ParticleProperties.create());
        this.length = length;
        this.pointsPerSide = pointsPerSide;
    }

    /**
     * Constructor for deserializing particle effects.
     *
     * @param uuid          the UUID of the particle effect
     * @param name          the name of the particle effect
     * @param point         the point to spawn the particle effect at
     * @param properties    the properties of the particle effect
     * @param length        the length of the square
     * @param pointsPerSide  the number of points on each side of the square
     */
    public SquareParticleEffect(@Nonnull UUID uuid, @Nonnull String name, @Nonnull Point<Rotation> point, @Nonnull ParticleProperties properties, double length, int pointsPerSide) {
        super(uuid, name, point, properties);
        this.length = length;
        this.pointsPerSide = pointsPerSide;
    }

    @Nonnull
    @Override
    public ParticleEffectType getType() {
        return ParticleEffectType.SQUARE;
    }

    @Override
    public void spawnParticle() {
        double increment = length / pointsPerSide;
        
        for (int i=0; i<pointsPerSide; i++) {
            for (int j=0; j<pointsPerSide; j++) {
                double x = i * increment;
                double z = j * increment;

                Position squarePoint = Position.of(world(), x, 0, z);
                Position rotatedPosition = squarePoint.rotate(rotation());
                Position finalPosition = position().add(rotatedPosition.getX(), rotatedPosition.getY(), rotatedPosition.getZ());

                spawnParticle(finalPosition.toLocation());
            }
        }
    }

    @Override
    protected void updatePropertyValue(@Nonnull ParticleProperty property, @Nonnull Object value) {
        switch (property) {
            case RADIUS -> length = (double) value;
            case POINTS -> pointsPerSide = (int) value;
            default -> super.updatePropertyValue(property, value);
        }
    }

    @Nonnull
    @Override
    public Object getProperty(@Nonnull ParticleProperty property) {
        return switch (property) {
            case LENGTH -> length;
            case POINTS -> pointsPerSide;
            default -> super.getProperty(property);
        };
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("length", length);
        map.put("pointsPerSide", pointsPerSide);
        return map;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static SquareParticleEffect deserialize(@Nonnull Map<String, Object> map) {
        Validate.checkMap(map, "uuid", "name", "point", "properties", "length", "pointsPerSide");

        UUID uuid = UUID.fromString((String) map.get("uuid"));
        String name = (String) map.get("name");
        Point<Rotation> point = Point.deserialize((Map<String, Object>) map.get("point"));
        ParticleProperties properties = ParticleProperties.deserialize((Map<String, Object>) map.get("properties"));
        double length = (double) map.getOrDefault("length", 5.0);
        int pointsPerSide = (int) map.getOrDefault("pointsPerSide", 20);

        return new SquareParticleEffect(uuid, name, point, properties, length, pointsPerSide);
    }
}
