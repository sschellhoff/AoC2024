package de.sschellhoff

class Day04 {
    fun run() {
        println("Day 4")
        println("part 1: ${part1()}")
        println("part 2: ${part2()}")
        println()
    }

    private fun part1(): Long {
        val grid = getInput("04").lines()
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

    private fun part2(): Long {
        val grid = getInput("04").lines()
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