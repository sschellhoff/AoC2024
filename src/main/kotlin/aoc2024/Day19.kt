package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.blockLines

class Day19: Day(19, 6, 16) {
    override fun part1(input: String): Long {
        val (towels, designs) = input.parseInput()
        val memoization = mutableMapOf<String, Long>()
        return designs.count { findPatterns3(it, towels, memoization) != 0L }.toLong()
    }

    override fun part2(input: String): Long {
        val (towels, designs) = input.parseInput()
        val memoization = mutableMapOf<String, Long>()
        return designs.sumOf { findPatterns3(it, towels, memoization) }
    }

    private fun findPatterns3(design: String, towels: List<String>, memoization: MutableMap<String, Long>): Long {
        val memo = memoization[design]
        if (memo != null) {
            return memo
        }

        var result = 0L
        towels.forEach { towel ->
            if (design.startsWith(towel)) {
                result += if (design == towel) 1 else findPatterns3(design.drop(towel.length), towels, memoization)
            }
        }
        memoization[design] = result
        return result
    }

    private fun String.parseInput(): Pair<List<String>, List<String>> {
        val (towels, designs) = blockLines()

        return towels.first().split(", ") to designs
    }
}

fun main () {
    Day19().run(Day.RunMode.BOTH)
}