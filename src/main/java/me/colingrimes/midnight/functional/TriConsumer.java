package me.colingrimes.midnight.functional;

import javax.annotation.Nonnull;

/**
 * Represents an operation that takes three input arguments and returns no result.
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {

    /**
     * Performs an operation on the given arguments.
     *
     * @param a first input
     * @param b second input
     * @param c third input
     */
    void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c);
}
