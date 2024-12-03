package de.sschellhoff

class Day03 {
    fun run() {
        println("Day 3")
        println("part 1: ${part1()}")
        println("part 2: ${part2()}")
        println()
    }

    private val mulRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    private val instructionRegex = """do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\)""".toRegex()

    private fun part1(): Long {
        return mulRegex.findAll(getInput("03")).map { a -> a.getMul() }.sum()
    }

    private fun part2(): Long {
        var count = true
        return instructionRegex.findAll(getInput("03")).map {
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