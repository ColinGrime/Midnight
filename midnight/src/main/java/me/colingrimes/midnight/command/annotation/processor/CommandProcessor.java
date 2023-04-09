package me.colingrimes.midnight.command.annotation.processor;

import me.colingrimes.midnight.annotation.AnnotationProcessor;
import me.colingrimes.midnight.command.annotation.Command;
import me.colingrimes.midnight.command.annotation.CommandPermission;
import me.colingrimes.midnight.command.annotation.CommandUsage;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandProcessor implements AnnotationProcessor {

	@Nonnull
	@Override
	public Class<? extends Annotation> getAnnotation() {
		return Command.class;
	}

	@Override
	public void process(@Nonnull Method method) {
		String command = method.getAnnotation(Command.class).value();
		String permission = nonNull(method.getAnnotation(CommandPermission.class).value());
		String usage = nonNull(method.getAnnotation(CommandUsage.class).value());
	}

	/**
	 * Returns the string if it's not null, otherwise returns an empty string.
	 * @param str the string to check
	 * @return the non-null string
	 */
	private String nonNull(String str) {
		return Objects.requireNonNullElse(str, "");
	}

	/**
	 * Parses a command input into a list of all command aliases.
	 * @param commandInput the command input to parse
	 * @return a list of all command aliases
	 */
	private List<String> parseCommandAliases(String commandInput) {
		List<String> commands = List.of("");

		// Iterate over each word in the command input.
		for (String input : commandInput.split(" ")) {
			// Get the aliases for the current word.
			String[] aliases = input.split("\\|");
			List<String> newCommands = new ArrayList<>();

			// Iterate over the existing command combinations.
			for (String command : commands) {
				// Iterate over each alias for the current word.
				for (String alias : aliases) {
					if (command.isEmpty()) {
						newCommands.add(alias);
					} else {
						newCommands.add(command + " " + alias);
					}
				}
			}

			commands = newCommands;
		}

		return commands;
	}
}
