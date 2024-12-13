package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.blockLines

class Day13 : Day(13, 480, 875318608908) {
    override fun part1(input: String): Long {
        val eqs = input.buildEquations()
        val solutions = eqs.mapNotNull { it.solve() }
        return solutions.filter { it.first <= 100 && it.second <= 100 }.sumOf { costPart1(it) }
    }

    override fun part2(input: String): Long {
        val eqs = input.buildEquations(10000000000000L)
        val solutions = eqs.mapNotNull { it.solve() }
        return solutions.sumOf { costPart1(it) }
    }

    private fun costPart1(xs: Pair<Long, Long>) = 3 * xs.first + xs.second

    data class Equation(val a: Long, val b: Long, val c: Long) {
        operator fun plus(other: Equation) = Equation(a = a + other.a, b = b + other.b, c = c + other.c)
    }

    data class Equations(val first: Equation, val second: Equation) {
        private fun detA(): Long = first.a * second.b - second.a * first.b
        private fun detA1(): Long = first.c * second.b - second.c * first.b
        private fun detA2(): Long = first.a * second.c - second.a * first.c

        fun solve(): Pair<Long, Long>? {
            val det = detA()
            if (det == 0L) {
                return null
            }
            val detX = detA1()
            val detY = detA2()
            if(detX % det != 0L) return null
            if(detY % det != 0L) return null
            return (detX / det) to (detY / det)
        }
    }

    private fun String.buildEquations(solutionSummand: Long = 0): List<Equations> = blockLines().map { block ->
        val (a, b, c) = block.map {
            val r = """\d+""".toRegex()
            val (a, b) = r.findAll(it).toList()
            a.value.toLong() to b.value.toLong()
        }
        Equations(Equation(a.first, b.first, c.first + solutionSummand), Equation(a.second, b.second, c.second + solutionSummand))
    }
}