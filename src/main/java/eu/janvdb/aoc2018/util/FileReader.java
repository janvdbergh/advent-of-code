package eu.janvdb.aoc2018.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class FileReader {

	public static List<String> readStringFile(Class<?> clazz, String name) throws IOException {
		InputStream inputStream = clazz.getResource(name).openStream();
		return IOUtils.readLines(inputStream, Charset.defaultCharset());
	}

	public static List<Integer> readIntFile(Class<?> clazz, String name) throws IOException {
		return readStringFile(clazz, name).stream()
				.flatMap(s -> Arrays.stream(s.split("\\s+")))
				.filter(StringUtils::isNotBlank)
				.map(Integer::parseInt)
				.collect(Collectors.toList());
	}
}
