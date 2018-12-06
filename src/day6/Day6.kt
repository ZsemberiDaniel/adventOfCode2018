package day6

import RunnablePuzzleSolver
import java.lang.Math.abs

class Day6 : RunnablePuzzleSolver {
    val part2AreaLessThan = 10_000

    lateinit var coords: Array<Coord>
    var topLeft: Coord = Coord(Int.MAX_VALUE, Int.MAX_VALUE)
    var bottomRight: Coord = Coord(Int.MIN_VALUE, Int.MIN_VALUE)

    override fun readInput1(lines: Array<String>) {
        coords = lines.map {
            val cs = it.split(", ").map { it.toInt() }

            // we look for the bounding rectangle
            if (cs[0] < topLeft.x)
                topLeft.x = cs[0]
            if (cs[0] > bottomRight.x)
                bottomRight.x = cs[0]
            if (cs[1] < topLeft.y)
                topLeft.y = cs[1]
            if (cs[1] > bottomRight.y)
                bottomRight.y = cs[1]

            Coord(cs[0], cs[1])
        }.toTypedArray()
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        val onEdge = Array(coords.size) { false }
        val areas = Array(coords.size) { 0 }

        // we go through each tile and check to which point it is the closest to
        for (y in topLeft.y..bottomRight.y) {
            for (x in topLeft.x..bottomRight.x) {
                var minDist = coords[0].distanceFrom(x, y)
                var minDistFrom: Int? = 0 // null if min dist is not only from one coord

                // check the distances from each point and check whether that distance is unique or not
                for (i in 1 until coords.size) {
                    val dist = coords[i].distanceFrom(x, y)

                    if (dist < minDist) {
                        minDist = dist
                        minDistFrom = i
                    } else if (dist == minDist) {
                        minDistFrom = null
                    }
                }

                // we found a unique min distance
                if (minDistFrom != null) {
                    areas[minDistFrom]++

                    // we also need to check whether it is on the edge of the map (the area will bi infinite)
                    if (x == topLeft.x || x == bottomRight.x)
                        onEdge[minDistFrom] = true
                    else if (y == topLeft.y || y == bottomRight.y)
                        onEdge[minDistFrom] = true
                }
            }
        }

        // return the biggest area that is not on the edge
        return areas.filterIndexed { index, _ -> !onEdge[index] }.max().toString()
    }

    override fun solvePart2(): String {
        var areaSize = 0

        val boundingRectangleWidth = bottomRight.x - topLeft.x
        val boundingRectangleHeight = bottomRight.y - topLeft.y

        // we need to expand the bounding rectangle by this much because there may be tiles that are in the area there
        val yExpand = (part2AreaLessThan - (coords.size + 1) * coords.size / 2 - boundingRectangleHeight) / coords.size
        val xExpand = (part2AreaLessThan - (coords.size + 1) * coords.size / 2 - boundingRectangleWidth) / coords.size

        // we go through each tile and check the distance from all points
        for (y in (topLeft.y - yExpand)..(bottomRight.y + yExpand)) {
            for (x in (topLeft.x - xExpand)..(bottomRight.x + xExpand)) {
                // sum of distances to all points
                val sumOfDist = coords.sumBy { it.distanceFrom(x, y) }

                // we found a tile that fits the description of the solution
                if (sumOfDist < part2AreaLessThan) {
                    areaSize++
                }
            }
        }
        return areaSize.toString()
    }

    data class Coord(var x: Int, var y: Int) {
        fun distanceFrom(x: Int, y: Int) = abs(x - this.x) + abs(y - this.y)
    }

}
