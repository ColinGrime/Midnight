package me.colingrimes.midnight.util.misc;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import javax.annotation.Nonnull;

public final class Validator {

    /**
     * Checks if the provided map contains the specified keys.
     * Throws an {@link IllegalArgumentException} if any of the keys are not present.
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

    /**
     * Checks if the provided json element contains the specified keys.
     * Throws an {@link IllegalArgumentException} if any of the keys are not present.
     *
     * @param element the json element to check
     * @param keys the keys that should be present in the json element
     * @return the json object
     * @throws IllegalArgumentException if the json element does not contain any of the specified keys
     */
    @Nonnull
    public static JsonObject checkJson(@Nonnull JsonElement element, String... keys) {
        Preconditions.checkArgument(element.isJsonObject(), "Json element must be an object.");
        JsonObject object = element.getAsJsonObject();
        for (String key : keys) {
            Preconditions.checkArgument(object.has(key), "Json object must contain key: " + key);
        }
        return object;
    }

    private Validator() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
