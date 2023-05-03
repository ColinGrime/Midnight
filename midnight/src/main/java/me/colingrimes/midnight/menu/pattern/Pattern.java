package me.colingrimes.midnight.menu.pattern;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pattern of slots in a {@link Gui} that can be filled with an item.
 */
public class Pattern {

    public static final Pattern BORDER = Pattern.create()
            .mask("111111111")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("111111111");

    private final List<String> patterns;
    private ItemStack item;

    /**
     * Creates a new Pattern instance.
     * @return the new Pattern instance
     */
    @Nonnull
    public static Pattern create() {
        return new Pattern();
    }

    /**
     * Creates a new Pattern instance with the given item.
     * @param item the item to place in the slots represented by '1' in the pattern
     * @return the new Pattern instance
     */
    @Nonnull
    public static Pattern of(@Nonnull ItemStack item) {
        return create().item(item);
    }

    /**
     * Creates a new Pattern instance with the given material.
     * @param material the material to place in the slots represented by '1' in the pattern
     * @return the new Pattern instance
     */
    @Nonnull
    public static Pattern of(@Nonnull Material material) {
        return create().item(Items.of(material).name("").build());
    }

    private Pattern() {
        this.patterns = new ArrayList<>();
    }

    /**
     * Adds a new mask to the pattern.
     * @param mask the pattern mask, using '0' for empty slots and '1' for slots to be filled
     * @return this Pattern instance for chaining
     * @throws IllegalStateException if more than 6 lines have been added to the pattern
     * @throws IllegalArgumentException if the mask contains characters other than '0' and '1'
     */
    @Nonnull
    public Pattern mask(@Nonnull String mask) {
        Preconditions.checkArgument(mask.length() <= 9, "The pattern is too large to fit in the Gui.");
        Preconditions.checkArgument(mask.chars().allMatch(c -> c == '0' || c == '1'), "Mask can only contain '0' and '1' characters.");
        Preconditions.checkState(patterns.size() < 6, "A Pattern cannot have more than 6 lines.");
        patterns.add(mask);
        return this;
    }

    /**
     * Sets the item to be placed in the slots represented by '1' in the pattern.
     * @param item the item to place in the slots
     * @return this Pattern instance for chaining
     */
    @Nonnull
    public Pattern item(@Nonnull ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * Sets the material to be placed in the slots represented by '1' in the pattern.
     * @param material the material to place in the slots
     * @return this Pattern instance for chaining
     */
    @Nonnull
    public Pattern item(@Nonnull Material material) {
        return item(Items.of(material).name("").build());
    }

    /**
     * Fills the slots in the {@link Gui} based on the current pattern and item.
     * @param gui the {@link Gui} in which to fill the slots
     * @throws IllegalArgumentException if the pattern is too large to fit in the Gui
     */
    public void fill(@Nonnull Gui gui) {


        // Fills the slots in the Gui based on the pattern.
        for (int i = 0; i< patterns.size(); i++) {
            String row = patterns.get(i);
            for (int j=0; j<row.length(); j++) {
                if (row.charAt(j) == '1') {
                    int slot = i * 9 + j;
                    gui.getSlot(slot).setItem(item);
                }
            }
        }
    }
}
