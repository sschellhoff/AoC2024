package de.sschellhoff

import java.util.function.Predicate

class Day02 {
    fun run() {
        println("Day 2")
        println("part 1: ${part1()}")
        println("part 2: ${part2()}")
        println()
    }

    private fun part1(): Int = readDay2().count { it.isSafe() }

    private fun part2(): Int = readDay2().count { line -> line.withDamping { it.isSafe() } }

    private fun readDay2(): List<List<Int>> =
        getInput("02")
            .lines()
            .map { line ->
                line.split(" ")
                    .map {
                        it.toInt()
                    }
            }

    private fun List<Int>.withDamping(predicate: Predicate<List<Int>>): Boolean {
        if (predicate.test(this)) return true
        indices.forEach { i ->
            toMutableList().apply { removeAt(i) }.let {
                if (predicate.test(it)) return true
            }
        }
        return false
    }

    private fun List<Int>.isSafe(): Boolean {
        val pairs = zipWithNext()
        return pairs.all { (a, b) -> a < b && b - a in 1..3 } || pairs.all { (a, b) -> a > b && a - b in 1..3 }
    }
}