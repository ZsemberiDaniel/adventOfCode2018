package day5

import RunnablePuzzleSolver
import java.lang.StringBuilder

class Day5 : RunnablePuzzleSolver {
    lateinit var polymer: String

    override fun readInput1(lines: Array<String>) {
        polymer = lines[0]
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        val polymer1 = StringBuilder()
        for (i in 0 until polymer.length) {
            // the reacted polymer is empty -> other checks will fail in the if
            if (polymer1.isEmpty()) {
                polymer1.append(polymer[i])
            } else {
                // found a character that clashes with the one at the end of the final polymer
                if (polymer1[polymer1.length - 1] != polymer[i] &&
                        polymer1[polymer1.length - 1].toLowerCase() == polymer[i].toLowerCase()) {
                    // remove the last one and don't add the current
                    polymer1.deleteCharAt(polymer1.length - 1)
                } else {
                    // no clash -> add the current
                    polymer1.append(polymer[i])
                }
            }
        }

        return polymer1.length.toString()
    }

    override fun solvePart2(): String {
        var k = 0
        val been = HashSet<Char>()

        var solution = Pair(Int.MAX_VALUE, '-')

        // we go through the whole string or until we went through the whole abc
        while (k < polymer.length && been.size < 26) {
            // we check the letters that have not been
            if (!been.contains(polymer[k].toLowerCase())) {
                been.add(polymer[k].toLowerCase()) // add current letter to been

                val polymer1 = StringBuilder()
                for (i in 0 until polymer.length) {
                    // we skip the letters that are omitted
                    if (polymer[i].toLowerCase() == polymer[k].toLowerCase())
                        continue

                    // the reacted polymer is empty -> other checks will fail in the if
                    if (polymer1.isEmpty()) {
                        polymer1.append(polymer[i])
                    } else {
                        // found a character that clashes with the one at the end of the final polymer
                        if (polymer1[polymer1.length - 1] != polymer[i] &&
                                polymer1[polymer1.length - 1].toLowerCase() == polymer[i].toLowerCase()) {
                            // remove the last one and don't add the current
                            polymer1.deleteCharAt(polymer1.length - 1)
                        } else {
                            // no clash -> add the current
                            polymer1.append(polymer[i])
                        }
                    }
                }

                // we look for the letter with the minimal length polymer
                if (polymer1.length < solution.first)
                    solution = Pair(polymer1.length, polymer[k])
            }

            k++
        }

        return solution.first.toString()
    }

}