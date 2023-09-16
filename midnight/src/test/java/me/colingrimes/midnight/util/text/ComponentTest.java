package me.colingrimes.midnight.util.text;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.implementation.ComponentMessage;
import me.colingrimes.midnight.message.implementation.ListMessage;
import me.colingrimes.midnight.message.implementation.TextMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentTest {

    private static final TextComponent basicComponent = new TextComponent("Hello, {name}!");
    private static final TextComponent complexComponent;

    private static final String namePlaceholder = "{name}";
    private static final String colorPlaceholder = "{color}";
    private static final String agePlaceholder = "{age}";

    static {
        complexComponent = Component
                .create("This is an advanced component, {name}!")
                .add("Oh! &cThere is even more text! The color is &a{color}.")
                .add("&aWow. Even more, with an age of &l{age}!")
                .build();
        complexComponent.setColor(ChatColor.BLUE);
        complexComponent.setBold(true);
    }

    @Test
    public void testOfAndColor() {
        TextComponent result1 = Component.of("&aThis is a test!");
        assertEquals("This is a test!", result1.getText());
        assertEquals(ChatColor.GREEN, result1.getColor());
        assertFalse(result1.isBold());

        TextComponent result2 = Component.of("&9&lThis &cis &aa &etest!");
        assertEquals("This §cis §aa §etest!", result2.getText());
        assertEquals(ChatColor.BLUE, result2.getColor());
        assertTrue(result2.isBold());

        TextComponent result3 = Component.of("This &lis a &9test!");
        assertEquals("This §lis a §9test!", result3.getText());
        assertEquals(ChatColor.WHITE, result3.getColor());
    }

    @Test
    public void testReplaceWithString() {
        TextMessage nameValue = new TextMessage("John");
        TextMessage colorValue = new TextMessage("Blue");
        TextMessage ageValue = new TextMessage("8");

        TextComponent basicResult = Component.replace(basicComponent, namePlaceholder, nameValue);
        assertEquals("Hello, John!", basicResult.getText());

        TextComponent complexResult = Component.replace(complexComponent, namePlaceholder, nameValue);
        complexResult = Component.replace(complexResult, colorPlaceholder, colorValue);
        complexResult = Component.replace(complexResult, agePlaceholder, ageValue);
        assertNotNull(complexResult.getExtra());
        assertEquals("This is an advanced component, John!\n", complexResult.getText());
        assertEquals("Oh! §cThere is even more text! The color is §aBlue.\n", getExtraText(complexResult, 0));
        assertEquals("Wow. Even more, with an age of §l8!", getExtraText(complexResult, 1));
    }

    @Test
    public void testReplaceWithComponent() {
        ComponentMessage nameValue = new ComponentMessage(new TextComponent("John"));
        ComponentMessage colorValue = new ComponentMessage(new TextComponent("Blue"));
        ComponentMessage ageValue = new ComponentMessage(new TextComponent("8"));

        TextComponent basicResult = Component.replace(basicComponent, namePlaceholder, nameValue);
        assertNotNull(basicResult.getExtra());
        assertEquals("Hello, ", basicResult.getText());
        assertEquals("John", getExtraText(basicResult, 0));
        assertEquals("!", getExtraText(basicResult, 1));

        TextComponent complexResult = Component.replace(complexComponent, namePlaceholder, nameValue);
        complexResult = Component.replace(complexResult, colorPlaceholder, colorValue);
        complexResult = Component.replace(complexResult, agePlaceholder, ageValue);
        assertNotNull(complexResult.getExtra());
        assertEquals("This is an advanced component, ", complexResult.getText());
        assertEquals("John", getExtraText(complexResult, 0));
        assertEquals("!\n", getExtraText(complexResult, 1));
        assertEquals("Oh! §cThere is even more text! The color is ", getExtraText(complexResult, 2));
        assertEquals("Blue", getExtraText(complexResult, 3));
        assertEquals(".\n", getExtraText(complexResult, 4));
        assertEquals("Wow. Even more, with an age of ", getExtraText(complexResult, 5));
        assertEquals("8", getExtraText(complexResult, 6));
        assertEquals("!", getExtraText(complexResult, 7));
    }

    @Test
    public void testReplaceWithStringList() {
        ListMessage nameValue = new ListMessage(Arrays.asList("John", "Doe"));
        ListMessage colorValue = new ListMessage(Arrays.asList("Blue", "Red"));
        ListMessage ageValue = new ListMessage(Arrays.asList("8", "4"));

        TextComponent basicResult = Component.replace(basicComponent, namePlaceholder, nameValue);
        assertEquals("Hello, John Doe!", basicResult.getText());

        TextComponent complexResult = Component.replace(complexComponent, namePlaceholder, nameValue);
        complexResult = Component.replace(complexResult, colorPlaceholder, colorValue);
        complexResult = Component.replace(complexResult, agePlaceholder, ageValue);
        assertNotNull(complexResult.getExtra());
        assertEquals("This is an advanced component, John Doe!\n", complexResult.getText());
        assertEquals("Oh! §cThere is even more text! The color is §aBlue Red.\n", getExtraText(complexResult, 0));
        assertEquals("Wow. Even more, with an age of §l8 4!", getExtraText(complexResult, 1));
    }

    @Test
    public void testReplaceWithNoPlaceholder() {
        var component = new TextComponent("Hello, World!");
        var value = Message.of("John");

        TextComponent result = Component.replace(component, namePlaceholder, value);
        assertEquals("Hello, World!", result.getText());
    }

    @Nonnull
    private String getExtraText(@Nonnull TextComponent component, int index) {
        return ((TextComponent) component.getExtra().get(index)).getText();
    }
}
