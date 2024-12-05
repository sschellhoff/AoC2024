package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day05: Day(5, 143, 123) {
    override fun part1(input: String): Long {
        val (orderings, updates) = parseInput(input)
        return updates.sumOf {
            if (it.getSorted(orderings) == it)
                it.getMiddleElement()
            else
                0
        }
    }

    override fun part2(input: String): Long {
        val (orderings, updates) = parseInput(input)
        return updates.sumOf {
            if (it.getSorted(orderings) == it)
                0
            else
                it.getSorted(orderings).getMiddleElement()
        }
    }

    private fun List<Long>.getSorted(orderings: Map<Long, Set<Long>>):  List<Long> {
        return sortedWith(object : Comparator<Long> {
            override fun compare(v1: Long, v2: Long): Int {
                if (v1 == v2) return 0
                if (orderings[v1]?.contains(v2) == true) return -1
                return 1
            }
        })
    }

    private fun parseInput(input: String): Pair<Map<Long, Set<Long>>, List<List<Long>>> {
        val (fst, snd) = input.blocks()
        val orderings = fst.buildOrderings()
        val updates = snd.lines().map { line -> line.split(",").map { it.toLong() } }
        return orderings to updates
    }

    private fun String.buildOrderings(): Map<Long, Set<Long>> {
        val orderings = mutableMapOf<Long, Set<Long>>()
        lines().map { it.split("|") }.forEach {
            val (key, value) = it
            orderings[key.toLong()] = orderings.getOrDefault(key.toLong(), emptySet()).plus(value.toLong())
        }
        return orderings
    }

    private fun List<Long>.getMiddleElement(): Long = this[(this.size / 2)]

    private fun String.blocks(): List<String> = this.split("\n\n")
}