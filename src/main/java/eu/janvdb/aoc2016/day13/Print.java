package eu.janvdb.aoc2016.day13;

public class Print {

	public static void main(String[] args) {
		System.out.println("  0123456789");
		for(int i=0; i<=9; i++) {
			System.out.print(i + " ");
			for(int j=0; j<=9; j++) {
				System.out.print(new Coord(i, j).isOpenSpace() ? "." : "#");
			}
			System.out.println();
		}
	}
}
