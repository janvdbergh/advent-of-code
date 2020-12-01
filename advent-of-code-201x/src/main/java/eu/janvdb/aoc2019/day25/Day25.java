package eu.janvdb.aoc2019.day25;

import eu.janvdb.aoc2019.common.AsciiComputer;
import eu.janvdb.aoc2019.common.ProgramParser;

public class Day25 {

	/*
	 * #########################
	 * # S |   O   | M |   | L #
	 * #####---#####---#   #####
	 *     #   # ? #   #   #
	 *     #   #---#   # K #
	 *     # P # R #   #   #
	 *     #   #---# N #   #
	 *     #   | Q #   #   #
	 *     #############---#####
	 *     #   F   | G # I | J #
	 *     #---#########---#####
	 *     #     E     | A # D #
	 *     #---#########---#---#
	 *     # H #       # B | C #
	 *     #####       #########
	 *
	 * A: hull breach
	 * B: whiteboard (monolith)
	 * C: kitchen (asterisk)
	 * D: storage (photons)
	 * E: arcade (coin)
	 * F: navigation (giant eletromagnet)
	 * G: sick bay (astronaut ice cream)
	 * H: warp drive maintenance
	 * I: gift wrapping center (molten lava)
	 * J: observatory
	 * K: corridor (mutex)
	 * L: science lab (infinite loop)
	 * M: stables (astrolabe)
	 * N: hallway
	 * O: hot chocolate fountain (dehydrated water)
	 * P: holodeck (escape pod)
	 * Q: passages
	 * R: security checkpoint
	 * S: crew quarters (wreath)
	 *
	 * Required items for weight
	 * + wreath
	 * + astrolabe
	 * + asterisk
	 * + monolith
	 * - mutex
	 * - dehydrated water
	 * - coin
	 */

	public static void main(String[] args) throws InterruptedException {
		new Day25().run();
	}

	private void run() throws InterruptedException {
		long[] program = ProgramParser.parseProgram(Day25.class.getResource("program.txt"));
		AsciiComputer asciiComputer = new AsciiComputer(program, System.in, System.out);
		asciiComputer.start();
		asciiComputer.join();
	}
}
