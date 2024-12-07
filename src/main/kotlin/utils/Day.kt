package de.sschellhoff.utils

import de.sschellhoff.getInput
import kotlin.time.measureTimedValue

open class Day(private val day: Int, private val testResultPart1: Long? = null, private val testResultPart2: Long? = null, private val testInputSuffixPart1: String = "", private val testInputSuffixPart2: String = "") {
    enum class RunMode {
        REAL, TEST, BOTH
    }
    enum class Part(val toInt: Int) {
        One(1), Two(2)
    }
    fun run(runMode: RunMode = RunMode.REAL) {
        val dayAsString = day.toString().padStart(2, '0')

        val resetColor = "\u001b[0m"
        val bold = "\u001b[1m"
        println("$bold--==:: Day $day ::==--$resetColor")
        runPart(runMode, dayAsString, Part.One)
        println("--------------------")
        runPart(runMode, dayAsString, Part.Two)
        println()
    }

    private fun runPart(runMode: RunMode, dayAsString: String, part: Part) {
        val yellowColor = "\u001B[33m"
        val resetColor = "\u001b[0m"
        if (runMode == RunMode.TEST || runMode == RunMode.BOTH) {
            val testInput = getInput(dayAsString, true, if (part == Part.One) testInputSuffixPart1 else testInputSuffixPart2)
            val (testResult, duration) = measureTimedValue {
                if (part == Part.One) part1(testInput) else part2(testInput)
            }
            printResult(testResult, if (part == Part.One) testResultPart1 else testResultPart2, part.toInt, true)
            println("$yellowColor$duration$resetColor")
        }

        if (runMode == RunMode.REAL || runMode == RunMode.BOTH) {
            val realInput = getInput(dayAsString)
            val (realResult, duration) = measureTimedValue {
                if (part == Part.One) part1(realInput) else part2(realInput)
            }
            printResult(realResult, null, part.toInt, false)
            println("$yellowColor$duration$resetColor")
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