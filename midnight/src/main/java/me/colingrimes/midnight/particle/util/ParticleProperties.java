package me.colingrimes.midnight.particle.util;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class ParticleProperties {

    private Particle particle = Particle.REDSTONE;
    private int count = 1;
    private Vector offset = new Vector(0, 0, 0);
    private double speed = 0;
    private Object data = new Particle.DustOptions(Color.RED, 1);

    /**
     * Gets the particle type.
     * @return the particle type
     */
    @Nonnull
    public Particle getParticle() {
        return particle;
    }

    /**
     * Sets the particle type.
     * @param particle the particle type
     */
    public void setParticle(@Nonnull Particle particle) {
        this.particle = particle;
        this.data = null;
    }

    /**
     * Gets the number of particles to spawn.
     * @return the count of particles
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of particles to spawn.
     * @param count the count of particles
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets the offset of the particle.
     * @return the particle offset
     */
    @Nonnull
    public Vector getOffset() {
        return offset;
    }

    /**
     * Sets the offset of the particle.
     * @param offset the particle offset
     */
    public void setOffset(@Nonnull Vector offset) {
        this.offset = offset;
    }

    /**
     * Gets the speed of the particle.
     * @return the particle speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the particle.
     * @param speed the particle speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Gets the data for the particle.
     * @return the particle data
     */
    @Nonnull
    public Object getData() {
        return data;
    }

    /**
     * Sets the data for the particle.
     * @param data the particle data
     */
    public void setData(@Nonnull Object data) {
        this.data = data;
    }

    /**
     * Sets the color of the particle.
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
}
