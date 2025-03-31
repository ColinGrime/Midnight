package me.colingrimes.midnight.util.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import me.colingrimes.midnight.message.Placeholders;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

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
	 * Creates a new {@link Builder} object.
	 * <p>
	 * This will use the provided item as a base for the item builder.
	 *
	 * @param item the item stack
	 * @return the item builder object
	 */
	@Nonnull
	public static Builder of(@Nonnull ItemStack item) {
		return new Builder(item);
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
	 * Damages the specified item by 1 durability.
	 *
	 * @param item the item to damage
	 * @return true if the item was broken
	 */
	public static boolean damage(@Nullable ItemStack item) {
		return damage(item, 1);
	}

	/**
	 * Damages the specified item.
	 *
	 * @param item the item to damage
	 * @param amount the amount to damage the item
	 * @return true if the item was broken
	 */
	public static boolean damage(@Nullable ItemStack item, int amount) {
		if (item == null || !(item.getItemMeta() instanceof Damageable damageable)) {
			return false;
		}

		int damage = damageable.getDamage() + amount;
		if (damage >= item.getType().getMaxDurability()) {
			item.setAmount(item.getAmount() - 1);
			return true;
		} else {
			damageable.setDamage(damage);
			item.setItemMeta(damageable);
			return false;
		}
	}

	/**
	 * Provides a simple way to build {@link ItemStack} objects:
	 * <ul>
	 *   <li>Supports parsing of {@link Material} enums from string formats.</li>
	 *   <li>Supports setting the colored name and lore of the item.</li>
	 *   <li>Supports hiding item attributes, glowing the item, and making it unbreakable. </li>
	 *   <li>Supports parsing of {@link ConfigurationSection} objects.</li>
	 *   <li>Supports replacing {@link Placeholders} from the name/lore.</li>
	 *   <li>Supports setting {@link NBT} tags on the built item.</li>
	 * </ul>
	 */
	public static class Builder {

		private final Placeholders placeholders = Placeholders.create();
		private final Map<String, Object> nbt = new HashMap<>();
		private final Material defMaterial;
		private final ItemStack baseItem;

		private Material material;
		private String name;
		private List<String> lore;
		private boolean hide = false;
		private boolean glow = false;
		private boolean unbreakable = false;

		public Builder(@Nonnull Material def) {
			this.defMaterial = Objects.requireNonNull(def, "material");
			this.baseItem = null;
		}

		public Builder(@Nonnull ItemStack base) {
			Preconditions.checkNotNull(base.getItemMeta(), "Item meta is null.");
			this.defMaterial = null;
			this.baseItem = base;
			this.name = base.getItemMeta().hasDisplayName() ? base.getItemMeta().getDisplayName() : null;
			this.lore = base.getItemMeta().hasLore() ? base.getItemMeta().getLore() : null;
		}

		/**
		 * Sets the {@link Material} of the item.
		 *
		 * @param material the material you want the item to be
		 * @return the item builder object
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
		 * @return the item builder object
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
		 * @return the item builder object
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
		 * @return the item builder object
		 */
		@Nonnull
		public Builder lore(@Nullable String[] lore) {
			if (lore == null) {
				return this;
			} else {
				return lore(List.of(lore));
			}
		}

		/**
		 * Sets the lore of the item.
		 *
		 * @param lore the lore you want the item to have
		 * @return the item builder object
		 */
		@Nonnull
		public Builder lore(@Nullable List<String> lore) {
			this.lore = lore;
			return this;
		}

		/**
		 * Hides the attributes of the item.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder hide() {
			hide(true);
			return this;
		}

		/**
		 * Hides the attributes of the item.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder hide(boolean hide) {
			this.hide = hide;
			return this;
		}

		/**
		 * Makes the item glow.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder glow() {
			glow(true);
			return this;
		}

		/**
		 * Makes the item glow.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder glow(boolean glow) {
			this.glow = glow;
			return this;
		}

		/**
		 * Makes the item unbreakable.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder unbreakable() {
			unbreakable(true);
			return this;
		}

		/**
		 * Makes the item unbreakable.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder unbreakable(boolean unbreakable) {
			this.unbreakable = unbreakable;
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
		 * @return the item builder object
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
			parse(sec, "hide", Boolean.class).ifPresent(this::hide);
			parse(sec, "glow", Boolean.class).ifPresent(this::glow);
			parse(sec, "unbreakable", Boolean.class).ifPresent(this::glow);
			return this;
		}

		/**
		 * Adds a placeholder to the list of placeholders.
		 *
		 * @param placeholder the placeholder you want to add
		 * @param replacement the value you want to replace the placeholder with
		 * @param <T>         any type
		 * @return the item builder object
		 */
		@Nonnull
		public <T> Builder placeholder(@Nonnull String placeholder, @Nonnull T replacement) {
			placeholders.add(placeholder, replacement);
			return this;
		}

		/**
		 * Adds a NBT tag (key-value pair) on an item.
		 *
		 * @param key  the key of the tag
		 * @param value the value corresponding to the key
		 * @return the item builder object
		 */
		@Nonnull
		public Builder nbt(@Nonnull String key, @Nonnull Object value) {
			nbt.put(key, value);
			return this;
		}

		/**
		 * Builds the {@link ItemStack} item.
		 *
		 * @return the item
		 */
		@Nonnull
		public ItemStack build() {
			Material type = material != null ? material : defMaterial;
			ItemStack item = baseItem != null ? baseItem : new ItemStack(Objects.requireNonNull(type, "Material is null."));
			ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Item meta is null.");

			if (name != null) meta.setDisplayName(placeholders.apply(name).toText());
			if (lore != null) meta.setLore(Arrays.asList(placeholders.apply(lore).toText().split("\n")));
			if (hide) {
				meta.setAttributeModifiers(HashMultimap.create());
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
			}
			if (glow) {
				meta.addEnchant(Enchantment.INFINITY, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			if (unbreakable) {
				meta.setUnbreakable(true);
				meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}

			item.setItemMeta(meta);
			nbt.forEach((key, value) -> NBT.setTag(item, key, value));
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
