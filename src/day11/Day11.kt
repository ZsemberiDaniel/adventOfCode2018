package day11

import RunnablePuzzleSolver
import java.lang.Integer.min

class Day11 : RunnablePuzzleSolver {
    val gridSerialNumber = 7347

    val cells = Array(301) { Array(301) { 0 } }
    val cellSums = Array(301) { Array(301) { 0 } }

    override fun readInput1(lines: Array<String>) { }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        for (y in 1 until cells.size) {
            for (x in 1 until cells[y].size) {
                // Find the fuel cell's rack ID, which is its X coordinate plus 10
                val rackId = x + 10

                // Begin with a power level of the rack ID times the Y coordinate
                var powerLevel = rackId * y

                // Increase the power level by the value of the grid serial number (your puzzle input)
                powerLevel += gridSerialNumber

                // Set the power level to itself multiplied by the rack ID
                powerLevel *= rackId

                // Keep only the hundreds digit of the power level
                powerLevel %= 1000
                powerLevel -= powerLevel % 100
                powerLevel /= 100

                // Subtract 5 from the power level
                powerLevel -= 5

                cells[y][x] = powerLevel

                // calculate the sums till this cell
                cellSums[y][x] = cells[y][x] + cellSums[y - 1][x] + cellSums[y][x - 1] - cellSums[y - 1][x - 1]

            }
        }

        var whereMax = Pair(0, 0)
        var max = Int.MIN_VALUE
        for (y in 1 until cells.size - 2) {
            for (x in 1 until cells.size - 2) {
                val sum = cellSums[y + 2][x + 2] - cellSums[y - 1][x + 2] - cellSums[y + 2][x - 1] + cellSums[y - 1][x - 1]

                if (max < sum) {
                    max = sum
                    whereMax = Pair(x, y)
                }
            }
        }

        return whereMax.toString()
    }

    override fun solvePart2(): String {
        var max = Int.MIN_VALUE
        var solution = Triple(0, 0, 0)
        for (y in 1 until cells.size) {
            for (x in 1 until cells[y].size) {
                val maxSize = min(cells.size - y, cells[y].size - x)

                for (size in 0 until maxSize) {
                    val sum = cellSums[y + size][x + size] - cellSums[y - 1][x + size] - cellSums[y + size][x - 1] + cellSums[y - 1][x - 1]

                    if (max < sum) {
                        max = sum
                        solution = Triple(x, y, size + 1)
                    }
                }
            }
        }

        return solution.toString()
    }

}
