package me.colingrimes.midnight.util.misc;

import com.google.common.base.Preconditions;

import java.util.Map;
import javax.annotation.Nonnull;

public final class Validator {

    /**
     * Checks if the provided map contains the specified keys.
     * Throws an IllegalArgumentException if any of the keys are not present.
     *
     * @param map  the map to check
     * @param keys the keys that should be present in the map
     * @throws IllegalArgumentException if the map does not contain any of the specified keys
     */
    public static void checkMap(@Nonnull Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Preconditions.checkArgument(map.containsKey(key), "Map must contain key: " + key);
        }
    }

    private Validator() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
