# Day 4
Getting the input was the tricky part here. First we need to sort the rows by the date so we know to which guard a 
"Guard falls asleep" belongs. For the parsing I mostly used regex.

  * `\[(.+)] (.+)` for getting the date with the first capture group.
  * `Guard #(\d+) begins shift` to get which guard begins a shift
  * `yyyy-MM-dd HH:mm` not regex but Java Date format, and it is for getting the date.
  
We just needed to get the date first and then sort the lines by that and process each line one by one. Quite
boring.

The class for storage is Guard:

```kotlin
data class Guard(val id: Int) {
    val times: Array<Int> = Array(60) { 0 }
    val asleepDayCountPerMinute: Array<Int> = Array(60) { 0 }
}
```

  * `times` contains for each day: (how many times guard fell asleep at this minute) + (how many times guard woke
  up at this minute)
  * `asleepDayCountPerMinute` contains for each minute on how many days the guard was asleep.
  
The conversion from `times` to `asleepDayCountPerMinute` happens via the `calculateAsleepDayCountPerMinute` function
of the Guard class:

```kotlin
fun calculateAsleepDayCountPerMinute() {
    asleepDayCountPerMinute[0] = times[0]

    for (i in 1 until times.size) {
        asleepDayCountPerMinute[i] += asleepDayCountPerMinute[i - 1] + times[i]
    }
}
```

Essentially: `asleepDayCountPerMinute[i] = sum(times[0..i])`.

### Part 1
After the hard part (getting the input and storing it correctly) we just need to do some max search and sum.
In the first part we need to get for how many minutes the guard was asleep so we add the `asleepDayCountPerMinute`
for each minute, because that contains for each minute how many days the guard was asleep.

We look for the maximum with the sum of that while also keeping a max of in which minute the guard was asleep the
most frequently. Conveniently, our `asleepDayCountPerMinute` contains exactly that.

```kotlin
// represents the guard who slept the most with id, sleepTime, mostSleptMinute
var mostMinuteAsleep = Triple(-1, 0, -1)

// for each guard we check for each minute on how many days he was asleep
for (guard in guards.values) {
    // how many minutes the guard was asleep all in all
    var sumOfSleepTimeGuard = 0

    // in which minute the guard was asleep on most days: count, minute
    var mostMinuteAsleepGuard = Pair(-1, 0)

    for (minute in 0 until guard.times.size) {
        // how many minutes were spent asleep
        sumOfSleepTimeGuard += guard.asleepDayCountPerMinute[minute]

        if (guard.asleepDayCountPerMinute[minute] > mostMinuteAsleepGuard.first) {
            mostMinuteAsleepGuard = Pair(guard.asleepDayCountPerMinute[minute], minute)
        }
    }

    if (sumOfSleepTimeGuard > mostMinuteAsleep.second) {
        mostMinuteAsleep = Triple(guard.id, sumOfSleepTimeGuard, mostMinuteAsleepGuard.second)
    }
}

return (mostMinuteAsleep.first * mostMinuteAsleep.third).toString()
```

### Part 2
We look for the guard who slept the most in any minute and we also want which minute that is. So we get in which
minute the guard was asleep the most frequently (again, conveniently our `asleepDayCountPerMinute` contains exactly that)
and then get the maximum from all guards.

```kotlin
var maxGuard: Guard = guards.values.first()
var maxMinute = 0

for (minute in 0 until 60) {
    // the guard that slept on most days in this minute
    val currMaxGuard = guards.values.maxBy { it.asleepDayCountPerMinute[minute] }!!

    // found a better solution
    if (currMaxGuard.asleepDayCountPerMinute[minute] > maxGuard.asleepDayCountPerMinute[maxMinute]) {
        maxGuard = currMaxGuard
        maxMinute = minute
    }
}

return (maxGuard.id * maxMinute).toString()
```
