package me.colingrimes.midnight.model;

import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

/**
 * Represents a block and the material it should be changed to later.
 * <p>
 * Intended for deferred or scheduled block updates.
 */
public class DeferredBlock {

	private final Block block;
	private final Material type;

	/**
	 * Constructs a new {@link DeferredBlock}.
	 *
	 * @param block the block
	 * @param type the material type to change the block to
	 */
	public DeferredBlock(@Nonnull Block block, @Nonnull Material type) {
		this.block = block;
		this.type = type;
	}

	/**
	 * Gets the block.
	 *
	 * @return the block
	 */
	@Nonnull
	public Block getBlock() {
		return block;
	}

	/**
	 * Gets the material type that will be applied to the block.
	 *
	 * @return the material type
	 */
	@Nonnull
	public Material getType() {
		return type;
	}

	/**
	 * Sets the block to the stored material type.
	 */
	public void apply() {
		block.setType(type);
	}

	/**
	 * Sets the block to the stored material type.
	 * Does not apply physics to the block.
	 */
	public void applyNoPhysics() {
		block.setType(type, false);
	}
}
