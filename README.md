# Advent of code 2018

This is my repository for advent of code 2018.

### Project structure
Each day gets it's own folder with the Kotlin file, the input and a README that discusses
what kind of challenges I faced each day.

### Tester
The Main.kt is a tester class, that receives the current day as a parameter and then provides input
for that day before running the part which solves the exercise.

For that a bit of reflection is used and each day's package requires a DayN.kt class, which
derives from the RunnablePuzzleSolver.kt. Next to that either an
input.txt for both parts or an input1.txt and and input2.txt for the first and the second case.

| Day # | Problem | Solution |
|:-----:|:-------:|:--------:|
| 1 | [problem](https://adventofcode.com/2018/day/1) | [solution](src/day1/README.md) |
| 2 | [problem](https://adventofcode.com/2018/day/2) | [solution](src/day2/README.md) |
| 3 | [problem](https://adventofcode.com/2018/day/3) | [solution](src/day3/README.md) |
| 4 | [problem](https://adventofcode.com/2018/day/4) | [solution](src/day4/README.md) |
| 5 | [problem](https://adventofcode.com/2018/day/5) | [solution](src/day5/README.md) |
| 6 | [problem](https://adventofcode.com/2018/day/6) | [solution](src/day6/README.md) |
| 7 | [problem](https://adventofcode.com/2018/day/7) | [solution](src/day7/README.md) |
| 8 | [problem](https://adventofcode.com/2018/day/8) | [solution](src/day8/README.md) |
| 9 | [problem](https://adventofcode.com/2018/day/9) | [solution](src/day9/README.md) |
| 10 | [problem](https://adventofcode.com/2018/day/10) | [solution](src/day10/README.md) |
| 11 | [problem](https://adventofcode.com/2018/day/11) | [solution](src/day11/README.md) |
| 12 | [problem](https://adventofcode.com/2018/day/12) | [solution](src/day12/README.md) |
