interface RunnablePuzzleSolver {
    fun readInput1(line: Array<String>)
    fun readInput2(line: Array<String>)

    fun solvePart1(): String
    fun solvePart2(): String
}