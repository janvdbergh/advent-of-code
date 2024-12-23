#!/bin/zsh

year=$(date +%Y)
day=$(date +%0d)
basedir=$(dirname "$0")/..

input_dir="$basedir/advent-of-code-$year/inputs"
input_file="$input_dir/input$day.txt"
input_file_test="$input_dir/input$day-test.txt"

if [ ! -f "$input_file" ]
then
  mkdir -p "$input_dir"
  touch "$input_file_test"
  session_cookie=$(cat "$basedir/util/session-cookie")
  curl https://adventofcode.com/$year/day/$day/input --cookie "session=$session_cookie" > "$input_file"
fi

source_dir="$basedir/advent-of-code-$year/src/main/kotlin/eu/janvdb/aoc$year/day$day/"
source_file="$source_dir/day$day.kt"
if [ ! -f $source_file ]
then
  mkdir -p "$source_dir"
  cat > "$source_file" << EOF
package eu.janvdb.aoc$year.day$day

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input$day-test.txt"
//const val FILENAME = "input$day.txt"

fun main() {
	val lines = readLines($year, FILENAME)
}
EOF
fi

git add "$source_file" "$input_file" "$input_file_test"
