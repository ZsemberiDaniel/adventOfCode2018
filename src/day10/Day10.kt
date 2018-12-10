package day10

import RunnablePuzzleSolver
import java.lang.Math.abs
import java.lang.StringBuilder
import java.util.regex.Pattern

class Day10 : RunnablePuzzleSolver {
    lateinit var movingLights: Array<MovingLight>
    var part2Solution: Int = -1

    override fun readInput1(lines: Array<String>) {
        val regex = Pattern.compile("""position=<\s*(.*),\s*(.*)> velocity=<\s*(.*),\s*(.*)>""")

        movingLights = lines.map {
            val matcher = regex.matcher(it).apply { matches() }

            MovingLight(matcher.group(1).toInt(), matcher.group(2).toInt(),
                    matcher.group(3).toInt(), matcher.group(4).toInt())
        }.toTypedArray()
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        var tickCount = 0
        var yDifference = Int.MAX_VALUE

        // we go until they assemble in the y dimension
        while (yDifference > 9) {
            // for each light we add the distance to the closest light
            var highestY = movingLights[0].position.y
            var lowestY = movingLights[0].position.y

            // updating each light
            for (movingLight in movingLights) {
                movingLight.update()

                if (movingLight.position.y > highestY)
                    highestY = movingLight.position.y
                if (movingLight.position.y < lowestY)
                    lowestY = movingLight.position.y
            }

            yDifference = highestY - lowestY
            tickCount++
        }
        part2Solution = tickCount

        // we determine the top left and the bottom right positions (bounding rectangle)
        val topLeft = Vector(Int.MAX_VALUE, Int.MAX_VALUE)
        val botRight = Vector(Int.MIN_VALUE, Int.MIN_VALUE)
        for (movingLight in movingLights) {
            if (movingLight.position.x < topLeft.x)
                topLeft.x = movingLight.position.x
            if (movingLight.position.y < topLeft.y)
                topLeft.y = movingLight.position.y

            if (movingLight.position.x > botRight.x)
                botRight.x = movingLight.position.x
            if (movingLight.position.y > botRight.y)
                botRight.y = movingLight.position.y
        }

        // we make the letters on a boolean 'board' (matrix)
        val letterArray =
                Array(botRight.x - topLeft.x + 1) { Array(botRight.y - topLeft.y + 1) { false } }
        for (movingLight in movingLights) {
            letterArray[movingLight.position.x - topLeft.x][movingLight.position.y - topLeft.y] = true
        }

        // we convert the board to a string
        val out = StringBuilder()
        for (i in 0 until letterArray[0].size) {
            for (k in 0 until letterArray.size) {
                out.append(if (letterArray[k][i]) { "*" } else { " " })
            }

            out.append("\n")
        }

        return out.toString()
    }

    override fun solvePart2(): String {
        return part2Solution.toString()
    }

    data class MovingLight(var position: Vector, var velocity: Vector) {
        constructor(x: Int, y: Int, xVel: Int, yVel: Int) : this(Vector(x, y), Vector(xVel, yVel))

        fun update(secondCount: Int = 1) {
            this.position += this.velocity * secondCount
        }
    }
    data class Vector(var x: Int, var y: Int) {
        operator fun plus(vect: Vector) = Vector(this.x + vect.x, this.y + vect.y)
        operator fun minus(vect: Vector) = Vector(this.x - vect.x, this.y - vect.y)
        operator fun times(scalar: Int) = Vector(scalar * this.x, scalar * this.y)
    }

}
