package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.locale.Messageable;
import me.colingrimes.midnight.locale.Placeholders;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
public class Message<T> implements Option<T>, Messageable {

	private final Function<ConfigurationAdapter, ? extends T> function;
	private T value;
	private List<String> messages;

	public Message(@Nonnull Function<ConfigurationAdapter, ? extends T> function) {
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


	@Override
	public void sendTo(@Nonnull CommandSender sender, @Nullable Placeholders placeholders) {
		Objects.requireNonNull(sender, "Sender is null.");
		placeholders = Objects.requireNonNullElseGet(placeholders, Placeholders::create);
		placeholders.replace(messages).forEach(sender::sendMessage);
	}
}
