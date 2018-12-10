package day9

import RunnablePuzzleSolver

class Day9 : RunnablePuzzleSolver {
    private val playerCount = 464
    private var lastMarble = 70918

    override fun readInput1(lines: Array<String>) { }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        val playerScores = LongArray(playerCount)

        var currMarble = LinkedList(0)

        // we go through the marbles
        for (nextMarbleIndex in 1..lastMarble) {
            if (nextMarbleIndex % 23 == 0) {
                val removed = currMarble.removeCounterClockwise(7)

                playerScores[nextMarbleIndex % playerCount] = playerScores[nextMarbleIndex % playerCount] + nextMarbleIndex + removed.value
                currMarble = removed.next
            } else {
                currMarble = currMarble.addClockwise(1, nextMarbleIndex)
            }
        }

        return playerScores.max()!!.toString()
    }

    override fun solvePart2(): String {
        lastMarble *= 100

        return solvePart1()
    }

    class LinkedList<T>(var value: T) {
        var next: LinkedList<T> = this
        var previous: LinkedList<T> = this

        fun addClockwise(stepCount: Int, elem: T): LinkedList<T> {
            // we traverse the linked list step amount
            var addAfter = this
            for (i in 1..stepCount)
                addAfter = addAfter.next

            // making and adding a new element
            val newElem = LinkedList(elem)

            newElem.next = addAfter.next
            newElem.previous = addAfter

            addAfter.next.previous = newElem
            addAfter.next = newElem

            return newElem
        }

        fun removeCounterClockwise(stepCount: Int): LinkedList<T> {
            // we traverse the linked list step amount counter clockwise
            var removeThis = this
            for (i in 1..stepCount)
                removeThis = removeThis.previous

            // updating the list
            removeThis.previous.next = removeThis.next
            removeThis.next.previous = removeThis.previous

            return removeThis
        }
    }

}
