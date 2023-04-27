package me.colingrimes.midnight.storage.sql;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SchemaReader {

	@Nonnull
	public static List<String> getQueries(@Nonnull InputStream is) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String query = reader.lines().collect(Collectors.joining("\n"));
			return Arrays.stream(query.split(";"))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	private SchemaReader() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}