package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*

class Day10: Day(10, 36, 81) {
    override fun part1(input: String): Long {
        val grid = input.toGrid()
        val starts = grid.findPositions { it == 0 }
        return grid.find(starts, 9).sum().toLong()
    }

    override fun part2(input: String): Long {
        val grid = input.toGrid()
        val starts = grid.findPositions {it == 0}
        return grid.rating(starts, 9).sum()
    }

    private fun String.toGrid(): Grid<Int> {
        val data = lines().map { line -> line.map { c -> c.digitToInt() } }
        return Grid(data)
    }

    private fun Grid<Int>.find(start: Vector2i, targetValue: Int): Set<Vector2i> =
        findTargets(start, targetValue) { _, _ -> }

    private fun Grid<Int>.findTargets(start: Vector2i, targetValue: Int, action: (position: Vector2i, next: Vector2i) -> Unit): Set<Vector2i> {
        val currentWave = mutableSetOf(start)
        val nextWave = mutableSetOf<Vector2i>()
        while(true) {
            currentWave.forEach { position ->
                val n = getNeighbours(position) { next -> get(next) == get(position) + 1 }
                n.forEach { next ->
                    action(position, next)
                }
                nextWave.addAll(n)
            }
            if (nextWave.isEmpty() || get(nextWave.first()) == targetValue) {
                return nextWave
            }
            currentWave.clear()
            currentWave.addAll(nextWave)
            nextWave.clear()
        }
    }

    private fun Grid<Int>.find(starts: Set<Vector2i>, targetValue: Int): List<Int> =
        starts.map { find(it, targetValue).size }

    private fun Grid<Int>.rating(start: Vector2i, targetValue: Int): Long {
        val ways = mutableMapOf<Vector2i, Long>(start to 1)
        return findTargets(start, targetValue) { position, next ->
            val length = ways.getOrPut(next) { 0 } + ways.getOrDefault(position, 1)
            ways[next] = length
        }.sumOf { ways.getOrDefault(it, 0) }
    }

    private fun Grid<Int>.rating(starts: Set<Vector2i>, targetValue: Int): List<Long> =
        starts.map { rating(it, targetValue) }
}
