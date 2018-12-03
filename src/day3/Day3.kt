package day3

import RunnablePuzzleSolver

class Day3 : RunnablePuzzleSolver {
    private val fabricSize = 1000
    private lateinit var claims: List<Claim>

    override fun readInput1(line: Array<String>) {
        // splitting by all the delimiters and mapping to Claim objects
        claims = line.map {
            val split = it.split("#", " @ ", ",", ": ", "x")

            Claim(split[1].toInt(), split[2].toInt(), split[3].toInt(), split[4].toInt(), split[5].toInt())
        }
    }

    override fun readInput2(line: Array<String>) { }

    override fun solvePart1(): String {
        val fabric: Array<Array<Int>> = Array(fabricSize + 1) { Array(fabricSize + 1) { 0 } }

        // go through the claims and mark where a claim begun and ended in each row
        for (claim in claims) {
            for (k in claim.start.y..claim.end.y) {
                fabric[claim.start.x][k]++
                fabric[claim.end.x + 1][k]--
            }
        }

        var overlapCount = 0
        var currClaimCount = 0
        // we go through the rows of the fabric
        for (i in 0..fabricSize) {
            currClaimCount = 0

            // we go through the
            for (k in 0..fabricSize) {
                currClaimCount += fabric[k][i]

                if (currClaimCount > 1)
                    overlapCount++
            }
        }

        return overlapCount.toString()
    }

    override fun solvePart2(): String {
        claims = claims.sortedBy { it.start.x }

        // we check for each claim whether any other claim overlaps it
        for (i in 0 until claims.size) {
            var k = 0
            while (k < claims.size) {
                if (i != k && claims[i].overlaps(claims[k]))
                    break

                k++
            }

            // we reached the end of the inner for loop -> no other claim overlaps it
            if (k == claims.size)
                return claims[i].id.toString()
        }

        return "No claim found with no other overlaps!"
    }

    data class Claim(val id: Int, val start: Coord, val end: Coord) {
        constructor(id: Int, startX: Int, startY: Int, sizeX: Int, sizeY: Int) :
                this(id, Coord(startX, startY), Coord(startX + sizeX - 1, startY + sizeY - 1))

        fun overlaps(claim: Claim) = this.start.x <= claim.end.x && claim.start.x <= this.end.x &&
                                     this.start.y <= claim.end.y && claim.start.y <= this.end.y
    }

    data class Coord(val x: Int, val y: Int)

}