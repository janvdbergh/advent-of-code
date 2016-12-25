package eu.janvdb.aoc2016.day25;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import javaslang.collection.Array;
import javaslang.collection.Stream;

public class Puzzle {

	private static final String FILE_NAME = "code.txt";

	public static void main(String[] args) throws IOException {
		new Puzzle().execute();
	}

	private void execute() throws IOException {
		Array<Instruction> program = Stream.ofAll(IOUtils.readLines(getClass().getResourceAsStream(FILE_NAME), "UTF-8"))
				.map(InstructionFactory::assembleInstruction)
				.toArray();

		for (int i=0; i<1000; i++) {
			System.out.print(i + ": ");

			State state = new State(program).withRegister(0, i);
			while (state.hasInstruction()) {
				state = state.getCurrentInstruction().execute(state);
			}
			System.out.println();
		}
	}


}
