package eu.janvdb.aocutil.java;

import io.vavr.collection.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class InputReader {

	public static Stream<String> readInput(File file) {
		try {
			return Stream.ofAll(FileUtils.readLines(file, "UTF-8")).filter(StringUtils::isNotBlank);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Stream<String> readInput(URL url) {
		return readInput(new File(url.getFile()));
	}

	private static String readInputFully(File file) {
		try {
			return FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String readInputFully(URL url) {
		File file = new File(url.getFile());
		return readInputFully(file);
	}
}
