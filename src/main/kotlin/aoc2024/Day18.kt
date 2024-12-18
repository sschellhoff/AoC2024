package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.Grid
import de.sschellhoff.utils.Vector2i
import de.sschellhoff.utils.pathfinding.EdgeInfo
import de.sschellhoff.utils.pathfinding.dijkstra

class Day18 : Day(18, 22, 1) {
    override fun part1(input: String): Long {
        val (targetNode, grid) = input.parseInput()
        val path = dijkstra(Vector2i(0, 0), { node -> node == targetNode }) { position ->
            grid.getNeighbours(position).filter { grid.get(it) == Tile.Empty }.map { EdgeInfo(position, it, 1) }
        }
        return path!!.second
    }

    override fun part2(input: String): Long {
        val (targetNode, grid, points) = input.parseInput()
        var position = points.findFirstNotInGrid(grid)
        println(position)
        while (true) {
            val nextPoint = points[position]
            grid.set(nextPoint, Tile.Wall)
            val path = dijkstra(Vector2i(0, 0), { node -> node == targetNode }) { position ->
                grid.getNeighbours(position).filter { grid.get(it) == Tile.Empty }.map { EdgeInfo(position, it, 1) }
            }
            if (path == null) {
                println(nextPoint)
                return 1
            }
            position += 1
        }

        TODO("Not yet implemented")
    }

    enum class Tile {
        Empty,
        Wall
    }

    private fun List<Vector2i>.findFirstNotInGrid(grid: Grid<Tile>): Int {
        this.forEachIndexed { index, vector2i ->
            if (grid.get(vector2i) == Tile.Empty) {
                return index
            }
        }
        return -1
    }

    private fun String.parseInput(): Triple<Vector2i, Grid<Tile>, List<Vector2i>> {
        val points = toPositions()
        return if (points.size < 30) {
            val targetNode = Vector2i(6, 6)
            val grid = points.take(12).toSet().toGrid(7, 7)
            Triple(targetNode, grid, points)
        } else {
            val grid = points.take(1024).toSet().toGrid(71, 71)
            val targetNode = Vector2i(70, 70)
            Triple(targetNode, grid, points)
        }
    }

    private fun String.toPositions(): List<Vector2i> = lines().map {
        it.split(",").let { (x, y) ->
            Vector2i(x.toInt(), y.toInt())
        }
    }

    private fun Set<Vector2i>.toGrid(width: Int, height: Int): Grid<Tile> {
        return Grid.initialize(width = width, height = height) { x, y ->
            if (contains(Vector2i(x, y))) {
                Tile.Wall
            } else {
                Tile.Empty
            }
        }
    }
}

fun main() {
    Day18().run(Day.RunMode.BOTH)
}