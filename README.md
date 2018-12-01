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
