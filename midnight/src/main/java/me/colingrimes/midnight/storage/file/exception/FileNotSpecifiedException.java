package me.colingrimes.midnight.storage.file.exception;

import javax.annotation.Nonnull;

public class FileNotSpecifiedException extends Exception {

	public FileNotSpecifiedException() {
		this("The identifier has no file name specified. Data has nowhere to be stored.");
	}

	public FileNotSpecifiedException(@Nonnull String message) {
		super(message);
	}
}
