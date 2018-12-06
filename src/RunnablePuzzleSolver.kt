interface RunnablePuzzleSolver {
    fun readInput1(lines: Array<String>)
    fun readInput2(lines: Array<String>)

    fun solvePart1(): String
    fun solvePart2(): String
}