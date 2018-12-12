# Day 12
Reading the input was done by splitting each line.
An interesting thing is that I did not store the transitions that resulted int the same
plant state. For example `##.## => .` has no useful information for us, because we could
just simply leave the plant the same.

I stored the transitions in a `Map<String, Char>` for constant time checking whether a transition
is possible.

The whole input algorithm is:

```kotlin
initialState = lines[0].substring(15).toCharArray()

val transitionPairs = lines.sliceArray(2 until lines.size).map {
    val line = it.split(" => ")

    line[0] to line[1][0]
}.filter { it.first[2] != it.second }.toTypedArray()
transitions = mapOf(*transitionPairs)
```

### Part 1
For part 1 I wrote a simple function which I later moved to `getSumsForGenerationsUntil(..)` function
so I'll only talk about that function because it's essentially the same.

I store the plants in an array of size 2000 which should be plenty enough. The 0th plant is at
`sizeOfArray / 2` and the negative plants are to the left while the positive plants are to the right.

The function returns the sums of plant indices up to the parameter `lastGeneration`. For storing that I
use an array.

For checking whether a plant needs to change it's state we need the `String` that represents it and
it's two neighbours to the left and to the right. For storing that I used a `StringBuilder` which
I update each iteration step to represent the current plant's neighbours by popping the first
character and adding the `currentPlantIndex + 2` character to the end. This way I don't have to rebuild
a new `String` object each iteration step.

While going through the plants in a generation I store only the plants' indices that needs to flip
their state at the end of the generation. I found this to be the best because we don't want to update
the plants until the end of our plants iteration so we need to store the new generation layout somehow.
We could do that by having a new array of plants but that would require us to always allocate a new array
and at the end copy all of it's elements to the `plants` array. Another advantage of having the flipped
plants in a list is the calculation of the sum if the plants.

We have to go through all the flipped plants anyway, to replace them in the `plants` array and while
we are doing that we can calculate the new sum by subtracting the new '.' non-plants indices
and adding the new '#' indices. And after we have added all that we can just add the previous
generation's sum to get the new generation's sum.

With all of that optimization the whole code looks like:
```kotlin
private fun getSumsForGenerationsUntil(lastGeneration: Int): Array<Int> {
    val generationSums = Array(lastGeneration + 1) { 0 }

    val plantsArraySize = 2000

    // the 0th plant is plantsArraySize / 2
    val plants = CharArray(plantsArraySize) { '.' }
    var firstPlant = plantsArraySize / 2
    var lastPlant = firstPlant + initialState.size - 1

    // we copy the initial state
    for (i in 0 until initialState.size)
        plants[plantsArraySize / 2 + i] = initialState[i]

    // what plants to 'flip' (.->#,#->.) after each generation
    val flipPlants = mutableListOf<Int>()
    for (generationAt in 1..lastGeneration) {
        var newFirstPlant = firstPlant
        var newLastPlant = lastPlant

        // stores the string of the current plant's 2 neighbours
        val currentString = StringBuilder()
        for (i in firstPlant - 5 until firstPlant) // we init it with th string before the first plant
            currentString.append(plants[i])

        // we check for each plant whether it can transition to something else
        for (plantId in firstPlant - 2..lastPlant + 2) {
            // we update the string
            currentString.deleteCharAt(0)
            currentString.append(plants[plantId + 2])

            // this plant flips it's state
            val transition = transitions[currentString.toString()]
            if (transition != null) {
                if (plantId < newFirstPlant) newFirstPlant = plantId
                if (plantId > newLastPlant)  newLastPlant = plantId

                flipPlants.add(plantId)
            }
        }

        // updating where the first and last plants are
        firstPlant = newFirstPlant
        lastPlant = newLastPlant

        // we flip the plants we need to flip
        for (plantId in flipPlants) {
            plants[plantId] = if (plants[plantId] == '.') { '#' } else { '.' }

            // we can't calculate sum based on previous generation if it is the first generation
            if (generationAt != 1) {
                // we subtract if we flipped to .
                generationSums[generationAt] += if (plants[plantId] == '.') {
                    - plantId + plantsArraySize / 2
                } else {  // we add if we flipped to #
                    plantId - plantsArraySize / 2
                }
            }
        }
        flipPlants.clear()

        // we need to calculate it by summing in the first generation
        if (generationAt == 1)
            generationSums[generationAt] = (firstPlant..lastPlant).filter { plants[it] == '#' }.map { it - plantsArraySize / 2 }.sum()
        else  // we can rely on previous generations in generations 2 and above
            generationSums[generationAt] += generationSums[generationAt - 1]
    }

    return generationSums
}
```

For the first part I just call this function and return the last element of the array.


### Part 2
I checked the sums by hand first and realized that the differences of them after a while are
constant. So we just need to find the place where it changes from non-constant to constant
and then we can calculate the other generations' sum easily in constant time.

I wanted to make sure that after the point I found it stayed constant so I checked not for only the
difference after a point but the difference after the next point as well. From that point on
I just needed to get the sum till the non-constant point and then add the constant difference
leftGeneration times.

```kotlin
val generationCount = 50000000000L
val sums = getSumsForGenerationsUntil(200)
val differences = (1 until sums.size).map { sums[it] - sums[it - 1] }

// we go through the differences searching for a starting point from which point on they repeat
var firstSameDiff: Int? = null
for (i in 0 until differences.size - 3) {
    if (differences[i] == differences[i + 1] && differences[i + 1] == differences[i + 2]) {
        firstSameDiff = i
        break
    }
}

if (firstSameDiff == null) {
    return "Couldn't find the sum. Maybe increase the generation count!"
}

// we add the numbers till the repetition and then repeat the difference x times till we get to the last
// generation we need
return (sums[firstSameDiff] + differences[firstSameDiff + 1] * (generationCount - firstSameDiff)).toString()
```
