package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageOption<T> implements Option<T>, Message<List<String>> {

	private final Function<ConfigurationAdapter, ? extends T> function;
	private T value;
	private List<String> messages;

	public MessageOption(@Nonnull Function<ConfigurationAdapter, ? extends T> function) {
		this.function = function;
		this.reload(null);
	}

	@Nonnull
	@Override
	public T get() {
		return value;
	}

	@Override
	public void reload(@Nullable ConfigurationAdapter adapter) {
		value = function.apply(adapter);

		// Convert string message.
		if (value instanceof String) {
			messages = List.of(Text.color((String) value));
		}

		// Convert list of string messages.
		if (Types.asStringList(value).isPresent()) {
			messages = Types.asStringList(value).get().stream().map(Text::color).collect(Collectors.toList());
		}
	}

	@Nonnull
	@Override
	public List<String> getContent() {
		return messages;
	}

	@Override
	public void send(@Nonnull CommandSender recipient) {
		messages.forEach(recipient::sendMessage);
	}
}
