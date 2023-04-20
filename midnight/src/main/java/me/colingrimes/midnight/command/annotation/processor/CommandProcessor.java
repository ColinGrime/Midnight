package me.colingrimes.midnight.command.annotation.processor;

import me.colingrimes.midnight.annotation.AnnotationProcessor;
import me.colingrimes.midnight.command.annotation.Command;
import me.colingrimes.midnight.command.annotation.CommandPermission;
import me.colingrimes.midnight.command.annotation.CommandUsage;
import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.command.handler.factory.CommandHandlerFactory;
import me.colingrimes.midnight.MidnightPlugin;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandProcessor implements AnnotationProcessor {

	private final MidnightPlugin plugin;

	public CommandProcessor(@Nonnull MidnightPlugin plugin) {
		this.plugin = plugin;
	}

	@Nonnull
	@Override
	public Class<? extends Annotation> getAnnotation() {
		return Command.class;
	}

	@Override
	public void process(@Nonnull Method method) {
		CommandPermission commandPermission = method.getAnnotation(CommandPermission.class);
		CommandUsage commandUsage = method.getAnnotation(CommandUsage.class);

		String command = method.getAnnotation(Command.class).value();
		String permission = commandPermission == null ? null : commandPermission.value();
		String usage = commandUsage == null ? null : commandUsage.value();

		for (String commandAlias : parseCommandAliases(command)) {
			try {
				CommandHandler handler = CommandHandlerFactory.create(method, permission, usage);
				plugin.getCommandRegistry().register(commandAlias.split(" "), handler);
			} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Parses a command input into a list of all command aliases.
	 * @param commandInput the command input to parse
	 * @return a list of all command aliases
	 */
	@Nonnull
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
