package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*
import java.lang.Long.max

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

    private fun Grid<Int>.find(start: Vector2i, targetValue: Int): Set<Vector2i> {
        val l = mutableSetOf(start)
        val r = mutableSetOf<Vector2i>()
        while(true) {
            l.forEach { position ->
                val n = getNeighbours(position) { next -> get(next) == get(position) + 1 }
                r.addAll(n)
            }
            if (r.isEmpty() || get(r.first()) == targetValue) {
                return r
            }
            l.clear()
            l.addAll(r)
            r.clear()
        }
    }

    private fun Grid<Int>.find(starts: Set<Vector2i>, targetValue: Int): List<Int> =
        starts.map { find(it, targetValue).size }

    private fun Grid<Int>.rating(start: Vector2i, targetValue: Int): Long {
        val predecessors = mutableMapOf<Vector2i, MutableSet<Vector2i>>()
        val ways = mutableMapOf<Vector2i, Long>(start to 1)
        var maxRating = 0L
        val l = mutableSetOf(start)
        val r = mutableSetOf<Vector2i>()
        while(true) {
            l.forEach { position ->
                val n = getNeighbours(position) { next -> get(next) == get(position) + 1 }
                n.forEach {
                    predecessors.getOrPut(it) { mutableSetOf() }.add(position)
                    val length = ways.getOrPut(it) { 0 } + ways.getOrDefault(position, 1)
                    ways[it] = length
                }
                r.addAll(n)
            }
            maxRating = max(maxRating, r.size.toLong())
            if (r.isEmpty() || get(r.first()) == targetValue) {
                return r.sumOf { ways.getOrDefault(it, 0) }
            }
            l.clear()
            l.addAll(r)
            r.clear()
        }
    }

    private fun Grid<Int>.rating(starts: Set<Vector2i>, targetValue: Int): List<Long> {
        return starts.map { rating(it, targetValue) }
    }
}
