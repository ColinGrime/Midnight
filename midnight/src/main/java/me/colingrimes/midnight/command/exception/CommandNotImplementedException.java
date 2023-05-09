package me.colingrimes.midnight.command.exception;

import javax.annotation.Nonnull;

public class CommandNotImplementedException extends RuntimeException {

    public CommandNotImplementedException() {
        this("Command not implemented.");
    }

    public CommandNotImplementedException(@Nonnull String message) {
        super(message);
    }
}
