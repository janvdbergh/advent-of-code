package eu.janvdb.aoc2017.day8;

import io.vavr.collection.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Instruction {

	private static final Pattern PATTERN = Pattern.compile("(\\w+) (inc|dec) (-?\\d+) if (\\w+) (>|>=|<|<=|==|!=) (-?\\d+)");

	private final String updateRegister;
	private final String updateOperator;
	private final int updateValue;
	private final String conditionRegister;
	private final String conditionOperator;
	private final int conditionValue;

	public Instruction(String command) {
		Matcher matcher = PATTERN.matcher(command);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(command);
		}

		this.updateRegister = matcher.group(1);
		this.updateOperator = matcher.group(2);
		this.updateValue = Integer.parseInt(matcher.group(3));

		this.conditionRegister = matcher.group(4);
		this.conditionOperator = matcher.group(5);
		this.conditionValue = Integer.parseInt(matcher.group(6));
	}

	public Map<String, Integer> execute(Map<String, Integer> registers) {
		if (evaluateCondition(registers)) {
			return evaluateExpression(registers);
		}
		return registers;
	}

	private boolean evaluateCondition(Map<String, Integer> registers) {
		int conditionRegisterValue = getRegisterValue(registers, conditionRegister);
		switch (conditionOperator) {
			case ">":
				return conditionRegisterValue > conditionValue;
			case ">=":
				return conditionRegisterValue >= conditionValue;
			case "<":
				return conditionRegisterValue < conditionValue;
			case "<=":
				return conditionRegisterValue <= conditionValue;
			case "==":
				return conditionRegisterValue == conditionValue;
			case "!=":
				return conditionRegisterValue != conditionValue;
			default:
				throw new IllegalArgumentException(conditionOperator);
		}
	}

	private Map<String, Integer> evaluateExpression(Map<String, Integer> registers) {
		int updateRegisterValue = getRegisterValue(registers, updateRegister);
		switch (updateOperator) {
			case "dec":
				return registers.put(updateRegister, updateRegisterValue - updateValue);
			case "inc":
				return registers.put(updateRegister, updateRegisterValue + updateValue);
			default:
				throw new IllegalArgumentException(updateOperator);
		}
	}

	private int getRegisterValue(Map<String, Integer> registers, String name) {
		return registers.get(name).getOrElse(0);
	}

}
