package me.colingrimes.channels.storage.exception;

import javax.annotation.Nonnull;

public class ParticipantNotFoundException extends Exception {

    public ParticipantNotFoundException(@Nonnull String message) {
        super(message);
    }
}
