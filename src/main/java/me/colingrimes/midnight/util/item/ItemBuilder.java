package me.colingrimes.midnight.util.item;

import me.colingrimes.midnight.locale.Placeholders;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides a simple way to build {@link ItemStack} objects.
 * - Supports parsing of {@link Material} enums from string formats.
 * - Supports parsing of {@link ConfigurationSection} objects.
 * - Supports replacing {@link Placeholders} from the name/lore.
 */
public final class ItemBuilder {

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
	 * @return the itembuilder object
	 */
	@Nonnull
	public ItemBuilder hide(boolean hide) {
		this.hide = hide;
		return this;
	}

	/**
	 * Makes the item glow.
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
	 * @param placeholder the placeholder you want to add
	 * @param replacement the value you want to replace the placeholder with
	 * @param <T> any type
	 * @return the placeholders object
	 */
	@NonNull
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
