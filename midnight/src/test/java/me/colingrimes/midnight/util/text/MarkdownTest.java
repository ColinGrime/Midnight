package me.colingrimes.midnight.util.text;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MarkdownTest {

	@Test
	void testCommandAndHover() {
		String input = "Click [here](/command_with_underscores Hover text) to execute the command!";
		TextComponent textComponent = Markdown.of(input).getContent();
		assertNotNull(textComponent);

		var plainText = textComponent.toPlainText();
		var commandText = textComponent.getExtra().get(1).getClickEvent().getValue();
		var hoverText = ((Text) textComponent.getExtra().get(1).getHoverEvent().getContents().get(0)).getValue();

		assertEquals("Click here to execute the command!", plainText);
		assertEquals("/command with underscores", commandText);
		assertEquals("Hover text", hoverText);
	}

	@Test
	void testUrlAndHover() {
		String input = "Click [here](https://example.com Hover text) to visit the website!";
		TextComponent textComponent = Markdown.of(input).getContent();
		assertNotNull(textComponent);

		var plainText = textComponent.toPlainText();
		var urlText = textComponent.getExtra().get(1).getClickEvent().getValue();
		var hoverText = ((Text) textComponent.getExtra().get(1).getHoverEvent().getContents().get(0)).getValue();

		assertEquals("Click here to visit the website!", plainText);
		assertEquals("https://example.com", urlText);
		assertEquals("Hover text", hoverText);
	}

	@Test
	void testHover() {
		String input = "Hover [here](Hover text) to see the text!";
		TextComponent textComponent = Markdown.of(input).getContent();
		assertNotNull(textComponent);

		var plainText = textComponent.toPlainText();
		var hoverText = ((Text) textComponent.getExtra().get(1).getHoverEvent().getContents().get(0)).getValue();

		assertEquals("Hover here to see the text!", plainText);
		assertEquals("Hover text", hoverText);
	}
}
