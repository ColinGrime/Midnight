package me.colingrimes.midnight.util.misc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public final class Types {

    /**
     * Gets the object as a string list if the conversion is possible.
     *
     * @param candidateList the object to check for a list of strings
     * @return an optional containing a list of strings if available
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static Optional<List<String>> asStringList(@Nonnull Object candidateList) {
        if (candidateList instanceof List<?> list && !list.isEmpty() && list.stream().allMatch(item -> item instanceof String)) {
            return Optional.of((List<String>) list);
        } else {
            return Optional.empty();
        }
    }

    private Types() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
