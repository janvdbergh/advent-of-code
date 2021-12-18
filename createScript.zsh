#!/bin/zsh

cd ${0:a:h}

YEAR=$(date +%Y)
DAY=$(date +%d)

INPUT_DIR=advent-of-code-$YEAR/inputs
INPUT_TEST=$INPUT_DIR/input$DAY-test.txt
INPUT=$INPUT_DIR/input$DAY.txt
CODE_DIR=advent-of-code-$YEAR/src/main/kotlin/eu/janvdb/aoc$YEAR/day$DAY
CODE=$CODE_DIR/Day$DAY.kt

mkdir -p $INPUT_DIR
touch $INPUT_TEST
touch $INPUT

mkdir -p $CODE_DIR
if [ ! -f $CODE ]
then
  echo "package eu.janvdb.aoc$YEAR.day$DAY" > $CODE
fi

git add $INPUT_TEST $INPUT $CODE
