package me.colingrimes.midnight.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Point<T extends Direction> {

	private final Position position;
	private final T direction;

	/**
	 * Constructs a new Point with the given position and direction.
	 * @param position the position
	 * @param direction the direction
	 * @return the point
	 * @param <T> the type of direction
	 */
	public static <T extends Direction> Point<T> of(@Nonnull Position position, @Nonnull T direction) {
		return new Point<>(position, direction);
	}

	private Point(@Nonnull Position position, @Nonnull T direction) {
		this.position = position;
		this.direction = direction;
	}

	/**
	 * Gets the position of the point.
	 * @return the position of the point
	 */
	@Nonnull
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the direction of the point.
	 * @return the direction of the point
	 */
	@Nonnull
	public T getDirection() {
		return direction;
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (this == o) return true;
		if (!(o instanceof Point<?> point)) return false;
		return Objects.equals(getPosition(), point.getPosition()) && Objects.equals(getDirection(), point.getDirection());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPosition(), getDirection());
	}

	@Nonnull
	@Override
	public String toString() {
		return "Point{" +
				"position=" + position +
				", direction=" + direction +
				'}';
	}
}