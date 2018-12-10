# Day 9

For the input I just set some variables, because there are only 2 numbers.

### Part 1
For this challenge I immediately thought of the data structure double linked list because it fit the circle
of marbles so well because of it's constant insert and remove time and because I knew we only wanted to get
the elements relative to the current one and not one with a given index.
 I implemented my own which also fits the challenge better:

```kotlin
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
```

A LinkedList object describes a marble and it has a 'pointer' to the next and the previous marble.

It has two methods that are needed to solve the challenge:
  * `addClockwise` which adds an element `stepCount` elements clockwise from the current one
  * `removeCounterClockwise` which removes an element `stepCount` elements counter-clockwise from the current one.
  
With this the algorithm was simple, just translating the English words to the Kotlin language:

```kotlin
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
```

I keep track of the current marble and the scores for all players (which in part 2 will get bigger so that's why
a Long array is needed). Other than that it's quite straightforward.

### Part 2
See part 1.
