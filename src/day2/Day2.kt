package day2

import RunnablePuzzleSolver

class Day2 : RunnablePuzzleSolver {
    lateinit var ids: Array<String>

    override fun readInput1(line: Array<String>) {
        ids = line
    }

    override fun readInput2(line: Array<String>) { }

    override fun solvePart1(): String {
        var twiceCount = 0
        var threeTimesCount = 0

        for (id in ids) {
            // contains how many times unique letters appear (that appear at least twice) in an id
            val grouped = id.toCharArray().groupBy { it }.filter { it.value.size >= 2 }.map { it.value.size }

            if (2 in grouped)
                twiceCount++
            if (3 in grouped)
                threeTimesCount++
        }

        return (twiceCount * threeTimesCount).toString()
    }

    override fun solvePart2(): String {
        for (i in 0 until ids.size) {
            for (k in i + 1 until ids.size) {
                // the similar characters in the ith and kth word
                val similar = ids[i].filterIndexed { index, c -> c == ids[k][index] }

                // found the solution
                if (similar.length == ids[i].length - 1) {
                    return similar
                }
            }
        }

        return "None found with difference 1"
    }

}