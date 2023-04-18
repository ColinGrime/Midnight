package me.colingrimes.midnight.command.util.exception;

import javax.annotation.Nonnull;

public class CommandNotImplementedException extends RuntimeException {

    public CommandNotImplementedException() {
        this("Command not implemented.");
    }

    public CommandNotImplementedException(@Nonnull String message) {
        super(message);
    }
}
