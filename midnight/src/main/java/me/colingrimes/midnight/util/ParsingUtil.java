package me.colingrimes.midnight.util;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ParsingUtil {

    /**
     * Parses a vector from a string.
     * @param value the string to parse
     * @return the parsed vector
     */
    @Nullable
    public static Particle parseParticle(@Nonnull String value) {
        for (Particle particle : Particle.values()) {
            if (particle.name().equalsIgnoreCase(value)) {
                return particle;
            }
        }
        return null;
    }

    /**
     * Parses a vector from a string.
     * @param value the string to parse
     * @return the parsed vector
     */
    @Nullable
    public static Vector parseVector(String value) {
        String[] parts = value.split(",", 3);
        if (parts.length != 3) {
            return null;
        }

        try {
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);
            return new Vector(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses a color from a string.
     * @param value the string to parse
     * @return the parsed color
     */
    @Nullable
    public static Color parseColor(String value) {
        String[] parts = value.split(",", 3);
        if (parts.length != 3) {
            return null;
        }

        try {
            int r = Integer.parseInt(parts[0]);
            int g = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);

            if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
                return Color.fromRGB(r, g, b);
            }
        } catch (NumberFormatException ignored) {}

        return null;
    }
}
