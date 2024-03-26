package me.colingrimes.midnight.util.text;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parser {

    private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d+)\\s*([a-zA-Z]+)$");
    private static final Map<Pattern, ChronoUnit> UNIT_PATTERNS = new HashMap<>();

    static {
        // s sec secs second seconds
        UNIT_PATTERNS.put(Pattern.compile("s(?:ec(?:ond)?s?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.SECONDS);
        // m min mins minute minutes
        UNIT_PATTERNS.put(Pattern.compile("m(?:in(?:ute)?s?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.MINUTES);
        // h hr hrs hour hours
        UNIT_PATTERNS.put(Pattern.compile("h(?:rs?|ours?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.HOURS);
        // d day days
        UNIT_PATTERNS.put(Pattern.compile("d(?:ays?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.DAYS);
        // w wk wks week weeks
        UNIT_PATTERNS.put(Pattern.compile("w(?:ks?|eeks?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.WEEKS);
        // mo mos month months
        UNIT_PATTERNS.put(Pattern.compile("mo(?:s?|nths?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.MONTHS);
        // y yr yrs year years
        UNIT_PATTERNS.put(Pattern.compile("y(?:rs?|ears?)?", Pattern.CASE_INSENSITIVE), ChronoUnit.YEARS);
    }

    /**
     * Parses the provided string to the corresponding Enum value.
     *
     * @param enumType the Class of the Enum to parse into
     * @param value    the string value to parse
     * @param <E>      the type of the Enum
     * @return an Optional containing the Enum value matching the provided string
     */
    @Nonnull
    public static <E extends Enum<E>> Optional<E> parse(@Nonnull Class<E> enumType, @Nullable String value) {
        return Arrays.stream(enumType.getEnumConstants()).filter(e -> e.name().equalsIgnoreCase(value)).findFirst();
    }

    /**
     * Parses the provided string to the corresponding Enum value.
     *
     * @param enumType the Class of the Enum to parse into
     * @param value    the string value to parse
     * @param <E>      the type of the Enum
     * @return the Enum value matching the provided string
     */
    @Nullable
    public static <E extends Enum<E>> E parseNullable(@Nonnull Class<E> enumType, @Nullable String value) {
        return Parser.parse(enumType, value).orElse(null);
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

        return switch(unit) {
            case WEEKS -> Duration.ofDays(amount * 7);
            case MONTHS -> Duration.ofDays(amount * 30);
            case YEARS -> Duration.ofDays(amount * 365);
            default -> Duration.of(amount, unit);
        };
    }

    /**
     * Parses a particle type from a string.
     *
     * @param value the string to parse
     * @return the parsed particle
     */
    @Nullable
    public static Particle parseParticle(@Nullable String value) {
        return Parser.parseNullable(Particle.class, value);
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
