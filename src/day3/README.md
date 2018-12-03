# Day 3

This is the first day for which I've used some data classes:
```kotlin
data class Claim(val id: Int, val start: Coord, val end: Coord) {
    constructor(id: Int, startX: Int, startY: Int, sizeX: Int, sizeY: Int) :
            this(id, Coord(startX, startY), Coord(startX + sizeX - 1, startY + sizeY - 1))
}

data class Coord(val x: Int, val y: Int)
```

Pretty obvious what each represents. Claim has another constructor that uses the data type that
is in the exercise.

### Part 1
My very first thought was mapping each claim onto a big array one by one which would
require me to loop through each coordinate of each Claim and then go through the fabric's tiles
one by one to check whether any overlap happens.

But that would be quite slow when for example all the Claims have a size of the whole fabric
it would have a run time of `O(claimCount * n ^ 2 + n ^ 2) ≈ O(claimCount * n ^ 2)`. So I
though of another way.

What if for each row we would store when a claim begun and when it ended. That way we would
only need to go through the claims' heights and then go through the tiles of the fabric once.
So for the case above it would be `O(claimCount * n + n ^ 2)`. I don't thank that an algorithm
better than n^2 is possible because you have to check each tile whether it's overlapped more than
once. Maybe you could check only the tiles that are for sure in a claim, but then you'd have to
store them or go through a tile more than once, both would be a pain in the butt.

So my algorithm is:

```kotlin
val fabric: Array<Array<Int>> = Array(fabricSize + 1) { Array(fabricSize + 1) { 0 } }

// go through the claims and mark where a claim begun and ended in each row
for (claim in claims) {
    for (k in claim.start.y..claim.end.y) {
        fabric[claim.start.x][k]++
        // +1 needs to be added because the last tile of a claim has to be counted
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
```

### Part 2
For this I just wanted to complete it quickly so I can get a good place in the leaderboard.
So I just made what came to my mind first: checking each tile one by one whether it overlaps with
any other. For that I needed an overlap function for the Claim class:

```kotlin
fun overlaps(claim: Claim) = this.start.x <= claim.end.x && claim.start.x <= this.end.x &&
                             this.start.y <= claim.end.y && claim.start.y <= this.end.y
```

I remembered one which I used quite a long time ago. It has a visualization [here](https://silentmatt.com/rectangle-intersection/).

After that I wrote my algorithm:

```kotlin
// we check for each claim whether any other claim overlaps it
for (i in 0 until claims.size) {
    var overlapCount = 0
    for (k in 0 until claims.size) {
        if (i != k && claims[i].overlaps(claims[k]))
            overlapCount++
    }

    if (overlapCount == 0)
        return claims[i].id.toString()
}

return "No claim found with no other overlaps!"
```

Which worked, and to my surprise was quite fast. Around *30ms* for the given input. So I thought
I could maybe work on this to make it a bit faster, and it will do for any other input.

First thing that came to my mind is that of course after we found one overlap we don't need to look for any other
one. After implementing that, the time went down to about *22ms*. The second thing is maybe we could sort
the list somehow so it stops faster (finds an overlap more easily). So I just sorted by the x value of
the start coordinate which turned out to be plentiful because the time went down to *10ms*. So my final
algorithm is here:

```kotlin
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
```
