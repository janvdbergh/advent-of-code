package eu.janvdb.aoc2020.day04

import eu.janvdb.aoc2020.util.groupLines
import eu.janvdb.aoc2020.util.readLines

val fieldRegex = Regex("(\\S+):(\\S+)")
val yearRegex = Regex("^\\d{4}$")
val heightRegex = Regex("^(\\d+)(cm|in)$")
val hairColorRegex = Regex("^#[0-9a-f]{6}$")
val eyeColorRegex = Regex("^amb|blu|brn|gry|grn|hzl|oth$")
val passportIdRegex = Regex("^\\d{9}$")

fun main() {
	val lines = readLines("input04.txt")
	val correctPassports = groupLines(lines, ::processPassport)
			.filter { it }
			.count()

	println(correctPassports)
}

fun processPassport(subLines: List<String>): Boolean {
	val fields = mutableMapOf<String, String>()
	subLines.flatMap(fieldRegex::findAll)
			.forEach {
				fields[it.groupValues[1]] = it.groupValues[2]
			}

	/*
	byr (Birth Year)
	iyr (Issue Year)
	eyr (Expiration Year)
	hgt (Height)
	hcl (Hair Color)
	ecl (Eye Color)
	pid (Passport ID)
	[ cid (Country ID) ]
	*/
	println(fields)
	val valid = validYear(fields["byr"], 1920, 2002) &&
			validYear(fields["iyr"], 2010, 2020) &&
			validYear(fields["eyr"], 2020, 2030) &&
			validHeight(fields["hgt"]) &&
			validHairColor(fields["hcl"]) &&
			validEyeColor(fields["ecl"]) &&
			validPassportId(fields["pid"]) /*&& fields.containsKey("cid")*/
	println(valid)
	println()
	return valid
}

fun validYear(value: String?, min: Int, max: Int): Boolean {
	if (value == null || !value.matches(yearRegex)) return false
	val yearValue = value.toInt()
	return yearValue >= min && yearValue <= max
}

fun validHeight(value: String?): Boolean {
	if (value == null) return false

	val matchResult = heightRegex.matchEntire(value)
	if (matchResult == null) return false

	val height = matchResult.groupValues[1].toInt()
	val unit = matchResult.groupValues[2]

	if (unit == "cm" && height >= 150 && height <= 193) return true
	if (unit == "in" && height >= 59 && height <= 76) return true
	return false
}

fun validHairColor(value: String?): Boolean {
	return value != null && value.matches(hairColorRegex)
}

fun validEyeColor(value: String?): Boolean {
	return value != null && value.matches(eyeColorRegex)
}

fun validPassportId(value: String?): Boolean {
	return value != null && value.matches(passportIdRegex)
}
