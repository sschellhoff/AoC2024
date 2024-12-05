package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import kotlin.math.abs

class Day01: Day(1, 11, 31) {
    override fun part1(input: String): Long {
        val (a, b) = input.toLists()
        return a.sorted().zip(b.sorted()).sumOf { (a, b) -> abs(b - a) }
    }

    override fun part2(input: String): Long {
        val (a, b) = input.toLists()
        return a.sumOf { elem -> b.count { it == elem } * elem }
    }

    private fun String.toLists() = lines().map { line ->
        val (a, b) = line.split("\\s+".toRegex()).map { it.toLong() }
        a to b
    }.unzip()
}
