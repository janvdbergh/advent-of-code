package eu.janvdb.aoc2017.day18;

@FunctionalInterface
public interface Instruction {
	void execute(Machine machine) throws DeadLockedException;
}
