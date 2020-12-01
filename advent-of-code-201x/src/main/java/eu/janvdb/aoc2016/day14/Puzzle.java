package eu.janvdb.aoc2016.day14;

import io.vavr.Tuple3;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final String INPUT0 = "abc";
	private static final String INPUT1 = "yjdafjpo";
	private static final String INPUT = INPUT1;
	private static final Pattern PATTERN3 = Pattern.compile("(\\w)\\1\\1");

	private MessageDigest md5;

	private Map<Integer, String> hashes = HashMap.empty();

	public static void main(String[] args) throws Exception {
		new Puzzle().execute();
	}

	private Puzzle() throws Exception {
		md5 = MessageDigest.getInstance("MD5");
	}

	private void execute() {
		int hashesFound = 0;
		int position = 0;

		while (hashesFound < 64) {
			Tuple3<Integer, String, Matcher> hash = findHashMatching(position, Integer.MAX_VALUE, PATTERN3).get();
			position = hash._1 + 1;

			Option<Tuple3<Integer, String, Matcher>> hash2 = findHashMatching(
					position,
					position + 1000,
					Pattern.compile(hash._3.group(1) + "{5}"));
			if (hash2.isDefined()) {
				hashesFound++;
			}
		}

		System.out.println(position - 1);
	}

	private Option<Tuple3<Integer, String, Matcher>> findHashMatching(int startIndex, int endIndex, Pattern pattern) {
		for (int i = startIndex; i < endIndex; i++) {
			String hash = getHash(i);
			Matcher matcher = pattern.matcher(hash);
			if (matcher.find()) {
				return Option.of(new Tuple3<>(i, hash, matcher));
			}
		}

		return Option.none();
	}

	private String getHash(int offset) {
		String hash = hashes.get(offset).getOrElse(() -> calculateHash(offset));
		hashes = hashes.put(offset, hash);
		return hash;
	}

	private String calculateHash(int offset) {
		String toHash = INPUT + offset;
		byte[] digest = md5.digest(toHash.getBytes());
		return stretchHash(Hex.encodeHexString(digest));
	}

	private String stretchHash(String hash) {
		for (int i = 0; i < 2016; i++) {
			hash = Hex.encodeHexString(md5.digest(hash.getBytes()));
		}

		return hash;
	}
}
