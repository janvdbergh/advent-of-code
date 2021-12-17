#!/bin/zsh

YEAR=$(date +%Y)
DAY=$(date +%d)

mkdir -p advent-of-code-$YEAR/inputs
touch advent-of-code-$YEAR/inputs/input$DAY.txt
touch advent-of-code-$YEAR/inputs/input$DAY-test.txt

mkdir -p advent-of-code-$YEAR/src/main/kotlin/eu/janvdb/aoc$YEAR/day$DAY
touch advent-of-code-$YEAR/src/main/kotlin/eu/janvdb/aoc$YEAR/day$DAY/Day$DAY.kt

git add advent-of-code-$YEAR/inputs/input$DAY.txt
git add advent-of-code-$YEAR/inputs/input$DAY-test.txt
git add advent-of-code-$YEAR/src/main/kotlin/eu/janvdb/aoc$YEAR/day$DAY/Day$DAY.kt
