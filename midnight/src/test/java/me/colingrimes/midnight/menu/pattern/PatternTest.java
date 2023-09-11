package me.colingrimes.midnight.menu.pattern;

import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.menu.Slot;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatternTest {

	@Mock private Gui mockGui;
	@Mock private Slot mockSlot;
	@Mock private ItemStack mockItem;

	@Test
	public void testValidPatterns() {
		when(mockGui.getSlot(anyInt())).thenReturn(mockSlot);

		Pattern.create()
				.mask("111111111")
				.mask("100000001")
				.mask("100000001")
				.item(mockItem)
				.bind(ClickType.LEFT, event -> {})
				.fill(mockGui);

		verify(mockGui, times(13)).getSlot(anyInt());
		verify(mockSlot, times(13)).setItem(mockItem);
		verify(mockSlot, times(13)).bind(eq(ClickType.LEFT), any());

		Pattern.of(mockItem)
				.mask("111111111")
				.mask("")
				.mask("")
				.mask("")
				.mask("")
				.mask("111111111")
				.bind(ClickType.LEFT, event -> {})
				.bind(ClickType.RIGHT, event -> {})
				.fill(mockGui);

		verify(mockGui, times(31)).getSlot(anyInt());
		verify(mockSlot, times(31)).setItem(mockItem);
		verify(mockSlot, times(49)).bind(any(ClickType.class), any());
	}

	@Test
	public void testMaskExceptions() {
		assertThrows(IllegalArgumentException.class, () -> Pattern.create().mask("1111111111"));
		assertThrows(IllegalArgumentException.class, () -> Pattern.create().mask("1112"));
		assertThrows(IllegalStateException.class, () ->
				Pattern.create()
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
		);
	}
}
