# Day 7
Input was quite easy again. I used the class below for storing an instruction.

```kotlin
data class Instruction(val id: Char) {
    val prerequisites: MutableList<Instruction> = mutableListOf()
    val prerequisitesOf: MutableList<Instruction> = mutableListOf()
    val takesTime = id - 'A' + 61
}
```

  * `prerequisites` contains all the instruction that are a prerequisite for the instruction
  * `prerequisitesOf` contain all the instruction that this instruction is a prerequisite of
  * `takesTime` is used in the 2nd part and stores how much time this instruction takes to be completed
  
### Part 1
For this we just needed to traverse the graph of the instructions. We start by searching for the instructions
that have no prerequisites:

```kotlin
val availableInstructions = TreeSet(instructions.values.filter { it.prerequisites.isEmpty() }
                .toSortedSet(Comparator<Instruction> { i1, i2 -> i1.id.compareTo(i2.id) }))
```

and we store them in a sorted set because we always want to get the one that is first alphabetically.

We also store whether an instruction has been finished in a HashMap:

```kotlin
// this contains whether an instruction has been finished
val isInstructionDone: HashMap<Char, Boolean> = hashMapOf()
for (instruction in instructions.values) {
    isInstructionDone[instruction.id] = false
}
```

After that we will always store the instruction that can be completed in the availableInstructions. We get
them one by one and mark the completed. After each marking we would have to check all the other non-completed
instructions whether they can be completed or not. But we are more clever than that, that's why we stored the
`prerequisitesOf`, because that way we only have to check those instructions.

```kotlin
// while we have available instructions
while (!availableInstructions.isEmpty()) {
    // we finish the current instruction
    val currInstruction = availableInstructions.pollFirst()
    isInstructionDone[currInstruction.id] = true
    orderOfInstructions.append(currInstruction.id)

    for (instruction in currInstruction.prerequisitesOf) {
        // there is no prerequisite that is not done
        if (instruction.prerequisites.find { !isInstructionDone[it.id]!! } == null &&
                !availableInstructions.contains(instruction)) {
            availableInstructions.add(instruction)
        }
    }
}
```

### Part 2
Things just got a lot trickier. The basic concept is the same as in part 1, by that I mean the traversal is the same
but we also need to store for each worker what (s)he (from now on I'll just write he because that is exactly
1 character shorter, but you could say that doesn't matter because I just wasted a lot of characters writing
that rule down, so sometimes I'll use he and sometimes I'll use she) is working on. We also have to store for
each instruction when it can be finished and update these variables each time an instruction gets done.

I chose to store these in `instructionDoneTime`, which stores when an instruction is finished and in `workersFinish`
which stores for each worker when she finishes. The `availableInstructions` is the same as before.

```kotlin
// this contains whether an instruction has been finished and if it has it stores
// when the instruction was finished
val instructionDoneTime: HashMap<Char, Int?> = hashMapOf()
for (instruction in instructions.values) {
    instructionDoneTime[instruction.id] = null
}

// the instructions with which we need to begin has no prerequisites
// we also need to map it to a sorted tree that is sorted in alphabetical order
val availableInstructions = TreeSet(instructions.values.filter { it.prerequisites.isEmpty() }
        .toSortedSet(Comparator<Instruction> { i1, i2 -> i1.id.compareTo(i2.id) }))

val workersFinish = Array(workerCount) { 0 }
```

So each time we pop an instruction we need to update it's finish time (based on it's start time which can be
calculated by getting the max of the prerequisites finish time). For that we need to find a worker, who is free
at the time the instruction needs to be started. If there is no free worker we find the one that finishes the earliest
and give him the instruction.

```kotlin
// we look for a worker who can do the current instruction by checking when (s)he finishes
var workerWorkingId: Int? = null
for (i in 0 until workerCount) {
    // we have a worker who can do this instruction
    if (workersFinish[i] <= instructionDoneTime[currInstruction.id]!! - currInstruction.takesTime) {
        workerWorkingId = i
        break
    }
}

// we found a worker that can work on this instruction
if (workerWorkingId != null) {
    workersFinish[workerWorkingId] = instructionDoneTime[currInstruction.id]!!
} else { // no worker can work on the current instruction right now
    // find the worker who finishes the earliest
    workerWorkingId = (0 until workerCount).minBy { workersFinish[it] }!!

    // delegate work to that worker
    workersFinish[workerWorkingId] += currInstruction.takesTime
    instructionDoneTime[currInstruction.id] = workersFinish[workerWorkingId]
}
```

After that we just need to check all the `prerequisitesOf` instructions like before.

```kotlin
// we need to update all the instructions that's prerequisites contain the current instruction
for (instruction in currInstruction.prerequisitesOf) {
    // all prerequisites have been finished and not already added to available
    if (instruction.prerequisites.all { instructionDoneTime[it.id] != null } &&
            !availableInstructions.contains(instruction)) {
        availableInstructions.add(instruction)
    }
}
```

And our solution will be the max of the `workersFinish` array.
