package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day03: Day(3, 161, 48, "", "_2") {
    private val mulRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    private val instructionRegex = """do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\)""".toRegex()

    override fun part1(input: String): Long {
        return mulRegex.findAll(input).map { a -> a.getMul() }.sum()
    }

    override fun part2(input: String): Long {
        var count = true
        return instructionRegex.findAll(input).map {
            when (it.value) {
                "do()" -> {
                    count = true
                    0L                }
                "don't()" -> {
                    count = false
                    0L
                }
                else -> {
                    if (count) {
                        it.getMul()
                    } else {
                        0L
                    }
                }
            }
        }.sum()
    }

    private fun MatchResult.getMul(): Long {
        return value.drop(4).dropLast(1).split(",").let { (a, b) -> a.toLong() * b.toLong() }
    }
}