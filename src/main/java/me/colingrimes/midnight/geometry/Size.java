package me.colingrimes.midnight.geometry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.misc.Validator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a size in the form of length (X/Z), height (Y), and width (X/Z).
 */
public class Size implements Serializable {

    private final int length;
    private final int height;
    private final int width;

    /**
     * Constructs a new {@link Size} with the same length, height, and width.
     *
     * @param size the size
     * @return the size
     */
    @Nonnull
    public static Size of(int size) {
        return new Size(size, size, size);
    }

    /**
     * Constructs a new {@link Size} with the given length, height, and width.
     *
     * @param length the length
     * @param height the height
     * @param width the width
     * @return the size
     */
    @Nonnull
    public static Size of(int length, int height, int width) {
        return new Size(length, height, width);
    }

    /**
     * Attempts to construct a {@link Size} from a generic object.
     * <p>
     * Acceptable formats:
     * <ul>
     *     <li>A {@link Size} instance - returned directly.</li>
     *     <li>A {@link String} in the form {@code "LxHxW"} or using the {@code ":"} delimiter.</li>
     *     <li>A {@link List} of string elements - must contain exactly 3 integer-parsable values.</li>
     *     <li>A {@code String[]} of length 3 with all integer values.</li>
     * </ul>
     * Returns {@code null} if parsing fails or the format is invalid.
     *
     * @param object the object
     * @return the size
     */
    @Nullable
    public static Size of(@Nullable Object object) {
        if (object == null) return null;
        if (object instanceof Size size) return size;

        List<String> parts = null;
        if (object instanceof String str) {
            String[] split = str.trim().split("[xX:]+");
            if (split.length == 3 && Arrays.stream(split).allMatch(Types::isInteger)) {
                parts = Arrays.asList(split);
            }
        } else if (Types.isStringList(object)) {
            List<String> list = Types.asStringList(object).orElse(null);
            if (list != null && list.size() == 3 && list.stream().allMatch(Types::isInteger)) {
                parts = list;
            }
        } else if (object instanceof String[] array && array.length == 3) {
            if (Arrays.stream(array).allMatch(Types::isInteger)) {
                parts = Arrays.asList(array);
            }
        }

        if (parts != null) {
            int length = Integer.parseInt(parts.get(0).trim());
            int height = Integer.parseInt(parts.get(1).trim());
            int width  = Integer.parseInt(parts.get(2).trim());
            return Size.of(length, height, width);
        }

        return null;
    }

    private Size(int length, int height, int width) {
        this.length = length;
        this.height = height;
        this.width = width;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width.
     *
     * @return the width.
     */
    public int getWidth() {
        return width;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof Size size)) return false;
		return getLength() == size.getLength() && getHeight() == size.getHeight() && getWidth() == size.getWidth();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLength(), getHeight(), getWidth());
    }

    @Nonnull
    @Override
    public String toString() {
        return "Size{" +
                "length=" + length +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    @Nonnull
    @Override
    public JsonElement serialize() {
        return Json.create()
                .add("length", length)
                .add("height", height)
                .add("width", width)
                .build();
    }

    @Nonnull
    public static Size deserialize(@Nonnull JsonElement element) {
        JsonObject object = Validator.checkJson(element, "length", "height", "width");
        return of(object.get("length").getAsInt(), object.get("height").getAsInt(), object.get("width").getAsInt());
    }
}
