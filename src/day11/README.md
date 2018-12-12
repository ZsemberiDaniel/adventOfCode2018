# Day 11
Didn't read the input but rather stored the number in a variable.

There were no need for data structures here.

### Part 1
The calculation of the cells were quite easy. I used a trick for calculating the sums.
I have the array `cellSums` and

`cellSums[i][k] = sum(cells[0..i][0..k])`

And this can be calculated in constant time while calculating the cell's fuel level

`cellSums[y][x] = cells[y][x] + cellSums[y - 1][x] + cellSums[y][x - 1] - cellSums[y - 1][x - 1]`

by using the previous `cellSums` calculations.

This is useful because this way we can calculate the sum of an area on the field in constant time
by:

`sumOfArea(startX, startY, size) = cellSums[y + size][x + size] - cellSums[y - 1][x + size] - cellSums[y + size][x - 1] + cellSums[y - 1][x - 1]`

So we don't need to go through each 3 by 3 field.

The full code is:

```kotlin
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
```

### Part 2
No other trick was used here because the trick above was fast enough for this as well.
I did not need to calculate the `cells` and `cellSums` array because I stored it from the first
exercise.
Full code is:

```kotlin
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
```
