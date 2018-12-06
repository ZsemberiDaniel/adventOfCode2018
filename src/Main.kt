import java.io.File
import kotlin.system.measureTimeMillis

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isEmpty()) {
                error("You need to provide the day count as first parameter!")
            }

            val day: Int = args[0].toInt()

            // this class will be used for the calculations
            val puzzleSolver = Class.forName("day$day.Day$day").newInstance() as RunnablePuzzleSolver

            // reading the input
            val oneInput = File("./src/day$day/input.txt")
            var inputOneString: Array<String>? = null
            var inputTwoString: Array<String>? = null

            val inputMillis = measureTimeMillis {
                // we have only one input file for the challenge (no separate)
                if (oneInput.exists()) {
                    inputOneString = oneInput.readLines().toTypedArray()
                    inputTwoString = inputOneString
                } else { // we have separate input files
                    val inputOne = File("./src/day$day/input1.txt")
                    val inputTwo = File("./src/day$day/input2.txt")

                    if (inputOne.exists()) {
                        inputOneString = inputOne.readLines().toTypedArray()
                    }

                    if (inputTwo.exists()) {
                        inputTwoString = inputTwo.readLines().toTypedArray()
                    }
                }
            }
            println("Input took $inputMillis milliseconds.")

            if (inputOneString == null || inputTwoString == null) {
                error("The input could not be found for day $day! " +
                        "${if (inputOneString == null) {"First input had a problem!"} else {""}}  " +
                        if (inputTwoString == null) {"Second input had a problem!"} else {""})
            }

            val millis1 = measureTimeMillis {
                // solving the puzzles
                puzzleSolver.readInput1(inputOneString!!)
                println("Part 1:\n${puzzleSolver.solvePart1()}")
            }
            println("Part 1 took $millis1 milliseconds.")

            val millis2 = measureTimeMillis {
                puzzleSolver.readInput2(inputTwoString!!)
                println("Part 2:\n${puzzleSolver.solvePart2()}")
            }

            println("Part 2 took $millis2 milliseconds.")
        }
    }
}
