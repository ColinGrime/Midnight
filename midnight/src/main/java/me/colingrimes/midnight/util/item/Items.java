package me.colingrimes.midnight.util.item;

import me.colingrimes.midnight.locale.Placeholders;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides various utilities for {@link ItemStack} objects.
 */
public final class Items {

	/**
	 * Finds the slot number of the first occurrence of the specified item,
	 * @param player the player to check
	 * @param item the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable Player player, @Nullable ItemStack item) {
		return player == null ? Optional.empty() : findSlot(player.getInventory(), item);
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item,
	 * @param inv the inventory to check
	 * @param item the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable Inventory inv, @Nullable ItemStack item) {
		return inv == null ? Optional.empty() : findSlot(inv.getContents(), item);
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item,
	 * @param items the items to check
	 * @param item the item to search for
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
	 * @param item1 any item
	 * @param item2 any item
	 * @return true if the two items are of the same type
	 */
	public static boolean isSameType(@Nullable ItemStack item1, @Nullable ItemStack item2) {
		if (item1 == null || item2 == null) {
			return false;
		}

		return item1.getType().equals(item2.getType());
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
	public static ItemStack config(@Nullable ConfigurationSection sec) {
		return new ItemBuilder().config(sec).build();
	}

	/**
	 * Provides a simple way to build {@link ItemStack} objects.
	 * - Supports parsing of {@link Material} enums from string formats.
	 * - Supports parsing of {@link ConfigurationSection} objects.
	 * - Supports replacing {@link Placeholders} from the name/lore.
	 */
	public static final class ItemBuilder {

		private final Placeholders placeholders = new Placeholders();
		private final Material defMaterial;

		private Material material;
		private String name;
		private List<String> lore;
		private boolean hide = false;
		private boolean glow = false;

		public ItemBuilder() {
			this(Material.STONE);
		}

		public ItemBuilder(@Nonnull Material def) {
			this.defMaterial = Objects.requireNonNull(def, "material");
		}

		/**
		 * Sets the {@link Material} of the item.
		 *
		 * @param material the material you want the item to be
		 * @return the itembuilder object
		 */
		@Nonnull
		public ItemBuilder material(@Nullable Material material) {
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
		public ItemBuilder material(@Nullable String str) {
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
		public ItemBuilder name(@Nullable String name) {
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
		public ItemBuilder lore(@Nullable List<String> lore) {
			this.lore = lore;
			return this;
		}

		/**
		 * Hides the attributes of the item.
		 *
		 * @return the itembuilder object
		 */
		@Nonnull
		public ItemBuilder hide(boolean hide) {
			this.hide = hide;
			return this;
		}

		/**
		 * Makes the item glow.
		 *
		 * @return the itembuilder object
		 */
		@Nonnull
		public ItemBuilder glow(boolean glow) {
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
		public ItemBuilder config(@Nullable ConfigurationSection sec) {
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
		public <T> ItemBuilder placeholder(@Nonnull String placeholder, @Nonnull T replacement) {
			placeholders.add(placeholder, replacement);
			return this;
		}

		/**
		 * Builds the {@link ItemStack} item.
		 * @return the item
		 */
		@Nonnull
		public ItemStack build() {
			ItemStack item = new ItemStack(Objects.requireNonNullElse(this.material, this.defMaterial));
			ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Meta is null.");

			if (name != null) meta.setDisplayName(placeholders.replace(name));
			if (lore != null) meta.setLore(placeholders.replace(lore));
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
		 * @param sec the section of the configuration file
		 * @param key the key of the string
		 * @return the value corresponding to the key
		 */
		@Nonnull
		private <T> Optional<T> parse(@Nonnull ConfigurationSection sec, @Nonnull String key, @Nonnull Class<T> clazz) {
			return Optional.ofNullable(sec.getObject(key, clazz));
		}
	}

	private Items() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
