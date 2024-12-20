package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*
import de.sschellhoff.utils.pathfinding.EdgeInfo
import de.sschellhoff.utils.pathfinding.aStar

class Day20: Day(20, 0, 0) {
    override fun part1(input: String): Long {
        val path = input.pathFromInput()
        return countWaysToCheat(path, cheatLength = 2, minPicosecondsToSave = 100)
    }

    override fun part2(input: String): Long {
        val path = input.pathFromInput()
        return countWaysToCheat(path, minPicosecondsToSave = 100, cheatLength = 20)
    }

    private fun countWaysToCheat(path: List<Vector2i>, minPicosecondsToSave: Int = 100, cheatLength: Int): Long {
        var counter = 0L
        val visited = mutableSetOf<Pair<Vector2i, Vector2i>>()
        path.forEachIndexed { index, position ->
            ((index + minPicosecondsToSave + 1)..<path.size).forEach { index2 ->
                val position2 = path[index2]
                if (position.manhattanDistance(position2) <= cheatLength) {
                    val oldLength = index2 - index
                    if (oldLength >= minPicosecondsToSave + position.manhattanDistance(position2)) {
                        if (visited.add(position to position2)) {
                            counter += 1
                        }
                    }
                }
            }
        }
        return counter
    }

    enum class Tile {
        Wall,
        Free
    }

    private fun String.pathFromInput(): List<Vector2i> {
        val (grid, start, isEnd, heuristic) = parse()
        val (path, _) = aStar(start, isEnd, heuristic) { from ->
            grid.getNeighbours(from) { next -> grid.get(next) != Tile.Wall }.map { next -> EdgeInfo(from, next, 1L) }
        } ?: throw IllegalStateException()
        return path
    }

    private fun String.parse(): NTuple4<Grid<Tile>, Vector2i, (Vector2i) -> Boolean, (Vector2i) -> Long> {
        val grid = Grid.fromString(this, ::toNode)
        val (start, end) = this.findStartAndEndInCharGrid(cStart = 'S', cEnd = 'E')
        val isEnd = { n: Vector2i -> n == end }
        val heuristic = { n: Vector2i -> n.manhattanDistance(end).toLong() }
        return grid then start then isEnd then heuristic
    }

    private fun toNode(char: Char): Tile = when (char) {
        '#' -> Tile.Wall
        '.', 'S', 'E' -> Tile.Free
        else -> throw IllegalArgumentException(char.toString())
    }
}