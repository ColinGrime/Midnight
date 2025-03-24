package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Responsible for NBT integration with {@link ItemStack} objects.
 */
public final class NBT {

	private static final Map<Class<?>, PersistentDataType<?, ?>> TYPE_MAP = new HashMap<>();

	static {
		TYPE_MAP.put(byte.class, PersistentDataType.BYTE);
		TYPE_MAP.put(Byte.class, PersistentDataType.BYTE);
		TYPE_MAP.put(short.class, PersistentDataType.SHORT);
		TYPE_MAP.put(Short.class, PersistentDataType.SHORT);
		TYPE_MAP.put(int.class, PersistentDataType.INTEGER);
		TYPE_MAP.put(Integer.class, PersistentDataType.INTEGER);
		TYPE_MAP.put(long.class, PersistentDataType.LONG);
		TYPE_MAP.put(Long.class, PersistentDataType.LONG);
		TYPE_MAP.put(float.class, PersistentDataType.FLOAT);
		TYPE_MAP.put(Float.class, PersistentDataType.FLOAT);
		TYPE_MAP.put(double.class, PersistentDataType.DOUBLE);
		TYPE_MAP.put(Double.class, PersistentDataType.DOUBLE);
		TYPE_MAP.put(boolean.class, PersistentDataType.BOOLEAN);
		TYPE_MAP.put(Boolean.class, PersistentDataType.BOOLEAN);
		TYPE_MAP.put(String.class, PersistentDataType.STRING);
	}

	/**
	 * Gets an item's NBT tag corresponding to a key.
	 *
	 * @param item the item to get the tag from
	 * @param key  the key to get the value from
	 * @return the item's tag corresponding to the key
	 */
	@Nonnull
	public static Optional<String> getTag(@Nullable ItemStack item, @Nullable String key) {
		return getTag(item, key, String.class);
	}

	/**
	 * Gets an item's NBT tag corresponding to a key.
	 *
	 * @param item  the item to get the tag from
	 * @param key   the key to get the value from
	 * @param clazz the data type class to convert to
	 * @return the item's tag corresponding to the key
	 */
	@Nonnull
	public static <T> Optional<T> getTag(@Nullable ItemStack item, @Nullable String key, @Nonnull Class<T> clazz) {
		if (item == null || item.getItemMeta() == null || key == null || key.isEmpty()) {
			return Optional.empty();
		}

		ItemMeta meta = item.getItemMeta();
		NamespacedKey namespacedKey = new NamespacedKey(MidnightPlugin.get(), key);
		return Optional.ofNullable(meta.getPersistentDataContainer().get(namespacedKey, getDataType(clazz)));
	}

	/**
	 * Sets a NBT tag (key-value pair) on an item.
	 *
	 * @param item the item to set the tag on
	 * @param key  the key of the tag
	 * @param value the value corresponding to the key
	 * @return the item with the tag added
	 */
	@Nonnull
	public static <T> ItemStack setTag(@Nonnull ItemStack item, @Nonnull String key, @Nonnull T value) {
		Objects.requireNonNull(item, "Item is null.");
		Objects.requireNonNull(item.getItemMeta(), "Item meta is null.");
		Objects.requireNonNull(key, "Key is null.");
		Objects.requireNonNull(value, "Value is null.");

		ItemMeta meta = item.getItemMeta();
		NamespacedKey namespacedKey = new NamespacedKey(MidnightPlugin.get(), key);
		meta.getPersistentDataContainer().set(namespacedKey, getDataType(value), value);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Checks whether the item has the given NBT tag.
	 *
	 * @param item  the item to check the tag from
	 * @param key   the key to check the tag from
	 * @param clazz the data type of the key
	 * @return true if the item contains the key of the specified type
	 */
	public static boolean hasTag(@Nullable ItemStack item, @Nullable String key, @Nonnull Class<?> clazz) {
		if (item == null || item.getItemMeta() == null || key == null || key.isEmpty()) {
			return false;
		}

		ItemMeta meta = item.getItemMeta();
		NamespacedKey namespacedKey = new NamespacedKey(MidnightPlugin.get(), key);
		return meta.getPersistentDataContainer().has(namespacedKey, getDataType(clazz));
	}

	/**
	 * Gets the {@link PersistentDataType} from the object.
	 *
	 * @param object the object to get the data type from
	 * @return the persistent data type
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	private static <T> PersistentDataType<?, T> getDataType(@Nonnull T object) {
		return getDataType((Class<T>) object.getClass());
	}

	/**
	 * Gets the {@link PersistentDataType} from the class.
	 *
	 * @param clazz the class to get the data type from
	 * @return the persistent data type
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	private static <T> PersistentDataType<?, T> getDataType(@Nonnull Class<T> clazz) {
		PersistentDataType<?, ?> type = TYPE_MAP.get(clazz);
		if (type == null) {
			throw new IllegalArgumentException("Unsupported data type: " + clazz.getSimpleName());
		} else {
			return (PersistentDataType<?, T>) type;
		}
	}

	private NBT() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
