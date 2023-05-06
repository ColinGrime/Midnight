package me.colingrimes.midnight.util.text;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MarkdownTest {

	@Test
	void testMarkdownWithCommand() {
		String input = "Click [here](/command) to execute the command!";
		TextComponent textComponent = Markdown.of(input).getContent();

		assertNotNull(textComponent, "Text component should not be null");

		assertEquals("Click here to execute the command!", textComponent.toPlainText(),
				"Parsed text component should have the correct plain text");

		assertEquals("/command", textComponent.getExtra().get(1).getClickEvent().getValue(),
				"Parsed text component should have the correct command in click event");
	}

	@Test
	void testMarkdownWithUrl() {
		String input = "Click [here](https://example.com) to visit the website!";
		TextComponent textComponent = Markdown.of(input).getContent();

		assertNotNull(textComponent, "Text component should not be null");

		assertEquals("Click here to visit the website!", textComponent.toPlainText(),
				"Parsed text component should have the correct plain text");

		assertEquals("https://example.com", textComponent.getExtra().get(1).getClickEvent().getValue(),
				"Parsed text component should have the correct URL in click event");
	}

	@Test
	void testMarkdownWithHoverText() {
		String input = "Hover [here](Hover text) to see the text!";
		TextComponent textComponent = Markdown.of(input).getContent();

		assertNotNull(textComponent, "Text component should not be null");

		assertEquals("Hover here to see the text!", textComponent.toPlainText(),
				"Parsed text component should have the correct plain text");

		Text hoverContent = (Text) textComponent.getExtra().get(1).getHoverEvent().getContents().get(0);
		assertEquals("Hover text", hoverContent.getValue(),
				"Parsed text component should have the correct hover text");
	}
}
