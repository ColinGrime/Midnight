package me.colingrimes.midnight.serialize;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An interface for custom serialization and deserialization of objects.
 * <p>
 * Classes implementing this interface should provide methods to convert the
 * object's state to a json element and reconstruct the object from a json element.
 */
public interface Serializable {

	/**
	 * Serializes the object's state to a json element.
	 *
	 * @return the json element containing the serialized state of the object
	 */
	@Nonnull
	JsonElement serialize();

	/**
	 * Deserializes the object's state from a json element and creates a new instance of the object.
	 *
	 * @param clazz the class of the object to deserialize
	 * @param element the json element containing the serialized state of the object
	 * @param <T> the type of the object to be deserialized
	 * @return the deserialized object
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	static <T extends Serializable> T deserialize(@Nonnull Class<T> clazz, @Nonnull JsonElement element) {
		try {
			Method deserialize = clazz.getDeclaredMethod("deserialize", JsonElement.class);
			if (!clazz.isAssignableFrom(deserialize.getReturnType())) {
				throw new IllegalStateException("Deserialize method return type mismatch.");
			}
			if (!Modifier.isStatic(deserialize.getModifiers())) {
				throw new IllegalStateException("Deserialize method is not static.");
			}
			return (T) deserialize.invoke(null, element);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new IllegalStateException("Failed to deserialize '" + clazz.getName() + "' class:", e);
		}
	}
}