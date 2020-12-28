package eu.janvdb.aocutil.java;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {

	public static List<String> readStringFile(Class<?> clazz, String name) {
		try {
			InputStream inputStream = clazz.getResource(name).openStream();
			return IOUtils.readLines(inputStream, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Integer> readIntFile(Class<?> clazz, String name) {
		return readStringFile(clazz, name).stream()
				.flatMap(s -> Arrays.stream(s.split("\\s+")))
				.filter(StringUtils::isNotBlank)
				.map(Integer::parseInt)
				.collect(Collectors.toList());
	}

	public static List<List<String>> groupLines(List<String> lines) {
		List<List<String>> result = new ArrayList<>();

		List<String> subLines = new ArrayList<>();
		for (String line : lines) {
			if (line.isBlank()) {
				if (!subLines.isEmpty()) result.add(subLines);
				subLines = new ArrayList<>();
			} else {
				subLines.add(line);
			}
		}
		if (!subLines.isEmpty()) result.add(subLines);

		return result;
	}
}
