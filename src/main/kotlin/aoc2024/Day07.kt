package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day07: Day(7, 3749, 11387) {
    override fun part1(input: String): Long = getInput(input).sumOf { operation ->
        if (calc(operation.first, operation.second, false)) {
            operation.first
        } else {
            0
        }
    }

    override fun part2(input: String): Long = getInput(input).sumOf { operation ->
        if (calc(operation.first, operation.second, true)) {
            operation.first
        } else {
            0
        }
    }

    private fun calc(solution: Long, numbers: List<Long>, withConcat: Boolean): Boolean = calc(solution, numbers, 0, 0, withConcat)

    private fun calc(solution: Long, numbers: List<Long>, currentResult: Long, currentIndex: Int, withConcat: Boolean): Boolean {
        if (currentIndex >= numbers.size) {
            return solution == currentResult
        }
        val newResult1 = currentResult + numbers[currentIndex]
        val newResult2 = currentResult * numbers[currentIndex]
        if (calc(solution, numbers, newResult1, currentIndex + 1, withConcat) || calc(solution, numbers, newResult2, currentIndex + 1, withConcat)) {
            return true
        }
        if (!withConcat) {
            return false
        }
        val newResult3 = "${currentResult}${numbers[currentIndex]}".toLong()
        return calc(solution, numbers, newResult3, currentIndex + 1, true)
    }

    private fun getInput(input: String): List<Pair<Long, List<Long>>> = input.lines().map { line ->
        val (solutionString, numbersString) = line.split(": ")
        val solution = solutionString.toLong()
        val numbers = numbersString.split(" ").map { it.toLong() }
        solution to numbers
    }
}