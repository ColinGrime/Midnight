package me.colingrimes.midnight.menu.pattern;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a pattern of slots in a {@link Gui} that can be filled with different items.
 */
public class MultiPattern {

    private final List<String> patterns;
    private final Map<Character, ItemStack> items;

    /**
     * Creates a new MultiPattern instance.
     *
     * @return the new MultiPattern instance
     */
    @Nonnull
    public static MultiPattern create() {
        return new MultiPattern();
    }

    private MultiPattern() {
        this.patterns = new ArrayList<>();
        this.items = new HashMap<>();
    }

    /**
     * Adds a new mask to the pattern.
     *
     * @param mask the pattern mask, using '0' for empty slots and any other character for slots to be filled
     * @return this MultiPattern instance for chaining
     * @throws IllegalStateException if more than 6 lines have been added to the pattern
     */
    @Nonnull
    public MultiPattern mask(@Nonnull String mask) {
        Preconditions.checkArgument(mask.length() == 9, "The pattern is too large to fit in the Gui.");
        Preconditions.checkState(patterns.size() < 6, "A MultiPattern cannot have more than 6 lines.");
        patterns.add(mask);
        return this;
    }

    /**
     * Maps a character in the pattern to the given item.
     *
     * @param c the character to map to the item
     * @param item the item to place in the slots represented by the character in the pattern
     * @return this MultiPattern instance for chaining
     */
    @Nonnull
    public MultiPattern item(char c, @Nonnull ItemStack item) {
        Preconditions.checkArgument(c != '0', "Character '0' is reserved for empty slots and cannot be mapped to an item.");
        items.put(c, item);
        return this;
    }

    /**
     * Maps a character in the pattern to the given material.
     *
     * @param c the character to map to the material
     * @param material the material to place in the slots represented by the character in the pattern
     * @return this MultiPattern instance for chaining
     */
    @Nonnull
    public MultiPattern item(char c, @Nonnull Material material) {
        return item(c, Items.of(material).name("").build());
    }

    /**
     * Fills the slots in the {@link Gui} based on the current pattern and mapped items.
     *
     * @param gui the {@link Gui} in which to fill the slots
     * @throws IllegalArgumentException if the pattern is too large to fit in the Gui
     * @throws IllegalStateException if a character in the pattern is not mapped to an item
     */
    public void fill(@Nonnull Gui gui) {
        for (String line : patterns) {
            Preconditions.checkArgument(line.length() <= 9, "The pattern is too large to fit in the Gui.");
        }

        // Fills the slots in the Gui based on the pattern.
        for (int i=0; i<patterns.size(); i++) {
            String row = patterns.get(i);
            for (int j=0; j<row.length(); j++) {
                char c = row.charAt(j);
                if (c != '0') {
                    Preconditions.checkState(items.containsKey(c), "Character '" + c + "' in the pattern is not mapped to an item.");
                    int slot = i * 9 + j;
                    gui.getSlot(slot).setItem(items.get(c));
                }
            }
        }
    }
}
