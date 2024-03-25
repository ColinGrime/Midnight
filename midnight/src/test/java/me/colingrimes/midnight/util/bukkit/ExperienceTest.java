package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.MockSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceTest extends MockSetup {

	@Test
	void testAddExperience() {
		when(bukkit.player.getLevel()).thenReturn(10);
		when(bukkit.player.getExp()).thenReturn(0.5f);

		Experience.add(bukkit.player, 500);

		verify(bukkit.player).setLevel(anyInt());
		verify(bukkit.player).setExp(anyFloat());
	}

	@Test
	void testRemoveExperience() {
		when(bukkit.player.getLevel()).thenReturn(15);
		when(bukkit.player.getExp()).thenReturn(0.5f);

		Experience.remove(bukkit.player, 500);

		verify(bukkit.player).setLevel(anyInt());
		verify(bukkit.player).setExp(anyFloat());
	}

	@Test
	void testFromPlayer() {
		when(bukkit.player.getLevel()).thenReturn(5);

		int totalExp = Experience.fromPlayer(bukkit.player);
		assertEquals(55, totalExp);
	}

	@Test
	void testFromLevel() {
		int totalExp = Experience.fromLevel(10);
		assertEquals(160, totalExp);
	}

	@Test
	void testToLevel() {
		double level = Experience.toLevel(216L);
		assertEquals(12, level, 1e-9);
	}

	@Test
	void testToWholeLevel() {
		int level = Experience.toWholeLevel(1095L);
		assertEquals(27, level);
	}
}
