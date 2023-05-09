package me.colingrimes.midnight.util.text;

import me.colingrimes.midnight.message.implementation.ComponentMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.colingrimes.midnight.util.text.Text.color;

public class Markdown {

	private static final Pattern MARKDOWN = Pattern.compile("\\[(.*?)]\\((.*?)\\)");

	/**
	 * Parses a string with custom markup into a TextComponent.
	 *
	 * <p>The custom markup format supports the following:</p>
	 * <ul>
	 *   <li>[Text](Value)</li>
	 * </ul>
	 *
	 * <p>Where Text is the visible text, and Value determines the event:</p>
	 * <ul>
	 *   <li>If it starts with a "/", it's a command.</li>
	 *   <li>If it starts with a URL, it's a URL you can click on.</li>
	 *   <li>If it starts with anything else, it's just hoverable text.</li>
	 * </ul>
	 *
	 * Additionally, if Value has a URL followed by more text, the additional text acts as hoverable text.
	 * Color codes using '&' can be used anywhere in the input string.
	 *
	 * @param input the input string with custom markup
	 * @return a text component with the parsed events and colors
	 */
	public static ComponentMessage of(@Nonnull String input) {
		TextComponent message = new TextComponent();
		Matcher matcher = MARKDOWN.matcher(input);
		int lastEnd = 0;

		while (matcher.find()) {
			// Add the text before the [Text](Value) group.
			String before = input.substring(lastEnd, matcher.start());
			message.addExtra(color(before));

			// Get the [Text](Value) groups.
			String eventText = matcher.group(1);
			String eventValue = matcher.group(2);

			// Handle the TextComponent events.
			TextComponent eventComponent = new TextComponent(color(eventText));
			handleEvent(eventComponent, eventValue);
			message.addExtra(eventComponent);

			// Set the last end to the end of the [Text](Value) group.
			lastEnd = matcher.end();
		}

		// Add the remaining text after the last [Text](Value) group.
		String remaining = input.substring(lastEnd);
		message.addExtra(color(remaining));

		return new ComponentMessage(message);
	}

	/**
	 * Handles the event based on the event value and sets the
	 * appropriate event to the provided {@link TextComponent}.
	 *
	 * @param eventComponent the text component to apply the event to
	 * @param eventValue the event value from the input string
	 */
	private static void handleEvent(@Nonnull TextComponent eventComponent, @Nonnull String eventValue) {
		if (eventValue.startsWith("/")) {
			eventComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, eventValue));
		} else if (eventValue.startsWith("http://") || eventValue.startsWith("https://")) {
			handleUrlEvent(eventComponent, eventValue);
		} else {
			eventComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getContent(eventValue)));
		}
	}

	/**
	 * Handles URL events and sets the appropriate ClickEvent and
	 * HoverEvent (if applicable) to the provided {@link TextComponent}.
	 *
	 * @param eventComponent the text component to apply the event to
	 * @param eventValue     the event value from the input string
	 */
	private static void handleUrlEvent(@Nonnull TextComponent eventComponent, @Nonnull String eventValue) {
		int spaceIndex = eventValue.indexOf(' ');
		if (spaceIndex == -1) {
			eventComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, eventValue));
			return;
		}

		String url = eventValue.substring(0, spaceIndex);
		String hoverText = eventValue.substring(spaceIndex + 1);
		eventComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		eventComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getContent(hoverText)));
	}

	/**
	 * Gets the content for the provided text.
	 *
	 * @param text the text to get the content for
	 * @return the content for the provided text
	 */
	private static Content getContent(@Nonnull String text) {
		return new net.md_5.bungee.api.chat.hover.content.Text(color(text));
	}

	private Markdown() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
