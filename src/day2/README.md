# Day 2
### Part 1
It was easy to count how many of each letter an id had with the group function
of Kotlin. After that we just had to check whether any of them was 2 or 3 and count
them. The most important line is where we assign to grouped.

```kotlin
var twiceCount = 0
var threeTimesCount = 0

for (id in ids) {
    // contains how many times unique letters appear (that appear at least twice) in an id
    val grouped = id.toCharArray().groupBy { it }.filter { it.value.size >= 2 }.map { it.value.size }

    if (2 in grouped)
        twiceCount++
    if (3 in grouped)
        threeTimesCount++
}

return (twiceCount * threeTimesCount).toString()
```

### Part 2
I wanted to make an algorithm that is better than `n * n * characterCount`. I tried by figuring
out a comparision method for the string so I can sort the array and only have on average `n * log(n)`
comparison. But I think that approach is impossible. So I stuck with the basic approach of checking
for each id with every other one. A small improvement is only checking the ones that have a higher index
because the ones before have already been checked. I calculate the similar characters with a `filter`.

```kotlin
for (i in 0 until ids.size) {
    for (k in i + 1 until ids.size) {
        // the similar characters in the ith and kth word
        val similar = ids[i].filterIndexed { index, c -> c == ids[k][index] }

        // found the solution
        if (similar.length == ids[i].length - 1) {
            return similar
        }
    }
}

return "None found with difference 1"
```