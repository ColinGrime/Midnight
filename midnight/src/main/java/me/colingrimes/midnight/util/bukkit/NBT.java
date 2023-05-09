package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Responsible for NBT integration with {@link ItemStack} objects.
 */
public final class NBT {

	/**
	 * Gets an item's NBT tag corresponding to a key.
	 *
	 * @param item the item to get the tag from
	 * @param key  the key to get the value from
	 * @return the item's tag corresponding to the key
	 */
	@Nonnull
	public static Optional<String> getTag(@Nullable ItemStack item, @Nullable String key) {
		if (item == null || item.getItemMeta() == null || key == null || key.isEmpty()) {
			return Optional.empty();
		}

		ItemMeta meta = item.getItemMeta();
		NamespacedKey namespacedKey = new NamespacedKey(MidnightPlugin.get(), key);
		return Optional.ofNullable(meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING));
	}

	/**
	 * Sets a NBT tag (key-value pair) on an item.
	 *
	 * @param item the item to set the tag on
	 * @param key  the key of the tag
	 * @param value the value corresponding to the key
	 */
	public static void setTag(@Nonnull ItemStack item, @Nonnull String key, @Nonnull String value) {
		Objects.requireNonNull(item, "Item is null.");
		Objects.requireNonNull(item.getItemMeta(), "Meta is null.");
		Objects.requireNonNull(key, "Key is null.");
		Objects.requireNonNull(value, "Value is null.");

		ItemMeta meta = item.getItemMeta();
		NamespacedKey namespacedKey = new NamespacedKey(MidnightPlugin.get(), key);
		meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
		item.setItemMeta(meta);
	}

	private NBT() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
