package me.colingrimes.channels.storage.exception;

import javax.annotation.Nonnull;

public class ChatterNotFoundException extends Exception {

    public ChatterNotFoundException(@Nonnull String message) {
        super(message);
    }
}
