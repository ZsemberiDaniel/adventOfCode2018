# Day 6
The input was easy. I used numbers instead of letters for identifying the areas.
While getting the input I also stored the bounding rectangle of the points:

```kotlin
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
```

A simple Coord data class was used for storage:

```kotlin
data class Coord(var x: Int, var y: Int) {
    fun distanceFrom(x: Int, y: Int) = abs(x - this.x) + abs(y - this.y)
}
```

### Part 1
I did what first came to my mind even after giving it some thought. The easiest was to measure for each tile the
distance to all points and then store the min of those.

There were two tricky parts though. The first one is that we also needed to store how many times the min occurred
because if it occurred more than once than that tile did not count to any area.

The second part was checking whether an area was infinite or not. I did that by marking the areas that have tiles
on the edge because those are the ones that will be infinite.

```kotlin
// this will store whether an area is on the edge
val onEdge = Array(coords.size) { false }
// this will store the sizes of the areas
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
            } else if (dist == minDist) { // we found a coord that is the same distance as the min
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
```

### Part 2
So here we had to go through all the tiles and for each tiles all the points, so I didn't even start thinking about
another solution.
 
For me the tricky part here was how many tiles we need to expand the bounding rectangle so we count all the tiles that
can be in the area we are looking for.

The worst case is when there is one separate tile away from all the others, while the others are all in one line at
the edge of the map. Like this:

```
X . . . . . . . . . . . .
X . . . . . . . . . . . .
X . . . . . . . . . . . .
X . . . . . . . . . . . .
X . . . . . . . . . . . .
. . . . . . . . . . . . .
. . . . . . . . . . . . .
. . . . . . . . . . . . X
. . . . . . . . . . . . .
```

If this happens then there has to be some tiles which are to the left of the Xs in a column and are good. So I
used my amazing mathematics skills (hahh...) to figure this one out. We need to calculate sum of the distances for
the top left corner because from that we can infer what the sum of distances for the tiles on the left will be.

  * Distance from the lonely X is the bounding rectangle's width.
  * Distance from the Xs in a column is (if n = 'number of Xs-1') n + (n - 1) + ... + 1 = n * (n + 1) / 2
  
If we do this for each side while moving all the Xs to be in the worst case we can do this for them as well.
For each pair of parallel sides it will be the same.

After we know that we also know that if we move one tile to the left the sum will increase by n because the
point will get 1 further away from each X. So our formula is:

`(neededPoints - boundingWidth - n * (n + 1) / 2) / n`

We divide by n because we want to know how many tiles we can go left.

With all this knowledge we can finally write our code:

```kotlin
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
```
