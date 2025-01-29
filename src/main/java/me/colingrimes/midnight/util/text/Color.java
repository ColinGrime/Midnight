package me.colingrimes.midnight.util.text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of custom colors with RGB values.
 */
public enum Color {
    BLACK        ('0', "000000"),
    DARK_BLUE    ('1', "000080"),
    GREEN        ('2', "008000"),
    AQUA         ('3', "008080"),
    RED          ('4', "800000"),
    PURPLE       ('5', "800080"),
    GOLD         ('6', "FFA500"),
    GRAY         ('7', "808080"),
    DARK_GRAY    ('8', "404040"),
    BLUE         ('9', "4040FF"),
    LIGHT_GREEN  ('a', "40FF40"),
    LIGHT_BLUE   ('b', "40FFFF"),
    LIGHT_RED    ('c', "FF4040"),
    PINK         ('d', "FF40FF"),
    YELLOW       ('e', "FFFF40"),
    WHITE        ('f', "FFFFFF");

    private static final Map<Character, Color> CODE_MAP = new HashMap<>();

    static {
        for (Color chatColor : values()) {
            CODE_MAP.put(chatColor.code, chatColor);
        }
    }

    private final char code;
    private final String hexColor;

    /**
     * Constructs a new Color.
     *
     * @param code     the color code
     * @param hexColor the hex color
     */
    Color(char code, @Nonnull String hexColor) {
        this.code = code;
        this.hexColor = hexColor;
    }

    /**
     * Gets the color code.
     *
     * @return the color code
     */
    public char getCode() {
        return code;
    }

    /**
     * Gets the hex color.
     *
     * @return the hex color
     */
    @Nonnull
    public String getHexColor() {
        return hexColor;
    }

    /**
     * Parses a {@link Color} from a string (name or '&' code).
     *
     * @param value the string to parse
     * @return the parsed custom color or null if the input is invalid
     */
    @Nullable
    public static Color fromString(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Check for the '&' code.
        if (value.startsWith("&") && value.length() == 2) {
            return CODE_MAP.get(value.charAt(1));
        }

        try {
            return Color.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /**
     * Applies the color to the input string.
     *
     * @param input the string to apply the color to
     * @return the input string with the color applied
     */
    public String apply(@Nonnull String input) {
        return "§x" + hexColor.substring(0, 2) + "§x" + hexColor.substring(2, 4) + "§x" + hexColor.substring(4, 6) + input;
    }
}
