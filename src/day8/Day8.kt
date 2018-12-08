package day8

import RunnablePuzzleSolver

class Day8 : RunnablePuzzleSolver {
    private lateinit var rootNode: Node

    override fun readInput1(lines: Array<String>) {
        rootNode = getNodeStartingFrom(lines[0].split(" ").map { it.toInt() }.toTypedArray(), 0).first
    }

    private fun getNodeStartingFrom(numbers: Array<Int>, at: Int): Pair<Node, Int> {
        val currNode = Node(numbers[at], numbers[at + 1])
        // this will store how long this node is in the input array
        var currLength = 2

        // we go through the children and get them from the input array
        for (i in 0 until currNode.childCount) {
            // the child should be at   'at + currLength' because we always add the child's length to the current length
            val childNodeWithLength = getNodeStartingFrom(numbers, at + currLength)

            currLength += childNodeWithLength.second
            currNode.children[i] = childNodeWithLength.first
        }

        // we also need to get the metadata
        for (i in 0 until currNode.metadataCount) {
            currNode.metadata[i] = numbers[at + currLength + i]
        }
        currLength += currNode.metadataCount

        // we return the node itself and how long it is in the input array
        return Pair(currNode, currLength)
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        return addMetadata(rootNode).toString()
    }

    private fun addMetadata(currNode: Node): Int {
        // we store the sum for part 2 so if it is null we need to calculate it
        if (currNode.normalSum == null) {
            // simply add metadata sum
            currNode.normalSum = currNode.metadata.sumBy { it ?: 0 }

            // and sum of all child metadata
            for (child in currNode.children) {
                currNode.normalSum = currNode.normalSum!! + addMetadata(child!!)
            }
        }

        // this cannot be null at this point
        return currNode.normalSum!!
    }

    override fun solvePart2(): String {
        return addMetadataComplicated(rootNode).toString()
    }

    private fun addMetadataComplicated(currNode: Node): Int {
        // we need to store the complicated sum because it may have to be calculated more than once for a single node
        if (currNode.complicatedSum == null) {
            // just like how it said in the exercise
            if (currNode.childCount == 0) {
                currNode.complicatedSum = currNode.normalSum
            } else {
                currNode.complicatedSum = 0

                // we go through the metadata and get only the ones which can be an index
                for (md in currNode.metadata) {
                    if (1 <= md!! && md <= currNode.childCount) {
                        currNode.complicatedSum =
                                currNode.complicatedSum!! +  addMetadataComplicated(currNode.children[md - 1]!!)
                    }
                }
            }
        }

        return currNode.complicatedSum!!
    }

    data class Node(val childCount: Int, val metadataCount: Int) {
        val metadata = Array<Int?>(metadataCount) { null }
        val children = Array<Node?>(childCount) { null }

        var normalSum: Int? = null
        var complicatedSum: Int? = null
    }

}
