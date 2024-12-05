package de.sschellhoff.utils

import de.sschellhoff.getInput

open class Day(private val day: Int, private val testResultPart1: Long? = null, private val testResultPart2: Long? = null) {
    enum class RunMode {
        REAL, TEST, BOTH
    }
    enum class Part(val toInt: Int) {
        One(1), Two(2)
    }
    fun run(runMode: RunMode = RunMode.REAL) {
        val dayAsString = day.toString().padStart(2, '0')

        println("Day $day")
        runPart(runMode, dayAsString, Part.One)
        runPart(runMode, dayAsString, Part.Two)
        println()
    }

    private fun runPart(runMode: RunMode, dayAsString: String, part: Part) {
        if (runMode == RunMode.TEST || runMode == RunMode.BOTH) {
            val testInput = getInput(dayAsString, true)
            val testResult = if (part == Part.One) part1(testInput) else part2(testInput)
            printResult(testResult, if (part == Part.One) testResultPart1 else testResultPart2, part.toInt, true)
        }

        if (runMode == RunMode.REAL || runMode == RunMode.BOTH) {
            val realInput = getInput(dayAsString)
            val realResult = if (part == Part.One) part1(realInput) else part2(realInput)
            printResult(realResult, null, part.toInt, false)
        }
    }

    private fun printResult(result: Long, testAgainst: Long?, part: Int, isTest: Boolean) {
        val greenColor = "\u001b[32m"
        val redColor = "\u001b[31m"
        val resetColor = "\u001b[0m"
        val printColor = when {
            testAgainst == null -> resetColor
            result == testAgainst -> greenColor
            else -> redColor
        }
        println("part $part${if (isTest) " (test)" else ""}: $printColor$result$resetColor")
    }

    protected open fun part1(input: String): Long {
        return 0
    }

    protected open fun part2(input: String): Long {
        return 0
    }

}