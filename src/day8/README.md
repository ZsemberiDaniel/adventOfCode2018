# Day 8
The input was kinda complicated here. I used a `Node` class to represent each node. It has all the metadata and children
and the solution to the first and the second part stored as well.

```kotlin
data class Node(val childCount: Int, val metadataCount: Int) {
    val metadata = Array<Int?>(metadataCount) { null }
    val children = Array<Node?>(childCount) { null }

    var normalSum: Int? = null
    var complicatedSum: Int? = null
}
```

So for the input I used recursion because that seemed to be the most simple and elegant solution here. I kept
track of how long the `Node` was in the input array, and returned that and the Node. Other than that quite a
simple algorithm:

```kotlin
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
```

### Part 1
I used recursion again, and it's a simple on again. I also chose to store the sum of the metadata here so it
can be used for part 2.

```kotlin
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
```

### Part 2
We needed to adjust the algorithm from part 1 to reflect the exercise but the concept of the algorithm is the same
as before.

```kotlin
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
```
