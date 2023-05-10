package me.colingrimes.particles.particle.util;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.serialize.Serializable;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ParticleProperties implements Serializable {

    private Particle particle;
    private int count;
    private Vector offset;
    private double speed;
    private Object data;

    /**
     * Constructs a new ParticleProperties with default values.
     *
     * @return the particle properties
     */
    public static ParticleProperties create() {
        return new ParticleProperties(Particle.REDSTONE, 1, new Vector(0, 0, 0), 0, new Particle.DustOptions(Color.RED, 1));
    }

    /**
     * Constructs a new ParticleProperties with the given particle type.
     *
     * @param particle the particle type
     * @param count    the number of particles to spawn
     * @param offset   the offset
     * @param speed    the speed
     * @param data     the data
     * @return the particle properties
     */
    public static ParticleProperties of(@Nonnull Particle particle, int count, @Nonnull Vector offset, double speed, @Nullable Object data) {
        return new ParticleProperties(particle, count, offset, speed, data);
    }

    private ParticleProperties(@Nonnull Particle particle, int count, @Nonnull Vector offset, double speed, @Nullable Object data) {
        this.particle = particle;
        this.count = count;
        this.offset = offset;
        this.speed = speed;
        this.data = data;
    }

    /**
     * Gets the particle type.
     *
     * @return the particle type
     */
    @Nonnull
    public Particle getParticle() {
        return particle;
    }

    /**
     * Sets the particle type.
     *
     * @param particle the particle type
     */
    public void setParticle(@Nonnull Particle particle) {
        this.particle = particle;
        this.data = null;
    }

    /**
     * Gets the number of particles to spawn.
     *
     * @return the count of particles
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of particles to spawn.
     *
     * @param count the count of particles
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets the offset of the particle.
     *
     * @return the particle offset
     */
    @Nonnull
    public Vector getOffset() {
        return offset;
    }

    /**
     * Sets the offset of the particle.
     *
     * @param offset the particle offset
     */
    public void setOffset(@Nonnull Vector offset) {
        this.offset = offset;
    }

    /**
     * Gets the speed of the particle.
     *
     * @return the particle speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the particle.
     *
     * @param speed the particle speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Gets the data for the particle.
     *
     * @return the particle data
     */
    @Nullable
    public Object getData() {
        return data;
    }

    /**
     * Sets the data for the particle.
     *
     * @param data the particle data
     */
    public void setData(@Nonnull Object data) {
        this.data = data;
    }

    /**
     * Sets the color of the particle.
     *
     * @param color the color
     */
    public void setColor(@Nonnull Color color) {
        if (particle == Particle.REDSTONE) {
            data = new Particle.DustOptions(color, 1.0f);
        } else if (particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT) {
            data = color.asRGB(); // Set the color as an integer value.
        } else if (particle == Particle.NOTE) {
            data = color.getRed(); // Set the color as the red component value (0-255).
        } else {
            throw new UnsupportedOperationException("This particle type does not support color change.");
        }
    }

    @Nonnull
    @Override
    public String toString() {
        return "ParticleProperties{" +
                "particle=" + particle +
                ", count=" + count +
                ", offset=" + offset +
                ", speed=" + speed +
                ", data=" + data +
                '}';
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("particle", particle.name());
        map.put("count", count);
        map.put("offset", offset.serialize());
        map.put("speed", speed);

        // The annoying particles that require more data.
        if (data instanceof Particle.DustOptions dust) {
            map.put("data", Map.of("type", "dust_options", "color", dust.getColor().asRGB(), "size", dust.getSize()));
        } else if (particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT) {
            map.put("data", Map.of("type", "color_as_rgb", "value", data));
        } else if (particle == Particle.NOTE) {
            map.put("data", Map.of("type", "color_red", "value", data));
        }

        return map;
    }

    /**
     * Deserializes a map into a ParticleProperties object.
     *
     * @param map the serialized map
     * @return the particle properties
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static ParticleProperties deserialize(@Nonnull Map<String, Object> map) {
        Preconditions.checkArgument(map.containsKey("particle"));
        Preconditions.checkArgument(map.containsKey("count"));
        Preconditions.checkArgument(map.containsKey("offset"));
        Preconditions.checkArgument(map.containsKey("speed"));
        Preconditions.checkArgument(map.get("offset") instanceof Map);

        ParticleProperties properties = ParticleProperties.of(
                Particle.valueOf((String) map.get("particle")),
                (int) map.get("count"),
                Vector.deserialize((Map<String, Object>) map.get("offset")),
                (double) map.get("speed"),
                null
        );

        // The annoying particles that require more data.
        if (map.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String type = (String) data.get("type");

            if (type.equals("dust_options") && properties.getParticle() == Particle.REDSTONE) {
                properties.setData(new Particle.DustOptions(Color.fromRGB((int) data.get("color")), ((Number) data.get("size")).floatValue()));
            } else if (type.equals("color_as_rgb") && (properties.getParticle() == Particle.SPELL_MOB || properties.getParticle() == Particle.SPELL_MOB_AMBIENT)) {
                properties.setData(data.get("value"));
            } else if (type.equals("color_red") && properties.getParticle() == Particle.NOTE) {
                properties.setData(data.get("value"));
            }
        }

        return properties;
    }
}
