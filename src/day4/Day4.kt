package day4

import RunnablePuzzleSolver
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Day4 : RunnablePuzzleSolver {
    val guards: HashMap<Int, Guard> = hashMapOf()

    override fun readInput1(lines: Array<String>) {
        // patterns for matching the input string
        val datePattern = Pattern.compile("""\[(.+)] (.+)""")
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

        val guardStartPattern = Pattern.compile("""Guard #(\d+) begins shift""")

        // gets the date from a line and sorts the list by the date
        val linesWithDates = lines.map {
            val matcher = datePattern.matcher(it).apply { find() }

            // first group is date second is the rest of the string
            Pair(dateFormatter.parse(matcher.group(1)), matcher.group(2))
        }.sortedBy { it.first }

        var currGuardId: Int = -1
        for (line in linesWithDates) {
            // we have a new guard
            val guardStartMatcher = guardStartPattern.matcher(line.second).apply { find() }

            if (guardStartMatcher.matches()) {
                currGuardId = guardStartMatcher.group(1).toInt()

                // we don't have a guard with this id so we need to make one
                if (!guards.containsKey(currGuardId))
                    guards[currGuardId] = Guard(currGuardId)
            } else { // current guard either falls asleep or wakes up
                if (line.second == "wakes up") {
                    guards[currGuardId]!!.times[line.first.minutes]--
                } else if (line.second == "falls asleep") {
                    guards[currGuardId]!!.times[line.first.minutes]++
                }
            }
        }

        // calculate some stuff for later usage
        for (guard in guards.values)
            guard.calculateAsleepDayCountPerMinute()
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
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
    }

    override fun solvePart2(): String {
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
    }

    data class Guard(val id: Int) {
        val times: Array<Int> = Array(60) { 0 }
        val asleepDayCountPerMinute: Array<Int> = Array(60) { 0 }

        /**
         * Calculates for each minute on how many days the guard was asleep.
         */
        fun calculateAsleepDayCountPerMinute() {
            asleepDayCountPerMinute[0] = times[0]

            for (i in 1 until times.size) {
                asleepDayCountPerMinute[i] += asleepDayCountPerMinute[i - 1] + times[i]
            }
        }
    }

}