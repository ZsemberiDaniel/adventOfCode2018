package day12

import RunnablePuzzleSolver
import java.lang.StringBuilder

class Day12 : RunnablePuzzleSolver {
    lateinit var initialState: CharArray
    lateinit var transitions: Map<String, Char>

    override fun readInput1(lines: Array<String>) {
        initialState = lines[0].substring(15).toCharArray()

        val transitionPairs = lines.sliceArray(2 until lines.size).map {
            val line = it.split(" => ")

            line[0] to line[1][0]
        }.filter { it.first[2] != it.second }.toTypedArray()
        transitions = mapOf(*transitionPairs)
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        return getSumsForGenerationsUntil(20)[19].toString()
    }

    override fun solvePart2(): String {
        val generationCount = 50000000000L
        val sums = getSumsForGenerationsUntil(200)
        val differences = (1 until sums.size).map { sums[it] - sums[it - 1] }

        // we go through the differences searching for a starting point from which point on they repeat
        var firstSameDiff: Int? = null
        for (i in 0 until differences.size - 3) {
            if (differences[i] == differences[i + 1] && differences[i + 1] == differences[i + 2]) {
                firstSameDiff = i
                break
            }
        }

        if (firstSameDiff == null) {
            return "Couldn't find the sum. Maybe increase the generation count!"
        }

        // we add the numbers till the repetition and then repeat the difference x times till we get to the last
        // generation we need
        return (sums[firstSameDiff] + differences[firstSameDiff + 1] * (generationCount - firstSameDiff)).toString()
    }

    private fun getSumsForGenerationsUntil(lastGeneration: Int): Array<Int> {
        val generationSums = Array(lastGeneration + 1) { 0 }

        val plantsArraySize = 2000

        // the 0th plant is plantsArraySize / 2
        val plants = CharArray(plantsArraySize) { '.' }
        var firstPlant = plantsArraySize / 2
        var lastPlant = firstPlant + initialState.size - 1

        // we copy the initial state
        for (i in 0 until initialState.size)
            plants[plantsArraySize / 2 + i] = initialState[i]

        // what plants to 'flip' (.->#,#->.) after each generation
        val flipPlants = mutableListOf<Int>()
        for (generationAt in 1..lastGeneration) {
            var newFirstPlant = firstPlant
            var newLastPlant = lastPlant

            // stores the string of the current plant's 2 neighbours
            val currentString = StringBuilder()
            for (i in firstPlant - 5 until firstPlant) // we init it with th string before the first plant
                currentString.append(plants[i])

            // we check for each plant whether it can transition to something else
            for (plantId in firstPlant - 2..lastPlant + 2) {
                // we update the string
                currentString.deleteCharAt(0)
                currentString.append(plants[plantId + 2])

                // this plant flips it's state
                val transition = transitions[currentString.toString()]
                if (transition != null) {
                    if (plantId < newFirstPlant) newFirstPlant = plantId
                    if (plantId > newLastPlant)  newLastPlant = plantId

                    flipPlants.add(plantId)
                }
            }

            // updating where the first and last plants are
            firstPlant = newFirstPlant
            lastPlant = newLastPlant

            // we flip the plants we need to flip
            for (plantId in flipPlants) {
                plants[plantId] = if (plants[plantId] == '.') { '#' } else { '.' }

                // we can't calculate sum based on previous generation if it is the first generation
                if (generationAt != 1) {
                    // we subtract if we flipped to .
                    generationSums[generationAt] += if (plants[plantId] == '.') {
                        - plantId + plantsArraySize / 2
                    } else {  // we add if we flipped to #
                        plantId - plantsArraySize / 2
                    }
                }
            }
            flipPlants.clear()

            // we need to calculate it by summing in the first generation
            if (generationAt == 1)
                generationSums[generationAt] = (firstPlant..lastPlant).filter { plants[it] == '#' }.map { it - plantsArraySize / 2 }.sum()
            else  // we can rely on previous generations in generations 2 and above
                generationSums[generationAt] += generationSums[generationAt - 1]
        }

        return generationSums
    }
}


