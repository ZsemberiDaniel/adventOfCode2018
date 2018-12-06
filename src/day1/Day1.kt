package day1

import RunnablePuzzleSolver

class Day1: RunnablePuzzleSolver {
    private lateinit var changes: Array<Int>

    override fun readInput1(lines: Array<String>) {
        // split lines by spaces and map to ints
        changes = lines[0].split(" ").map { it.toInt() }.toTypedArray()
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        return changes.sum().toString()
    }

    override fun solvePart2(): String {
        val been = hashSetOf<Int>()
        var current = 0 // the current sub-sum
        var i = 0 // where we are in the changes array

        while (!been.contains(current)) {
            been.add(current)
            current += changes[i]

            // wrapping around
            i = if (i == changes.size - 1) {0} else {i + 1}
        }

        return current.toString()
    }

}
