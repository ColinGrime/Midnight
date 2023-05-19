package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides various utilities for {@link ItemStack} objects.</p>
 */
public final class Items {

	/**
	 * Creates a new {@link Builder} object.
	 * <p>
	 * Defaults to {@link Material#STONE}.
	 *
	 * @return the item builder object
	 */
	@Nonnull
	public static Builder create() {
		return new Builder(Material.STONE);
	}

	/**
	 * Creates a new {@link Builder} object.
	 *
	 * @param material the default material of the item
	 * @return the item builder object
	 */
	@Nonnull
	public static Builder of(@Nonnull Material material) {
		return new Builder(material);
	}

	/**
	 * Renames an item.
	 *
	 * @param item the item to rename
	 * @param name the new name of the item
	 * @return the renamed item
	 */
	@Nonnull
	public static ItemStack rename(@Nonnull ItemStack item, @Nonnull String name) {
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return item;
		}

		meta.setDisplayName(Text.color(name));
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Sets the lore of an item.
	 *
	 * @param item the item to set the lore of
	 * @param lore the new lore of the item
	 * @return the item with the new lore
	 */
	@Nonnull
	public static ItemStack lore(@Nonnull ItemStack item, @Nonnull List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return item;
		}

		meta.setLore(Text.color(lore));
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Sets the lore of an item.
	 *
	 * @param item the item to set the lore of
	 * @param lore the new lore of the item
	 * @return the item with the new lore
	 */
	@Nonnull
	public static ItemStack lore(@Nonnull ItemStack item, @Nonnull String... lore) {
		return lore(item, List.of(lore));
	}

	/**
	 * Parses a {@link ConfigurationSection} and checks for the following:
	 * - A "type" or "material" key for materials.
	 * - A "name" key for the name of the item.
	 * - A "lore" key for the lore of the item.
	 * - A "glowing" key for whether the item should be glowing or not.
	 *
	 * @param sec the section of the configuration file
	 * @return the item builder object
	 */
	@Nonnull
	public static ItemStack config(@Nullable ConfigurationSection sec) {
		return create().config(sec).build();
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item.
	 *
	 * @param player the player to check
	 * @param item   the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable Player player, @Nullable ItemStack item) {
		return player == null ? Optional.empty() : findSlot(player.getInventory(), item);
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item.
	 *
	 * @param inv  the inventory to check
	 * @param item the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable Inventory inv, @Nullable ItemStack item) {
		return inv == null ? Optional.empty() : findSlot(inv.getContents(), item);
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item.
	 *
	 * @param items the items to check
	 * @param item  the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable ItemStack[] items, @Nullable ItemStack item) {
		if (items == null || item == null) {
			return Optional.empty();
		}

		for (int i=0; i<items.length; i++) {
			if (item.equals(items[i])) {
				return Optional.of(i);
			}
		}

		return Optional.empty();
	}

	/**
	 * Checks if two items are of the same {@link org.bukkit.Material}.
	 *
	 * @param item1 any item
	 * @param item2 any item
	 * @return true if the two items are of the same type
	 */
	public static boolean isSameType(@Nullable ItemStack item1, @Nullable ItemStack item2) {
		return item1 != null && item2 != null && item1.getType().equals(item2.getType());
	}

	/**
	 * Provides a simple way to build {@link ItemStack} objects:
	 * <ul>
	 *   <li>Supports parsing of {@link Material} enums from string formats.</li>
	 *   <li>Supports parsing of {@link ConfigurationSection} objects.</li>
	 *   <li>Supports replacing {@link Placeholders} from the name/lore.</li>
	 * </ul>
	 */
	public static class Builder {

		private final Placeholders placeholders = Placeholders.create();
		private final Material defMaterial;

		private Material material;
		private String name;
		private List<String> lore;
		private boolean hide = false;
		private boolean glow = false;

		public Builder(@Nonnull Material def) {
			this.defMaterial = Objects.requireNonNull(def, "material");
		}

		/**
		 * Sets the {@link Material} of the item.
		 *
		 * @param material the material you want the item to be
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder material(@Nullable Material material) {
			this.material = material;
			return this;
		}

		/**
		 * Sets the {@link Material} of the item.
		 *
		 * @param str the string containing the material name
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder material(@Nullable String str) {
			if (str == null) {
				return this;
			}

			this.material = Material.matchMaterial(str);
			return this;
		}

		/**
		 * Sets the name of the item.
		 *
		 * @param name the name you want the item to be
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder name(@Nullable String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the lore of the item.
		 *
		 * @param lore the lore you want the item to have
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder lore(@Nullable List<String> lore) {
			this.lore = lore;
			return this;
		}

		/**
		 * Hides the attributes of the item.
		 *
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder hide(boolean hide) {
			this.hide = hide;
			return this;
		}

		/**
		 * Makes the item glow.
		 *
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder glow(boolean glow) {
			this.glow = glow;
			return this;
		}

		/**
		 * Parses a {@link ConfigurationSection} and checks for the following:
		 * - A "type" or "material" key for materials.
		 * - A "name" key for the name of the item.
		 * - A "lore" key for the lore of the item.
		 * - A "glowing" key for whether the item should be glowing or not.
		 *
		 * @param sec the section of the configuration file
		 * @return the itembuilder object
		 */
		@Nonnull
		public Builder config(@Nullable ConfigurationSection sec) {
			if (sec == null) {
				return this;
			}

			parse(sec, "type").ifPresent(this::material);
			parse(sec, "material").ifPresent(this::material);
			parse(sec, "name").ifPresent(this::name);
			parse(sec, "lore", List.class).ifPresent(this::lore);
			parse(sec, "glow", Boolean.class).ifPresent(this::glow);
			return this;
		}

		/**
		 * Adds a placeholder to the list of placeholders.
		 *
		 * @param placeholder the placeholder you want to add
		 * @param replacement the value you want to replace the placeholder with
		 * @param <T>         any type
		 * @return the placeholders object
		 */
		@Nonnull
		public <T> Builder placeholder(@Nonnull String placeholder, @Nonnull T replacement) {
			placeholders.add(placeholder, replacement);
			return this;
		}

		/**
		 * Builds the {@link ItemStack} item.
		 *
		 * @return the item
		 */
		@Nonnull
		public ItemStack build() {
			ItemStack item = new ItemStack(Objects.requireNonNullElse(this.material, this.defMaterial));
			ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Meta is null.");

			if (name != null) meta.setDisplayName(placeholders.apply(name).toText());
			if (lore != null) meta.setLore(Arrays.asList(placeholders.apply(lore).toText().split("\n")));
			if (hide) meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			if (glow) {
				item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}

			item.setItemMeta(meta);
			return item;
		}

		/**
		 * Gets the string that corresponds to the inputted key.
		 *
		 * @param sec the section of the configuration file
		 * @param key the key of the string
		 * @return the value corresponding to the key
		 */
		@Nonnull
		private Optional<String> parse(@Nonnull ConfigurationSection sec, @Nonnull String key) {
			return Optional.ofNullable(sec.getString(key));
		}

		/**
		 * Gets the object that corresponds to the inputted key.
		 *
		 * @param sec the section of the configuration file
		 * @param key the key of the string
		 * @return the value corresponding to the key
		 */
		@Nonnull
		private <T> Optional<T> parse(@Nonnull ConfigurationSection sec, @Nonnull String key, @Nonnull Class<T> clazz) {
			return Optional.ofNullable(sec.getObject(key, clazz));
		}
	}
}
