package eu.janvdb.aoc2017.day25;

public class Puzzle {

	public static void main(String[] args) {
		runWithStates(6, new StateExampleA(), new StateExampleB());

		runWithStates(12425180, new StateA(), new StateB(), new StateC(), new StateD(), new StateE(), new StateF());
	}

	private static void runWithStates(int numberOfIterations, State... states) {
		TuringMachine turingMachine = new TuringMachine(states);
		turingMachine.executeStates(numberOfIterations, states[0].getName());

		System.out.println(turingMachine);
		System.out.println(turingMachine.countOnes());
	}
}
