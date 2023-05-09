package me.colingrimes.midnight.util.text;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parser {

    private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)\\s*([a-zA-Z]+)$");
    private static final Map<Pattern, ChronoUnit> UNIT_PATTERNS = new HashMap<>();

    static {
        UNIT_PATTERNS.put(Pattern.compile("s(?:ec(?:ond)?s?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.SECONDS);
        UNIT_PATTERNS.put(Pattern.compile("m(?:in(?:ute)?s?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.MINUTES);
        UNIT_PATTERNS.put(Pattern.compile("h(?:ou?rs?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.HOURS);
        UNIT_PATTERNS.put(Pattern.compile("d(?:ays?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.DAYS);
        UNIT_PATTERNS.put(Pattern.compile("w(?:ee)?ks?", Pattern.CASE_INSENSITIVE), ChronoUnit.WEEKS);
        UNIT_PATTERNS.put(Pattern.compile("mo(?:nth)?s?", Pattern.CASE_INSENSITIVE), ChronoUnit.MONTHS);
        UNIT_PATTERNS.put(Pattern.compile("y(?:ea)?rs?", Pattern.CASE_INSENSITIVE), ChronoUnit.YEARS);
    }

    /**
     * Parses the given text into a {@link Duration}.
     *
     * @param value the text to parse
     * @return the duration represented by the text
     */
    @Nullable
    public static Duration parseDuration(@Nonnull String value) {
        Matcher matcher = DURATION_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return null;
        }

        long amount = Long.parseLong(matcher.group(1));
        String unitString = matcher.group(2);

        ChronoUnit unit = matchUnit(unitString);
        if (unit == null) {
            return null;
        }

        return Duration.of(amount, unit);
    }

    /**
     * Parses a vector from a string.
     *
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
     *
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
     *
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

    /**
     * Matches the given unit string to a {@link ChronoUnit}.
     *
     * @param unit the unit string
     * @return the matching unit, or null if none was found
     */
    private static ChronoUnit matchUnit(@Nonnull String unit) {
        for (Map.Entry<Pattern, ChronoUnit> entry : UNIT_PATTERNS.entrySet()) {
            if (entry.getKey().matcher(unit).matches()) {
                return entry.getValue();
            }
        }
        return null;
    }

    private Parser() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
