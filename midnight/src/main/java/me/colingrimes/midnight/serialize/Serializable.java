package me.colingrimes.midnight.serialize;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;

/**
 * An interface for custom serialization and deserialization of objects.
 * Classes implementing this interface should provide methods to convert
 * the object's state to a Map and reconstruct the object from a Map.
 */
public interface Serializable {

	/**
	 * Serializes the object's state to a Map.
	 * @return a map containing the serialized state of the object
	 */
	@Nonnull
	Map<String, Object> serialize();

	/**
	 * Deserializes the object's state from a Map and creates a new instance of the object.
	 * @param serialized a Map containing the serialized state of the object
	 * @param deserializationFunction a function that takes a Map and returns a new instance of the object
	 * @param <T> the type of the object to be deserialized
	 * @return a new instance of the object with its state set from the Map
	 */
	@Nonnull
	static <T> T deserialize(@Nonnull Map<String, Object> serialized, @Nonnull Function<Map<String, Object>, T> deserializationFunction) {
		return deserializationFunction.apply(serialized);
	}
}