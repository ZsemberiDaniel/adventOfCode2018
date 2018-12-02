# Day 1
### Part 1
It's clear that all it searches for is the sum of all numbers. Fortunately kotlin has a
built in summation function.

```kotlin
return changes.sum().toString()
```

### Part 2
I don't think that much optimization was needed here, but I used a HashSet just in case.
A HashSet has a good insert and look up complexity (constant), so it's prefect for this
exercise. We just need to go through all the sub-sums add them all to a HashSet and then
check each time a new one is calculated whether that is in the HashSet or not.

```kotlin
val been = hashSetOf<Int>()
var current = 0 // the current sub-sum
var i = 0 // where we are in the changes array

while (!been.contains(current)) {
    been.add(current)
    current += changes[i]
    
    // wrapping around
    i = if (i == changes.size - 1) {0} else {i + 1}
}

return current.toString()
```
