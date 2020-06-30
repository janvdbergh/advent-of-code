package eu.janvdb.aoc2018.day3;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import eu.janvdb.aoc2018.util.FileReader;
import eu.janvdb.aoc2018.util.Pair;

public class Day3 {

	public static void main(String[] args) throws IOException {
		List<Claim> claims = FileReader.readStringFile(Day3.class, "day3.txt").stream()
				.map(Claim::read)
				.collect(Collectors.toList());

		int minX = claims.stream().mapToInt(Claim::getMinX).min().orElse(0);
		int minY = claims.stream().mapToInt(Claim::getMinY).min().orElse(0);
		int maxY = claims.stream().mapToInt(Claim::getMaxY).max().orElse(0);
		int maxX = claims.stream().mapToInt(Claim::getMaxX).max().orElse(0);

		long doubles = IntStream.range(minX, maxX + 1)
				.boxed()
				.flatMap(x -> IntStream.range(minY, maxY + 1).boxed().map(y -> new Pair<>(x, y)))
				.filter(xy -> hasMultipleClaims(claims, xy._1(), xy._2()))
				.count();
		System.out.println(doubles);

		claims.stream()
				.filter(claim -> doesNotOverLapWithClaims(claims, claim))
				.map(Claim::getId)
				.forEach(System.out::println);
	}

	private static boolean hasMultipleClaims(Collection<Claim> claims, int x, int y) {
		return claims.stream()
				.filter(claim -> claim.contains(x, y))
				.count() > 1;
	}

	private static boolean doesNotOverLapWithClaims(List<Claim> claims, Claim claim) {
		return claims.stream()
				.filter(otherClaim -> claim.getId() != otherClaim.getId())
				.noneMatch(claim::overlapsWith);
	}
}

