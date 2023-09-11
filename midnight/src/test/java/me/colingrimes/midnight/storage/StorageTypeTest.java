package me.colingrimes.midnight.storage;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StorageTypeTest {

	@Test
	public void testGetName() {
		assertEquals("YAML", StorageType.YAML.getName());
		assertEquals("JSON", StorageType.JSON.getName());
		assertEquals("MySQL", StorageType.MYSQL.getName());
		assertEquals("SQLite", StorageType.SQLITE.getName());
	}

	@Test
	public void testFromString() {
		Optional<StorageType> yaml = StorageType.fromString("YAML");
		assertTrue(yaml.isPresent());
		assertEquals(StorageType.YAML, yaml.get());

		Optional<StorageType> json = StorageType.fromString("JSON");
		assertTrue(json.isPresent());
		assertEquals(StorageType.JSON, json.get());

		Optional<StorageType> mysql = StorageType.fromString("MySQL");
		assertTrue(mysql.isPresent());
		assertEquals(StorageType.MYSQL, mysql.get());

		Optional<StorageType> sqlite = StorageType.fromString("SQLite");
		assertTrue(sqlite.isPresent());
		assertEquals(StorageType.SQLITE, sqlite.get());

		Optional<StorageType> nonExistent = StorageType.fromString("NonExistentStorageType");
		assertFalse(nonExistent.isPresent());
	}
}
