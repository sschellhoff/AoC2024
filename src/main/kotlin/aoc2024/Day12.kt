package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*

class Day12 : Day(12, 1930, 1206) {
    override fun part1(input: String): Long {
        val grid = Grid.fromString(input) { it }
        val prices = mutableListOf<Long>()
        grid.forDistinctAreas { areaPieces, edgeInfo ->
            prices.add(areaPieces.size * edgeInfo.sumOf { 4 - it.second }.toLong())
        }
        return prices.sum()
    }

    override fun part2(input: String): Long {
        val grid = Grid.fromString(input) { it }
        val costs = mutableListOf<Long>()
        grid.forDistinctAreas { areaPieces, edgeInfo ->
            val edgePieces = edgeInfo.map { it.first }
            val area = areaPieces.size
            val numberOfHorizontalEdges = calculatePerimeterForEdgeDirection(
                edgePieces,
                areaPieces,
                listOf(Direction.EAST, Direction.WEST)
            ) { it.x }
            val numberOfVerticalEdges = calculatePerimeterForEdgeDirection(
                edgePieces,
                areaPieces,
                listOf(Direction.NORTH, Direction.SOUTH)
            ) { it.y }
            val perimeter = numberOfHorizontalEdges + numberOfVerticalEdges

            costs.add(perimeter * area.toLong())
        }
        return costs.sum()
    }

    private fun calculatePerimeterForEdgeDirection(
        edgePieces: List<Vector2i>,
        areaPieces: Set<Vector2i>,
        directions: List<Direction>,
        selectComponent: (position: Vector2i) -> Int,
    ): Int {
        var perimeter = 0
        val groupedByComponent = edgePieces.groupBy(selectComponent).map { it.value }
        groupedByComponent.forEach { group ->
            directions.forEach { direction ->
                perimeter += getRangesForSite(group, areaPieces, direction).size
            }
        }
        return perimeter
    }

    private fun getRangesForSite(
        group: List<Vector2i>,
        areaPieces: Set<Vector2i>,
        direction: Direction
    ): List<IntRange> {
        val selectComponent = when (direction) {
            Direction.NORTH, Direction.SOUTH -> { vec: Vector2i -> vec.x }
            Direction.WEST, Direction.EAST -> { vec: Vector2i -> vec.y }
        }
        val edgeNorth = group.filter { !areaPieces.contains(it.move(direction)) }
        val northRanges = edgeNorth.toContinuousRanges(selectComponent)
        return northRanges
    }

    private fun List<Vector2i>.toContinuousRanges(selectComponent: (vector: Vector2i) -> Int): List<IntRange> =
        map(selectComponent).toContinuousRanges()

    private fun List<Int>.toContinuousRanges(): List<IntRange> {
        val selfSorted = sorted()
        if (isEmpty()) {
            return emptyList()
        }
        val result = mutableListOf<IntRange>()
        var start = selfSorted.first()
        var end = selfSorted.first()
        selfSorted.zipWithNext { a, b ->
            if (a + 1 == b) {
                end = b
            } else {
                result.add(start..end)
                start = b
                end = b
            }
        }
        if (result.isEmpty() || result.last().last != end) {
            result.add(start..end)
        }
        return result
    }
}
