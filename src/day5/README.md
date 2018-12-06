# Day 5
Input was easy here.

### Part 1
My first thought was going through the string and then removing elements as needed. But after giving it some thought
that would be quite inefficient because the string would have to be remade each time so what if we built the
new string ourselves.

That is how I ended up with going through the polymer and then adding each character if it does react with the one
at the end of the new string. However if it reacts then we need to remove the last character from the new string
and we should not add the new character.

Of course the most efficient way of building this string is using a StringBuilder.

```kotlin
val polymer1 = StringBuilder()
for (i in 0 until polymer.length) {
    // the reacted polymer is empty -> other checks will fail in the if
    if (polymer1.isEmpty()) {
        polymer1.append(polymer[i])
    } else {
        // found a character that clashes with the one at the end of the final polymer
        if (polymer1[polymer1.length - 1] != polymer[i] &&
                polymer1[polymer1.length - 1].toLowerCase() == polymer[i].toLowerCase()) {
            // remove the last one and don't add the current
            polymer1.deleteCharAt(polymer1.length - 1)
        } else {
            // no clash -> add the current
            polymer1.append(polymer[i])
        }
    }
}

return polymer1.length.toString()
```

### Part 2
Using the code above we need to check the result string and search for a minimum. But first we need to loop through
all the characters the polymer has. There are several approaches: we could loop through the whole abc but for small
inputs that would be quite inefficient, we could first look for the unique characters in the polymer then check
all of that in a separate loop.

But the approach I chose was combining the latter one with reaction simulation. I have a HashSet, which contains the
characters we have already checked. Then we go through the original polymer and for each letter we check whether 
that one has already been checked and if it has not we use that letter as the one we have to omit from the polymer.

I also added a check whether the been set has fewer than 26 letters, because if we went through the whole
alphabet we don't want to check any more letters in the original polymer.

The resulting code is quite similar to the first part:

```kotlin
var k = 0
val been = HashSet<Char>()

var solution = Pair(Int.MAX_VALUE, '-')

// we go through the whole string or until we went through the whole abc
while (k < polymer.length && been.size < 26) {
    // we check the letters that have not been
    if (!been.contains(polymer[k].toLowerCase())) {
        been.add(polymer[k].toLowerCase()) // add current letter to been

        val polymer1 = StringBuilder()
        for (i in 0 until polymer.length) {
            // we skip the letters that are omitted
            if (polymer[i].toLowerCase() == polymer[k].toLowerCase())
                continue

            // the reacted polymer is empty -> other checks will fail in the if
            if (polymer1.isEmpty()) {
                polymer1.append(polymer[i])
            } else {
                // found a character that clashes with the one at the end of the final polymer
                if (polymer1[polymer1.length - 1] != polymer[i] &&
                        polymer1[polymer1.length - 1].toLowerCase() == polymer[i].toLowerCase()) {
                    // remove the last one and don't add the current
                    polymer1.deleteCharAt(polymer1.length - 1)
                } else {
                    // no clash -> add the current
                    polymer1.append(polymer[i])
                }
            }
        }

        // we look for the letter with the minimal length polymer
        if (polymer1.length < solution.first)
            solution = Pair(polymer1.length, polymer[k])
    }

    k++
}

return solution.first.toString()
```
