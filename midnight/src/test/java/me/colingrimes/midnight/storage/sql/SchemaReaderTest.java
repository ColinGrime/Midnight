package me.colingrimes.midnight.storage.sql;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchemaReaderTest {

	@Test
	void testGetQueries() {
		String input = "CREATE TABLE test1 (id INTEGER);\nCREATE TABLE test2 (name TEXT);";
		try (InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))) {
			List<String> queries = SchemaReader.getQueries(is);

			assertEquals(2, queries.size(), "Expected two queries in the list");
			assertEquals("CREATE TABLE test1 (id INTEGER)", queries.get(0), "Expected first query to match");
			assertEquals("CREATE TABLE test2 (name TEXT)", queries.get(1), "Expected second query to match");
		} catch (IOException e) {
			fail("IOException should not be thrown");
		}
	}
}
