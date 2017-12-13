package eu.janvdb.aoc2015.day19;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final String[] INPUT0 = {
			"e => H",
			"e => O",
			"H => HO",
			"H => OH",
			"O => HH"
	};
	private static final String RESULT0 = "e";
	private static final String START0 = "HOHOHO";

	private static final String[] INPUT1 = {
			"A => ZF",
			"A => Z(F)",
			"B => BX",
			"B => TB",
			"B => T(F)",
			"X => XX",
			"X => PB",
			"X => P(F)",
			"X => S(F,F)",
			"X => S(M)",
			"X => SZ",
			"F => XF",
			"F => PM",
			"F => SA",
			"H => C(A)",
			"H => C(F,F,F)",
			"H => C(F,M)",
			"H => C(M,F)",
			"H => HX",
			"H => N(F,F)",
			"H => N(M)",
			"H => NZ",
			"H => OB",
			"H => O(F)",
			"M => BF",
			"M => TM",
			"N => C(F)",
			"N => HS",
			"O => C(F,F)",
			"O => C(M)",
			"O => HP",
			"O => N(F)",
			"O => OT",
			"P => XP",
			"P => PT",
			"P => S(F)",
			"S => XS",
			"Z => ZX",
			"T => BP",
			"T => TT",
			"e => HF",
			"e => NA",
			"e => OM",
	};

	private static final String RESULT1 = "e";
	private static final String START1 = "O(PBPM)XXXSZXXSZXXPBS(F)(F)XXSZXXSZXXXXXXS(F,F)S(M)XS(PTTBF,PBF)S(XS(T(F)SA)PTBPT(XSA)XPTTBPM,F)PT(F)S(XXF)(XF)XS(S(M)F,XS(M)XXSZP(F)PBXS(M)XXSZXS(TM)F)SZSZXXS(M)XXS(F)TBPT(XSA)XPT(F)PBPBXXSZXPBSZP(F)SZXSZXSZXPTBS(F,F)XXP(F)PBXXPBS(T(F)XP(F)S(XXXSZX(XF),XS(F)BXXXSZF)PBF)XS(F)(XXXF)S(F)T(PM)F";

	private static final String[] INPUT = INPUT1;
	public static final String START = START1;
	private static final String RESULT = RESULT1;
	private static final int NO_RESULT = 20000;

	private static List<Formula> formulas;

	public static void main(String[] args) {
		formulas = Stream.of(INPUT)
				.map(Formula::new)
				.sorted()
				.toList();

		int steps = reduce(START);
		System.out.println(steps);
	}

	private static int reduce(String start) {
		if (RESULT.equals(start)) {
			return 0;
		}

		for (Formula formula : formulas) {
			int stringIndex = -1;

			while ((stringIndex = start.indexOf(formula.getFrom(), stringIndex + 1)) != -1) {
				String next = start.substring(0, stringIndex) + formula.getTo() + start.substring(stringIndex + formula.getFrom().length());
				if (next.length() > 1 && next.contains(RESULT)) {
					continue;
				}

				int temp = reduce(next);
				if (temp != NO_RESULT) {
					return temp + 1;
				}
			}
		}

		return NO_RESULT;
	}


	private static class Formula implements Comparable<Formula> {
		private static final Pattern PATTERN = Pattern.compile("(\\S+) => (\\S+)");

		private final String from;
		private final String to;

		private Formula(String description) {
			Matcher matcher = PATTERN.matcher(description);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(description);
			}

			this.from = matcher.group(2);
			this.to = matcher.group(1);
		}

		String getFrom() {
			return from;
		}

		String getTo() {
			return to;
		}

		@Override
		public int compareTo(Formula o) {
			return o.from.length() - from.length();
		}
	}
}
