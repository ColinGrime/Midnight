package me.colingrimes.midnight.message;

import me.clip.placeholderapi.PlaceholderAPI;
import me.colingrimes.midnight.message.implementation.ComponentMessage;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Component;
import me.colingrimes.midnight.util.text.Text;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class designed for managing and applying multiple placeholders
 * within different types of input, including strings, lists of strings, or components.
 * <p>
 * In addition, this class also supports the use of PlaceholderAPI placeholders.
 */
public class Placeholders {

	private final Map<String, Message<?>> placeholders = new HashMap<>();
	private Player player;

	/**
	 * Creates a new placeholders object.
	 *
	 * @return the placeholders object
	 */
	@Nonnull
	public static Placeholders create() {
		return new Placeholders();
	}

	/**
	 * Creates a new placeholders object with the specified player.
	 * This is used for PlaceholderAPI placeholders.
	 *
	 * @param player the player
	 * @return the placeholders object
	 */
	@Nonnull
	public static Placeholders create(@Nullable Player player) {
		Placeholders placeholders = new Placeholders();
		placeholders.player = player;
		return placeholders;
	}

	/**
	 * Creates a new placeholders object.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	@Nonnull
	public static <T> Placeholders of(@Nonnull String placeholder, @Nonnull T value) {
		return new Placeholders().add(placeholder, value);
	}

	/**
	 * Creates a new placeholders object.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 */
	@Nonnull
	public static Placeholders of(@Nonnull String placeholder, @Nonnull Message<?> value) {
		return new Placeholders().add(placeholder, value);
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	@Nonnull
	public <T> Placeholders add(@Nonnull String placeholder, @Nonnull T value) {
		placeholders.put(placeholder, Message.of(String.valueOf(value)));
		return this;
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 */
	@Nonnull
	public Placeholders add(@Nonnull String placeholder, @Nonnull Message<?> value) {
		placeholders.put(placeholder, value);
		return this;
	}

	/**
	 * Applies all placeholders in a string.
	 *
	 * @param str the string to apply placeholders to
	 * @return the new string with applied placeholders
	 */
	@Nonnull
	public ComponentMessage apply(@Nullable String str) {
		if (str == null) {
			return new ComponentMessage();
		}

		// Apply the placeholders in the string.
		TextComponent component = Component.of(str);
		for (Map.Entry<String, Message<?>> replacement : placeholders.entrySet()) {
			component = Component.replace(component, replacement.getKey(), replacement.getValue());
		}

		// Apply the PlaceholderAPI placeholders.
		applyPlaceholderAPI(component);
		return new ComponentMessage(component);
	}

	/**
	 * Applies all placeholders in all strings.
	 *
	 * @param strList the list of strings to apply placeholders to
	 * @return the new list of strings with applied placeholders
	 */
	@Nonnull
	public ComponentMessage apply(@Nullable List<String> strList) {
		if (strList == null) {
			return new ComponentMessage();
		}

		ComponentMessage component = apply(strList.get(0));
		for (int i=1; i<strList.size(); i++) {
			component.getContent().addExtra(apply(strList.get(i)).getContent());
		}
		return component;
	}

	/**
	 * Applies all placeholders in a component.
	 *
	 * @param component the component to apply placeholders to
	 * @return the new component with applied placeholders
	 */
	@Nonnull
	public ComponentMessage apply(@Nullable TextComponent component) {
		if (component == null) {
			return new ComponentMessage();
		}

		// Apply the placeholders in the main component.
		ComponentMessage message = apply(component.getText());
		TextComponent mainComponent = message.getContent();
		mainComponent.setClickEvent(component.getClickEvent());
		mainComponent.setHoverEvent(component.getHoverEvent());

		if (component.getExtra() == null) {
			return message;
		}

		// Apply the placeholders in the extra components.
		for (BaseComponent baseComponent : component.getExtra()) {
			if (!(baseComponent instanceof TextComponent)) {
				mainComponent.addExtra(baseComponent);
				continue;
			}

			ComponentMessage extraMessage = apply(((TextComponent) baseComponent).getText());
			TextComponent extraComponent = extraMessage.getContent();
			extraComponent.setClickEvent(extraComponent.getClickEvent());
			extraComponent.setHoverEvent(extraComponent.getHoverEvent());
			mainComponent.addExtra(extraComponent);
		}

		return message;
	}

	/**
	 * Applies all placeholders in a message.
	 *
	 * @param message the message to apply placeholders to
	 * @return the new message with applied placeholders
	 */
	@Nonnull
	public ComponentMessage apply(@Nonnull Message<?> message) {
		if (message.getContent() instanceof String content) {
			return apply(content);
		} else if (message.getContent() instanceof TextComponent component) {
			return apply(component);
		} else if (Types.asStringList(message.getContent()).isPresent()) {
			return apply(Types.asStringList(message.getContent()).get());
		} else {
			throw new IllegalArgumentException("Invalid message content type: " + message.getContent().getClass().getName());
		}
	}

	/**
	 * Applies PlaceholderAPI placeholders to a component.
	 *
	 * @param component the component to apply placeholders to
	 */
	private void applyPlaceholderAPI(@Nonnull TextComponent component) {
		if (Common.getPlugin("PlaceholderAPI") == null) {
			return;
		}

		component.setText(Text.color(PlaceholderAPI.setPlaceholders(player, component.getText().replaceAll("\\{(.+?)}", "%$1%"))));

		// Replace the placeholder in the hover event.
		if (component.getHoverEvent() != null) {
			HoverEvent hoverEvent = component.getHoverEvent();
			List<Content> contents = new ArrayList<>();

			for (Content content : hoverEvent.getContents()) {
				if (!(content instanceof net.md_5.bungee.api.chat.hover.content.Text text)) {
					contents.add(content);
					continue;
				} else if (text.getValue() instanceof String str) {
					String replaced = Text.color(PlaceholderAPI.setPlaceholders(player, str.replaceAll("\\{(.+?)}", "%$1%")));
					contents.add(new net.md_5.bungee.api.chat.hover.content.Text(replaced));
					continue;
				} else if (!(text.getValue() instanceof BaseComponent[])) {
					contents.add(content);
					continue;
				}

				BaseComponent[] baseComponents = (BaseComponent[]) text.getValue();
				for (BaseComponent baseComponent : baseComponents) {
					if (baseComponent instanceof TextComponent textComponent) {
						applyPlaceholderAPI(textComponent);
					}
				}
				contents.add(new net.md_5.bungee.api.chat.hover.content.Text(baseComponents));
			}

			component.setHoverEvent(new HoverEvent(hoverEvent.getAction(), contents));
		}

		// Replace the placeholder in the click event.
		if (component.getClickEvent() != null) {
			ClickEvent clickEvent = component.getClickEvent();
			if (clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND || clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
				String replaced = PlaceholderAPI.setPlaceholders(player, clickEvent.getValue().replaceAll("\\{(.+?)}", "%$1%"));
				component.setClickEvent(new ClickEvent(clickEvent.getAction(), replaced));
			}
		}

		// Recursively replace placeholders in the extras of the original component.
		if (component.getExtra() != null) {
			for (BaseComponent extra : component.getExtra()) {
				if (extra instanceof TextComponent extraComponent) {
					applyPlaceholderAPI(extraComponent);
				}
			}
		}
	}
}
