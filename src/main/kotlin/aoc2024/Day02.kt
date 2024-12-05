package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import java.util.function.Predicate

class Day02 : Day(2, 2, 4) {
    override fun part1(input: String): Long = input.toDay2().count { it.isSafe() }.toLong()

    override fun part2(input: String): Long  = input.toDay2().count { line -> line.withDamping { it.isSafe() } }.toLong()

    private fun String.toDay2(): List<List<Long>> = lines()
            .map { line ->
                line.split(" ")
                    .map {
                        it.toLong()
                    }
            }

    private fun List<Long>.withDamping(predicate: Predicate<List<Long>>): Boolean {
        if (predicate.test(this)) return true
        indices.forEach { i ->
            toMutableList().apply { removeAt(i) }.let {
                if (predicate.test(it)) return true
            }
        }
        return false
    }

    private fun List<Long>.isSafe(): Boolean {
        val pairs = zipWithNext()
        return pairs.all { (a, b) -> a < b && b - a in 1..3 } || pairs.all { (a, b) -> a > b && a - b in 1..3 }
    }
}