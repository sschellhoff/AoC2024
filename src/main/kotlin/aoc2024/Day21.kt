package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*

class Day21 : Day(21, 126384, 154115708116294) {

    override fun part1(input: String): Long {
        val (terminal, codes) = input.getProblem()
        val pathsOnKeypads = terminal.findPathsBetween("0123456789A", terminal.findPathsBetween("<>^va"))

        return codes.sumOf { code ->
            find(code, 3, pathsOnKeypads, 'A', mutableMapOf(), mutableMapOf()) * code.takeWhile { it.isDigit() }.toLong()
        }
    }

    override fun part2(input: String): Long {
        val (terminal, codes) = input.getProblem()
        val pathsOnKeypads = terminal.findPathsBetween("0123456789A", terminal.findPathsBetween("<>^va"))

        return codes.sumOf { code ->
            find(code, 26, pathsOnKeypads, 'A', mutableMapOf(), mutableMapOf()) * code.takeWhile { it.isDigit() }.toLong()
        }
    }

    private fun find(
        input: String,
        depth: Int,
        pathsOnKeypads: Map<Pair<Char, Char>, List<String>>,
        acceptKey: Char,
        memoizationBetweenTwoPoints: MutableMap<Triple<Char, Char, Int>, Long>,
        memoizationForPaths: MutableMap<Pair<String, Int>, Long>
    ): Long {
        val memoKey = input to depth
        val memo = memoizationForPaths[memoKey]
        if (memo != null) {
            return memo
        }
        val length = "$acceptKey$input".zipWithNext { a, b ->
            nestedFind(a, b, depth, pathsOnKeypads, memoizationBetweenTwoPoints, memoizationForPaths)
        }.sum()
        memoizationForPaths[memoKey] = length
        return length
    }

    private fun nestedFind(
        from: Char,
        to: Char,
        depth: Int,
        pathsOnKeypads: Map<Pair<Char, Char>, List<String>>,
        memoizationBetweenToPoints: MutableMap<Triple<Char, Char, Int>, Long>,
        memoizationForPaths: MutableMap<Pair<String, Int>, Long>
    ): Long {
        val memoKey = Triple(from, to, depth)
        val memo = memoizationBetweenToPoints[memoKey]
        if (memo != null) {
            return memo
        }
        if (depth == 0) {
            memoizationBetweenToPoints[memoKey] = 1
            return 1
        }

        var bestLength = Long.MAX_VALUE
        pathsOnKeypads[from to to]!!.forEach { path ->
            val newLength = find("${path}a", depth - 1, pathsOnKeypads, 'a', memoizationBetweenToPoints, memoizationForPaths)
            if (newLength < bestLength) {
                bestLength = newLength
            }
        }
        memoizationBetweenToPoints[memoKey] = bestLength
        return bestLength
    }

    private fun Grid<Char>.findPathsBetween(
        buttons: String,
        paths: MutableMap<Pair<Char, Char>, List<String>> = mutableMapOf()
    ): MutableMap<Pair<Char, Char>, List<String>> {
        buttons.indices.forEach { from ->
            val start = this.findPositions { it == buttons[from] }
            check(start.size == 1)
            buttons.indices.forEach { to ->
                val end = this.findPositions { it == buttons[to] }
                check(end.size == 1)
                val startToEnd = start.first() - end.first()
                val endToStart = end.first() - start.first()
                paths[buttons[from] to buttons[to]] =
                    startToEnd.toPaths().withoutInvalidPaths(start.first(), end.first())
                paths[buttons[to] to buttons[from]] =
                    endToStart.toPaths().withoutInvalidPaths(end.first(), start.first())
            }
        }
        return paths
    }

    private fun List<String>.withoutInvalidPaths(start: Vector2i, end: Vector2i): List<String> =
        this.filter { it.isValidPath(start, end) }

    private fun String.isValidPath(start: Vector2i, end: Vector2i): Boolean {
        var currentSpot = start
        indices.forEach { i ->
            currentSpot = currentSpot.move(
                when (this[i]) {
                    '<' -> Direction.WEST
                    '>' -> Direction.EAST
                    '^' -> Direction.NORTH
                    'v' -> Direction.SOUTH
                    else -> throw IllegalArgumentException(this)
                }
            )
            if (currentSpot.x == 0 && currentSpot.y in 3..4) {
                return false
            }
        }
        return true
    }

    private fun Vector2i.toPaths(): List<String> {
        val horizontal = if (x > 0) "<".repeat(x) else ">".repeat(-x)
        val vertical = if (y > 0) "^".repeat(y) else "v".repeat(-y)
        return listOf(horizontal + vertical, vertical + horizontal).toSet().toList()
    }

    private fun Grid<Char>.print() {
        var lastLine = 0
        this.forEachIndexedI { _, y, c ->
            if (lastLine != y) {
                println()
            }
            print(c)
            lastLine = y
        }
        println()
    }


    private fun String.getProblem(): Pair<Grid<Char>, List<String>> = Grid.fromString(
        """
            789
            456
            123
            .0A
            .^a
            <v>
           """.trimIndent()
    ) { it } to lines()
}

fun main() {
    Day21().run(Day.RunMode.BOTH)
}