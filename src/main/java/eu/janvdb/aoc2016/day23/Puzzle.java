package eu.janvdb.aoc2016.day23;

import io.vavr.collection.Array;
import io.vavr.collection.Stream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class Puzzle {

	private static final String FILE_NAME = "codeOptimized.txt";

	public static void main(String[] args) throws IOException {
		new Puzzle().execute();
	}

	private void execute() throws IOException {
		Array<Instruction> program = Stream.ofAll(IOUtils.readLines(getClass().getResourceAsStream(FILE_NAME), "UTF-8"))
				.map(InstructionFactory::assembleInstruction)
				.toArray();
		State state = new State(program).withRegister(0, 12);

//		System.out.println(state);
//		System.out.println();

		while (state.hasInstruction()) {
//			System.out.println(state.getCurrentInstruction());
			state = state.getCurrentInstruction().execute(state);
//			System.out.println(state);
//			System.out.println();
		}

		System.out.println(state);
	}


}
