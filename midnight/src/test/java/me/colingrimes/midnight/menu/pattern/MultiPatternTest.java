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
public class MultiPatternTest {

	@Mock private Gui mockGui;
	@Mock private Slot mockSlot;
	@Mock private ItemStack mockItem1;
	@Mock private ItemStack mockItem2;

	@Test
	void testValidPatterns() {
		when(mockGui.getSlot(anyInt())).thenReturn(mockSlot);

		MultiPattern.create()
				.mask("AAAABAAAA")
				.mask("A000B000A")
				.mask("A000BC00A")
				.item('A', mockItem1)
				.item('B', mockItem2)
				.item('C', mockItem2)
				.bind('A', ClickType.LEFT, event -> {})
				.bind('B', ClickType.RIGHT, event -> {})
				.fill(mockGui);

		verify(mockGui, times(16)).getSlot(anyInt());
		verify(mockSlot, times(12)).setItem(mockItem1);
		verify(mockSlot, times(4)).setItem(mockItem2);
		verify(mockSlot, times(12)).bind(eq(ClickType.LEFT), any());
		verify(mockSlot, times(3)).bind(eq(ClickType.RIGHT), any());
	}

	@Test
	void testMaskExceptions() {
		assertThrows(IllegalArgumentException.class, () -> MultiPattern.create().mask("1111111111"));
		assertThrows(IllegalStateException.class, () ->
				MultiPattern.create()
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
						.mask("111")
		);
		assertThrows(IllegalArgumentException.class, () -> MultiPattern.create().item('0', mockItem1).mask("0"));
	}
}
