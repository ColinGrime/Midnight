package me.colingrimes.midnight.message;

import me.colingrimes.midnight.MockSetup;
import me.colingrimes.midnight.message.implementation.ComponentMessage;
import me.colingrimes.midnight.util.text.Component;
import me.colingrimes.midnight.util.text.Tooltip;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceholdersTest extends MockSetup {

    private static final Placeholders stringPlaceholders = Placeholders
            .of("{food}", "Apple")
            .add("{letter}", 'C')
            .add("{color}", Message.of("Blue"));

    private static final Placeholders componentPlaceholders = Placeholders
            .of("{sender}", Tooltip.create("Player", List.of("Hello!", "This is a tooltip!")));

    private static final Placeholders stringListPlaceholders = Placeholders
            .of("{foods}", Message.of(List.of("Banana", "Cherry")))
            .add("{letters}", Message.of(List.of("A", "B", "C")))
            .add("{colors}", Message.of(List.of("Red", "Green")));

    private static final Placeholders allPlaceholders = Placeholders
            .of("{food}", "Orange")
            .add("{look}", Tooltip.create("Look here!", List.of("This is a secret")))
            .add("{letters}", Message.of(List.of("D", "E", "F")));

    @Test
    void testStringPlaceholders() {
        String expected1 = "I like Apple and my favorite letter is C with Blue color.";
        String expected2 = "I like Apple.\nThe C letter is nice.\nBlue is awesome.";

        // Test with String.
        String test1 = "I like {food} and my favorite letter is {letter} with {color} color.";
        ComponentMessage result1 = stringPlaceholders.apply(test1);
        assertEquals(expected1, result1.toText());
        assertNull(result1.getContent().getExtra());

        // Test with List of Strings.
        List<String> test2 = List.of("I like {food}.", "The {letter} letter is nice.", "{color} is awesome.");
        ComponentMessage result2 = stringPlaceholders.apply(test2);
        assertEquals(expected2, result2.toText());
        assertNull(result2.getContent().getExtra());

        // Test with Text Component.
        TextComponent test3 = Component
                .create("I like {food}.")
                .add("The {letter} letter is nice.")
                .add("{color} is awesome.")
                .build();
        ComponentMessage result3 = stringPlaceholders.apply(test3);
        assertEquals(expected2, result3.toText());
        assertNotNull(result3.getContent().getExtra());
        assertEquals(2, result3.getContent().getExtra().size());

    }

    @Test
    void testComponentPlaceholders() {
        String expected1 = "Hello, Player! Hope you're doing well.";
        String expected2 = "You are the Player\nPlayer is cool.";

        // Test with String.
        String test1 = "Hello, {sender}! Hope you're doing well.";
        ComponentMessage result1 = componentPlaceholders.apply(test1);
        assertEquals(expected1, result1.toText());
        assertNotNull(result1.getContent().getExtra());
        assertEquals(2, result1.getContent().getExtra().size());

        // Test with List of Strings.
        List<String> test2 = List.of("You are the {sender}", "{sender} is cool.");
        ComponentMessage result2 = componentPlaceholders.apply(test2);
        assertEquals(expected2, result2.toText());
        assertNotNull(result2.getContent().getExtra());
        assertEquals(4, result2.getContent().getExtra().size());

        // Test with Text Component.
        TextComponent test3 = Component.create("You are the {sender}").add("{sender} is cool.").build();
        ComponentMessage result3 = componentPlaceholders.apply(test3);
        assertEquals(expected2, result3.toText());
        assertNotNull(result3.getContent().getExtra());
        assertEquals(4, result3.getContent().getExtra().size());
    }

    @Test
    void testStringListPlaceholders() {
        String expected1 = "I like Banana Cherry and my favorite letters are A B C with Red Green colors.";
        String expected2 = "I like Banana Cherry.\nThe letters are A B C.\nThe colors are Red Green.";

        // Test with String.
        String test1 = "I like {foods} and my favorite letters are {letters} with {colors} colors.";
        ComponentMessage result1 = stringListPlaceholders.apply(test1);
        assertEquals(expected1, result1.toText());
        assertNull(result1.getContent().getExtra());

        // Test with List of Strings.
        List<String> test2 = List.of("I like {foods}.", "The letters are {letters}.", "The colors are {colors}.");
        ComponentMessage result2 = stringListPlaceholders.apply(test2);
        assertEquals(expected2, result2.toText());
        assertNull(result2.getContent().getExtra());

        // Test with Text Component.
        TextComponent test3 = Component
                .create("I like {foods}.")
                .add("The letters are {letters}.")
                .add("The colors are {colors}.")
                .build();
        ComponentMessage result3 = stringListPlaceholders.apply(test3);
        assertEquals(expected2, result3.toText());
        assertNotNull(result3.getContent().getExtra());
        assertEquals(2, result3.getContent().getExtra().size());
    }

    @Test
    void testAllPlaceholders() {
        String expected1 = "I like Orange and Look here! with D E F colors.";
        String expected2 = "The Orange is good.\nLook at Look here!.\nLetters are D E F.";

        // Test with String.
        String test1 = "I like {food} and {look} with {letters} colors.";
        ComponentMessage result1 = allPlaceholders.apply(test1);
        assertEquals(expected1, result1.toText());
        assertNotNull(result1.getContent().getExtra());
        assertEquals(2, result1.getContent().getExtra().size());

        // Test with List of Strings.
        List<String> test2 = List.of("The {food} is good.", "Look at {look}.", "Letters are {letters}.");
        ComponentMessage result2 = allPlaceholders.apply(test2);
        assertEquals(expected2, result2.toText());
        assertNotNull(result2.getContent().getExtra());
        assertEquals(2, result2.getContent().getExtra().size());

        // Test with Text Component.
        TextComponent test3 = Component
                .create("The {food} is good.")
                .add("Look at {look}.")
                .add("Letters are {letters}.")
                .build();
        ComponentMessage result3 = allPlaceholders.apply(test3);
        assertEquals(expected2, result3.toText());
        assertNotNull(result3.getContent().getExtra());
        assertEquals(4, result3.getContent().getExtra().size());
    }
}
