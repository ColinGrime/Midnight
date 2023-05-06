package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageOption<T> implements Option<T>, Message<String> {

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
		if (value instanceof List<?> list) {
			if (list.stream().allMatch(item -> item instanceof String)) {
				messages = list.stream().map(String.class::cast).map(Text::color).collect(Collectors.toList());
			}
		}
	}

	@Nonnull
	@Override
	public String getContent() {
		return String.join("\n", messages);
	}

	@Override
	public void send(@Nonnull CommandSender recipient) {
		messages.forEach(recipient::sendMessage);
	}

	@Nonnull
	@Override
	public Message<String> replace(@Nonnull Placeholders placeholders) {
		messages = placeholders.apply(messages);
		return this;
	}
}
