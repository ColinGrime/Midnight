package me.colingrimes.midnight.storage;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StorageTypeTest {

	@Test
	public void testGetName() {
		assertEquals("YAML", StorageType.YAML.getName(), "StorageType YAML should have the name 'YAML'");
		assertEquals("JSON", StorageType.JSON.getName(), "StorageType JSON should have the name 'JSON'");
		assertEquals("MySQL", StorageType.MYSQL.getName(), "StorageType MYSQL should have the name 'MySQL'");
		assertEquals("SQLite", StorageType.SQLITE.getName(), "StorageType SQLITE should have the name 'SQLite'");
	}

	@Test
	public void testFromString() {
		Optional<StorageType> yamlOpt = StorageType.fromString("YAML");
		assertTrue(yamlOpt.isPresent(), "Optional should not be empty");
		assertEquals(StorageType.YAML, yamlOpt.get(), "fromString('YAML') should return StorageType.YAML");

		Optional<StorageType> jsonOpt = StorageType.fromString("JSON");
		assertTrue(jsonOpt.isPresent(), "Optional should not be empty");
		assertEquals(StorageType.JSON, jsonOpt.get(), "fromString('JSON') should return StorageType.JSON");

		Optional<StorageType> mysqlOpt = StorageType.fromString("MySQL");
		assertTrue(mysqlOpt.isPresent(), "Optional should not be empty");
		assertEquals(StorageType.MYSQL, mysqlOpt.get(), "fromString('MySQL') should return StorageType.MYSQL");

		Optional<StorageType> sqliteOpt = StorageType.fromString("SQLite");
		assertTrue(sqliteOpt.isPresent(), "Optional should not be empty");
		assertEquals(StorageType.SQLITE, sqliteOpt.get(), "fromString('SQLite') should return StorageType.SQLITE");
	}

	@Test
	public void testFromStringNotFound() {
		Optional<StorageType> notFoundOpt = StorageType.fromString("NonExistentStorageType");
		assertFalse(notFoundOpt.isPresent(), "Optional should be empty for a non-existent storage type");
	}
}
