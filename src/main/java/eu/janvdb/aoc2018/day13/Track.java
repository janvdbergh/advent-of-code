package eu.janvdb.aoc2018.day13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class Track {

	private final List<String> tracks;
	private final Set<Cart> carts;

	Track(List<String> tracks) {
		this.carts = new HashSet<>();
		this.tracks = new ArrayList<>();

		tracks.forEach(this::handleTrackLine);
	}

	private void handleTrackLine(String trackLine) {
		int y = tracks.size();

		for (int x = 0; x < trackLine.length(); x++) {
			char ch = trackLine.charAt(x);
			if (ch == '>' || ch == '<' || ch == '^' || ch == 'v') {
				carts.add(new Cart(x, y, Direction.fromChar(ch)));
			}
		}

		this.tracks.add(trackLine.replaceAll("[<>]", "-").replaceAll("[v^]", "|"));
	}

	void move() {
		List<Cart> cartsToMove = new ArrayList<>(carts);
		cartsToMove.sort(Comparator.comparingInt(Cart::getX).thenComparingInt(Cart::getY));

		Set<Cart> cartsToRemove = new HashSet<>();
		cartsToMove.stream()
				.filter(cart -> !cartsToRemove.contains(cart))
				.forEach(cart -> {
					try {
						cart.move(this);
					} catch (CollisionException e) {
						streamCartsAt(e.getX(), e.getY()).forEach(cartsToRemove::add);
					}
				});

		carts.removeAll(cartsToRemove);
	}

	boolean isIntersection(int x, int y) {
		return getChar(x, y) == '+';
	}

	boolean containsMultipleCarts(int x, int y) {
		return streamCartsAt(x, y).count() > 1;
	}

	private Stream<Cart> streamCartsAt(int x, int y) {
		return carts.stream()
				.filter(cart -> cart.getX() == x && cart.getY() == y);
	}

	boolean isCornerTopLeftOrDownRight(int x, int y) {
		return getChar(x, y) == '/';
	}

	boolean isCornerTopRightOrDownLeft(int x, int y) {
		return getChar(x, y) == '\\';
	}

	private char getChar(int x, int y) {
		return tracks.get(y).charAt(x);
	}

	Set<Cart> getCarts() {
		return carts;
	}
}
