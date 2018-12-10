# Day 10
I processed the input with the regex pattern `position=<\s*(.*),\s*(.*)> velocity=<\s*(.*),\s*(.*)>`.

I used some basic data structures:

```kotlin
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
```

The `MovingLight` represents a single light source and it can be updated with `secondCount` time.

### Part 1
The trick I used here was updating all the `MovingLight` objects until the highest and lowest y coordinates
were larger than the height of a letter, that is exactly 9 (I calculated that with some trial and error).
If we wanted to we could maybe go until the difference of y coordinates decrease, but after looking at other
people's animated solutions I think a height of 9 is what everyone else got.

So for the first part of the algorithm I just update the lights and store the lowest and highest y coordinates.

```kotlin
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
```

(I also store the solution for part 2 here, so I won't have to calculate that again.)

After that we just need to print out the solution. But first we need to know how big the letter's bounding rectangle
is so, we need to get the top left and the bottom right lights:

```kotlin
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
```

And with that we can make a boolean array and mark the light sources with true.

```kotlin
// we make the letters on a boolean 'board' (matrix)
val letterArray =
        Array(botRight.x - topLeft.x + 1) { Array(botRight.y - topLeft.y + 1) { false } }
for (movingLight in movingLights) {
    letterArray[movingLight.position.x - topLeft.x][movingLight.position.y - topLeft.y] = true
}
```

And then for the conversion to string we just go through the `letterArray` and append all the light sources
to a `StringBuilder`.

```kotlin
// we convert the board to a string
val out = StringBuilder()
for (i in 0 until letterArray[0].size) {
    for (k in 0 until letterArray.size) {
        out.append(if (letterArray[k][i]) { "*" } else { " " })
    }

    out.append("\n")
}

return out.toString()
```

### Part 2
I stored the solution in part 1, so I just had to print that out.
