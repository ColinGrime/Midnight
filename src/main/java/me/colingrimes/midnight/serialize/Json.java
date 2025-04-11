package me.colingrimes.midnight.serialize;

import com.google.gson.*;

import javax.annotation.Nonnull;

/**
 * Utility class for common Gson operations.
 */
public final class Json {

    private static final Gson gson = new GsonBuilder().create();
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Creates a new {@link Json.Builder} object.
     *
     * @return the json builder object
     */
    @Nonnull
    public static Json.Builder create() {
        return new Json.Builder();
    }

    /**
     * Creates a new {@link Json.Builder} object.
     *
     * @param element the json element
     * @return the json builder object
     */
    @Nonnull
    public static Json.Builder of(@Nonnull JsonElement element) {
        return new Json.Builder(element);
    }

    /**
     * Converts a {@link JsonElement} object to a JSON string.
     *
     * @param element the json element
     * @return the converted json string
     */
    @Nonnull
    public static String toString(@Nonnull JsonElement element) {
        return gson.toJson(element);
    }

    /**
     * Converts a {@link Serializable} object to a JSON string.
     *
     * @param serializable the serializable object
     * @return the converted json string
     */
    @Nonnull
    public static String toString(@Nonnull Serializable serializable) {
        return toString(serializable.serialize());
    }

    /**
     * Converts a {@link JsonElement} object to a pretty formatted JSON string.
     *
     * @param element the json element
     * @return the converted json string
     */
    @Nonnull
    public static String toStringPretty(@Nonnull JsonElement element) {
        return gsonPretty.toJson(element);
    }

    /**
     * Converts a {@link Serializable} object to a pretty formatted JSON string.
     *
     * @param serializable the serializable object
     * @return the converted json string
     */
    @Nonnull
    public static String toStringPretty(@Nonnull Serializable serializable) {
        return toStringPretty(serializable.serialize());
    }

    /**
     * Converts a string into a json element.
     *
     * @param str the string
     * @return the converted json element
     */
    @Nonnull
    public static JsonElement toElement(@Nonnull String str) {
        return JsonParser.parseString(str);
    }

    /**
     * Provides a simple way to build {@link com.google.gson.JsonObject} objects.
     */
    public static final class Builder {

        private final JsonObject object;

        public Builder() {
            this.object = new JsonObject();
        }

        public Builder(@Nonnull JsonElement element) {
            this.object = (JsonObject) element;
        }

        /**
         * Adds a key-value pair with a JsonElement value.
         *
         * @param key the json key
         * @param value the json element
         * @return the json builder object
         */
        @Nonnull
        public Builder add(@Nonnull String key, @Nonnull JsonElement value) {
            object.add(key, value);
            return this;
        }


        /**
         * Adds a key-value pair with a {@link String} value.
         *
         * @param key the json key
         * @param value the string value
         * @return the json builder object
         */
        @Nonnull
        public Builder add(@Nonnull String key, @Nonnull String value) {
            object.addProperty(key, value);
            return this;
        }

        /**
         * Adds a key-value pair with a {@link Number} value.
         *
         * @param key the json key
         * @param value the number value
         * @return the json builder object
         */
        @Nonnull
        public Builder add(@Nonnull String key, @Nonnull Number value) {
            object.addProperty(key, value);
            return this;
        }

        /**
         * Adds a key-value pair with a {@link Boolean} value.
         *
         * @param key the json key
         * @param value the boolean value
         * @return the json builder object
         */
        @Nonnull
        public Builder add(@Nonnull String key, boolean value) {
            object.addProperty(key, value);
            return this;
        }

        /**
         * Builds the {@link JsonObject} item.
         *
         * @return the json object
         */
        @Nonnull
        public JsonObject build() {
            return object;
        }
    }

    private Json() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
