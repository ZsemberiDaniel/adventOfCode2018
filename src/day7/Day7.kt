package day7

import RunnablePuzzleSolver
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap

class Day7 : RunnablePuzzleSolver {
    private val instructions: HashMap<Char, Instruction> = hashMapOf()

    private val workerCount = 5

    override fun readInput1(lines: Array<String>) {
        lines.forEach {
            // which is needed for the instructionId
            val neededId = it[5]
            val instructionId = it[36]

            // we create objects if they are not present in the list
            if (!instructions.containsKey(neededId))
                instructions[neededId] = Instruction(neededId)

            if (!instructions.containsKey(instructionId))
                instructions[instructionId] = Instruction(instructionId)

            // add the prerequisite for the current line's instruction
            instructions[instructionId]!!.prerequisites.add(instructions[neededId]!!)
            instructions[neededId]!!.prerequisitesOf.add(instructions[instructionId]!!)
        }
    }

    override fun readInput2(lines: Array<String>) { }

    override fun solvePart1(): String {
        val orderOfInstructions = StringBuilder()

        // this contains whether an instruction has been finished
        val isInstructionDone: HashMap<Char, Boolean> = hashMapOf()
        for (instruction in instructions.values) {
            isInstructionDone[instruction.id] = false
        }

        // the instructions with which we need to begin has no prerequisites
        // we also need to map it to a sorted tree that is sorted in alphabetical order
        val availableInstructions = TreeSet(instructions.values.filter { it.prerequisites.isEmpty() }
                .toSortedSet(Comparator<Instruction> { i1, i2 -> i1.id.compareTo(i2.id) }))

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

        return orderOfInstructions.toString()
    }

    override fun solvePart2(): String {
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

        // while we have available instructions
        while (!availableInstructions.isEmpty()) {
            val currInstruction = availableInstructions.pollFirst()

            // we get the prerequisite instruction that was finished the last and get when was it finished
            val currInstructionStartTime =
                    instructionDoneTime[currInstruction.prerequisites.maxBy { instructionDoneTime[it.id] ?: 0 }?.id ?: -1] ?: 0
            instructionDoneTime[currInstruction.id] = currInstructionStartTime + currInstruction.takesTime

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

            // we need to update all the instructions that's prerequisites contain the current instruction
            for (instruction in currInstruction.prerequisitesOf) {
                // all prerequisites have been finished and not already added to available
                if (instruction.prerequisites.all { instructionDoneTime[it.id] != null } &&
                        !availableInstructions.contains(instruction)) {
                    availableInstructions.add(instruction)
                }
            }
        }

        val maxFinishTime = workersFinish.max()!!
        for (i in 0 until workerCount) workersFinish[i] = maxFinishTime

        return workersFinish.max().toString()
    }

    data class Instruction(val id: Char) {
        val prerequisites: MutableList<Instruction> = mutableListOf()
        val prerequisitesOf: MutableList<Instruction> = mutableListOf()
        val takesTime = id - 'A' + 61
    }

}
