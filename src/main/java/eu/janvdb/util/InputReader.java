package eu.janvdb.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import javaslang.collection.Stream;

public class InputReader {

	public static Stream<String> readInput(File file) {
		try {
			return Stream.ofAll(FileUtils.readLines(file, "UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Stream<String> readInput(URL url) {
		return readInput(new File(url.getFile()));
	}
}
