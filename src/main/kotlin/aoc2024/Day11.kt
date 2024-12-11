package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day11 : Day(11, 55312, 65601038650482) {
    override fun part1(input: String): Long {
        return blink(input.initialStones(), 25)
    }

    override fun part2(input: String): Long {
        return blink(input.initialStones(), 75)
    }

    private fun blink(stones: List<Long>, stepsLeft: Int): Long {
        val memory = mutableMapOf<MemiozationKey, Long>()
        return blink(stones, stepsLeft, memory)
    }

    private fun blink(stones: List<Long>, stepsLeft: Int, memory: Memoization): Long =
        stones.sumOf { blink(it, stepsLeft, memory) }

    private fun blink(stone: Long, stepsLeft: Int, memory: Memoization): Long {
        if (stepsLeft == 0) {
            return 1
        }
        val cachedValue = memory[stone to stepsLeft]
        if (cachedValue != null) return cachedValue

        val result = if (stone == 0L) {
            blink(1L, stepsLeft - 1, memory)
        } else if ("$stone".length % 2 == 0) {
            val strStone = "$stone"
            val halfLength = strStone.length / 2
            val stones = listOf(strStone.substring(0..<halfLength).toLong(), strStone.substring(halfLength).toLong())
            blink(stones, stepsLeft - 1, memory)
        } else {
            blink(stone * 2024, stepsLeft - 1, memory)
        }
        check(memory.getOrPut(stone to stepsLeft, { result }) == result) { "Oh nooooo $result not found" }
        return result
    }

    private fun String.initialStones() = split(" ").map { it.toLong() }
}

private typealias MemiozationKey = Pair<Long, Int>
private typealias Memoization = MutableMap<MemiozationKey, Long>
