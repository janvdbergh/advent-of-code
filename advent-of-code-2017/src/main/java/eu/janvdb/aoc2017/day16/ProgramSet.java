package eu.janvdb.aoc2017.day16;

class ProgramSet {

	private final char[] programs;

	ProgramSet(int size) {
		this.programs = new char[size];
		for (int i = 0; i < size; i++) {
			programs[i] = (char) ('a' + i);
		}
	}

	private ProgramSet(char[] programs) {
		this.programs = programs;
	}

	ProgramSet spin(int index) {
		char[] newPrograms = new char[programs.length];
		System.arraycopy(programs, programs.length - index, newPrograms, 0, index);
		System.arraycopy(programs, 0, newPrograms, index, programs.length-index);

		return new ProgramSet(newPrograms);
	}

	ProgramSet exchange(int index1, int index2) {
		char[] newPrograms = programs.clone();
		newPrograms[index1] = programs[index2];
		newPrograms[index2] = programs[index1];
		return new ProgramSet(newPrograms);
	}

	ProgramSet partner(char program1, char program2) {
		return exchange(findIndexOf(program1), findIndexOf(program2));
	}

	private int findIndexOf(char program) {
		for(int i=0; i<programs.length; i++) {
			if (programs[i] == program) return i;
		}
		throw new IllegalArgumentException(String.valueOf(program));
	}

	String getProgramList() {
		return new String(programs);
	}
}
