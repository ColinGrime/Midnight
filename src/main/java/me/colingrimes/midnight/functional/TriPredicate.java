package me.colingrimes.midnight.functional;

import javax.annotation.Nonnull;

/**
 * Represents a predicate with three input arguments.
 */
@FunctionalInterface
public interface TriPredicate<A, B, C> {

	/**
	 * Evaluates this predicate on the given arguments.
	 *
	 * @param a first input
	 * @param b second input
	 * @param c third input
	 * @return true if the input matches the predicate
	 */
	boolean test(@Nonnull A a, @Nonnull B b, @Nonnull C c);
}
