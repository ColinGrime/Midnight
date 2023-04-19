package me.colingrimes.midnight.util;

import org.bukkit.Color;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class ParsingUtil {

    /**
     * Parses a vector from a string.
     * @param value the string to parse
     * @return the parsed vector
     */
    @Nonnull
    public static Vector parseVector(String value) {
        String[] parts = value.split(",", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid vector format, expected format: x,y,z");
        }

        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        return new Vector(x, y, z);
    }

    /**
     * Parses a color from a string.
     * @param value the string to parse
     * @return the parsed color
     */
    public static Color parseColor(String value) {
        String[] parts = value.split(",", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid color format, expected format: r,g,b");
        }

        int r = Integer.parseInt(parts[0]);
        int g = Integer.parseInt(parts[1]);
        int b = Integer.parseInt(parts[2]);

        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException("Invalid color values, RGB values should be between 0 and 255");
        }

        return Color.fromRGB(r, g, b);
    }
}
