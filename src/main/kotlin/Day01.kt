package de.sschellhoff

import kotlin.math.abs

class Day01 {
    fun run () {
        println("Day 1")
        println("part 1: ${part1()}")
        println("part 2: ${part2()}")
        println()
    }

    private fun part1(): Int {
        val (a, b) = getInput("01").toLists()
        return a.sorted().zip(b.sorted()).sumOf { (a, b) -> abs(b - a) }
    }

    private fun part2(): Int {
        val (a, b) = getInput("01").toLists()
        return a.sumOf { elem -> b.count { it == elem } * elem }
    }

    private fun String.toLists() = lines().map { line ->
        val (a, b) = line.split("\\s+".toRegex()).map { it.toInt() }
        a to b
    }.unzip()
}
