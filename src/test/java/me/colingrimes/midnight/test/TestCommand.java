package me.colingrimes.midnight.test;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TestCommand implements Command<Midnight> {

	private final String executeMessage;
	private final List<String> tabComplete;

	public TestCommand() {
		this("Test executed successfully!");
	}

	public TestCommand(@Nonnull String executeMessage) {
		this(executeMessage, List.of("test", "testing", "tested"));
	}

	public TestCommand(@Nonnull String executeMessage, @Nonnull List<String> tabComplete) {
		this.executeMessage = executeMessage;
		this.tabComplete = tabComplete;
	}

	@Override
	public void execute(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		sender.message(executeMessage);
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull Midnight plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return tabComplete;
		} else {
			return null;
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Message.of("Usage: /test <arg1> <arg2>"));
		properties.setPermission("test.permission");
		properties.setAliases("t");
		properties.setArgumentsRequired(2);
		properties.setPlayerRequired(true);
	}
}
