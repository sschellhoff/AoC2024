package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day04: Day(4, 18, 9) {
    override fun part1(input: String): Long {
        val grid = input.lines()
        var count = 0L
        grid.forEachIndexed { y, line ->
            line.indices.forEach { x ->
                Direction.entries.forEach { direction ->
                    if (grid.getWord(x, y, 4, direction) in listOf("XMAS", "SAMX")) {
                        count += 1
                    }
                }
            }
        }
        return count
    }

    override fun part2(input: String): Long {
        val grid = input.lines()
        var count = 0L
        grid.forEachIndexed { y, line ->
            line.indices.forEach { x ->
                val w1 = grid.getWord(x, y, 3, Direction.DIAGONAL_RIGHT_DOWN)
                val w2 = grid.getWord(x + 2, y, 3, Direction.DIAGONAL_LEFT_DOWN)
                if (w1 in listOf("MAS", "SAM") && w2 in listOf("MAS", "SAM")) {
                    count += 1
                }
            }
        }
        return count
    }

    private fun List<String>.getWord(x: Int, y: Int, length: Int, direction: Direction): String = try {
        when (direction) {
            Direction.RIGHT -> (0 until length).joinToString("") { "${this[y][x + it]}" }
            Direction.DOWN -> (0 until length).joinToString("") { "${this[y + it][x]}" }
            Direction.DIAGONAL_RIGHT_DOWN -> (0 until length).joinToString("") { "${this[y + it][x + it]}" }
            Direction.DIAGONAL_LEFT_DOWN -> (0 until length).joinToString("") { "${this[y + it][x - it]}" }
        }
    } catch (e: IndexOutOfBoundsException) {
        ""
    }

    enum class Direction {
        RIGHT, DOWN, DIAGONAL_RIGHT_DOWN, DIAGONAL_LEFT_DOWN
    }
}