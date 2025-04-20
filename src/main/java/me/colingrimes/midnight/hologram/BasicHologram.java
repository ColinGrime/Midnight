package me.colingrimes.midnight.hologram;

import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.util.bukkit.Entities;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BasicHologram implements Hologram {

	private final List<Line> lines = new ArrayList<>();
	private final Position position;
	private boolean active = false;

	public BasicHologram(@Nonnull Position position) {
		this.position = position;
	}

	/**
	 * Updates the {@link Hologram} to ensure each line is in a valid state.
	 */
	private void update() {
		for (int i=0; i<lines.size(); i++) {
			Line line = lines.get(i);
			if (line.index != i) {
				line.index = i;
				line.reposition();
			}
		}

		if (!active) {
			return;
		}

		for (Line line : lines) {
			if (line.stand == null || line.stand.isDead()) {
				line.spawn();
			}
		}
	}

	@Override
	public void spawn() {
		Hologram.SPAWNED_HOLOGRAMS.add(this);
		lines.forEach(Line::spawn);
		active = true;
	}

	@Override
	public void remove() {
		Hologram.SPAWNED_HOLOGRAMS.remove(this);
		lines.forEach(Line::remove);
		active = false;
	}

	@Override
	public boolean active() {
		return active;
	}

	@Nonnull
	@Override
	public Position getPosition() {
		return position;
	}

	@Nonnull
	@Override
	public List<String> getLines() {
		return lines.stream().map(line -> line.text).toList();
	}

	@Override
	public void setLines(@Nonnull List<String> newLines) {
		int prevSize = lines.size();
		int currSize = newLines.size();

		// Update existing lines.
		for (int i=0; i<Math.min(prevSize, currSize); i++) {
			lines.get(i).update(newLines.get(i));
		}

		// Some lines have been removed.
		while (lines.size() > newLines.size()) {
			lines.removeLast().remove();
		}

		// Some lines have been added.
		while (newLines.size() > lines.size()) {
			lines.add(new Line(lines.size(), newLines.get(lines.size())));
		}

		update();
	}

	@Override
	public void setLine(int index, @Nonnull String text) {
		// Add additional lines if needed.
		while (index >= lines.size()) {
			lines.add(new Line(lines.size(), ""));
		}

		// Set the specified line with the new text.
		lines.get(index).update(text);
	}

	@Override
	public void addLine(@Nonnull String text) {
		lines.add(new Line(lines.size(), text));
		update();
	}

	@Override
	public void removeLine(int index) {
		lines.remove(index);
		update();
	}

	private class Line {
		private int index;
		private String text;
		private ArmorStand stand;

		public Line(int index, @Nonnull String text) {
			this.index = index;
			this.text = text;
			if (active) {
				spawn();
			}
		}

		/**
		 * Spawns the hologram line.
		 */
		public void spawn() {
			if (stand != null && !stand.isDead()) {
				return;
			}

			Location location = position.subtract(0, 0.25 * index, 0).toLocation();
			if (!location.getChunk().isLoaded()) {
				location.getChunk().load(true);
			}

			Entities.nearby(ArmorStand.class, location, 0.1).forEach(ArmorStand::remove);
			stand = (ArmorStand) Entities.spawn(EntityType.ARMOR_STAND, location);
			stand.setAI(false);
			stand.setSmall(true);
			stand.setMarker(true);
			stand.setGravity(false);
			stand.setVisible(false);
			stand.setCollidable(false);
			stand.setInvulnerable(true);
			stand.setCustomName(text);
			stand.setCustomNameVisible(true);
		}

		/**
		 * Removes the hologram line.
		 */
		public void remove() {
			if (stand != null) {
				stand.remove();
			}
			stand = null;
		}

		/**
		 * Updates the line with new text.
		 *
		 * @param text the new next
		 */
		public void update(@Nonnull String text) {
			this.text = text;
			if (stand != null && !stand.isDead()) {
				stand.setCustomName(text);
			}
		}

		/**
		 * Repositions the line if it has moved.
		 */
		private void reposition() {
			if (stand != null && !stand.isDead()) {
				stand.teleport(position.subtract(0, 0.25 * index, 0).toLocation());
			}
		}
	}
}
