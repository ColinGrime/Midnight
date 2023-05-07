package me.colingrimes.midnight.util.bukkit;

import org.bukkit.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of custom chat colors with RGB values.
 */
public enum ChatColor {
    BLACK        ('0', 0 ,  0,   0    ),
    DARK_BLUE    ('1', 0,   0,   128  ),
    GREEN        ('2', 0,   128, 0    ),
    AQUA         ('3', 0,   128, 128  ),
    RED          ('4', 128, 0,   0    ),
    PURPLE       ('5', 128, 0,   128  ),
    GOLD         ('6', 255, 128, 0    ),
    GRAY         ('7', 128, 128, 128  ),
    DARK_GRAY    ('8', 64,  64,  64   ),
    BLUE         ('9', 64,  64,  255  ),
    LIGHT_GREEN  ('a', 64,  255, 64   ),
    LIGHT_BLUE   ('b', 64,  255, 255  ),
    LIGHT_RED    ('c', 255, 64,  64   ),
    PINK         ('d', 255, 64,  255  ),
    YELLOW       ('e', 255, 255, 64   ),
    WHITE        ('f', 255, 255, 255  );

    private static final Map<Character, ChatColor> CODE_MAP = new HashMap<>();

    static {
        for (ChatColor chatColor : values()) {
            CODE_MAP.put(chatColor.code, chatColor);
        }
    }

    private final char code;
    private final Color color;

    /**
     * Constructs a new ChatColor.
     * @param code the color code
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     */
    ChatColor(char code, int r, int g, int b) {
        this.code = code;
        this.color = Color.fromRGB(r, g, b);
    }

    /**
     * Gets the color code.
     * @return the color code
     */
    public char getCode() {
        return code;
    }

    /**
     * Gets the RGB color.
     * @return the RGB color
     */
    @Nonnull
    public Color getColor() {
        return color;
    }

    /**
     * Parses a {@link ChatColor} from a string (name or '&' code).
     * @param value the string to parse
     * @return the parsed custom chat color or null if the input is invalid
     */
    @Nullable
    public static ChatColor parse(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Check for the '&' code.
        if (value.startsWith("&") && value.length() == 2) {
            return CODE_MAP.get(value.charAt(1));
        }

        try {
            return ChatColor.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
