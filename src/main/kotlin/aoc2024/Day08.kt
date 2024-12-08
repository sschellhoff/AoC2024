package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.Vector2
import de.sschellhoff.utils.getGridSize

class Day08: Day(8, 14, 34) {
    override fun part1(input: String): Long {
        val antiNodes = mutableSetOf<Vector2>()
        val antennasPerFrequency = input.getAntennasPerFrequency()
        val gridSize = input.getGridSize()

        antennasPerFrequency.forEachPair { antenna, antenna2 ->
            addNextAntiNode(antenna2, antenna, gridSize, antiNodes)
            addNextAntiNode(antenna, antenna2, gridSize, antiNodes)
        }

        return antiNodes.size.toLong()
    }

    private fun addNextAntiNode(
        antenna2: Vector2,
        antenna: Vector2,
        gridSize: Pair<Long, Long>,
        antiNodes: MutableSet<Vector2>
    ) {
        val antiNode = antenna2 + (antenna distanceVectorTo antenna2)
        if (antiNode.isInBounds(gridSize)) {
            antiNodes.add(antiNode)
        }
    }

    override fun part2(input: String): Long {
        val antiNodes = mutableSetOf<Vector2>()
        val antennasPerFrequency = input.getAntennasPerFrequency()
        val gridSize = input.getGridSize()

        antennasPerFrequency.forEachPair { antenna, antenna2 ->
            addAntiNodesInDirection(antenna, antenna2, gridSize, antiNodes)
            addAntiNodesInDirection(antenna2, antenna, gridSize, antiNodes)
        }

        antennasPerFrequency.values.forEach { antennas ->
            antennas.forEach { antenna ->
                antiNodes.add(antenna)
            }
        }

        return antiNodes.size.toLong()
    }

    private fun addAntiNodesInDirection(
        antenna: Vector2,
        antenna2: Vector2,
        gridSize: Pair<Long, Long>,
        antiNodes: MutableSet<Vector2>
    ) {
        val distance = (antenna distanceVectorTo antenna2)
        var antiNode = antenna2 + distance
        while (antiNode.isInBounds(gridSize)) {
            antiNodes.add(antiNode)
            antiNode += distance
        }
    }

    private fun String.getAntennasPerFrequency(): Map<Char, Set<Vector2>> {
        val result = mutableMapOf<Char, MutableSet<Vector2>>()

        lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, a ->
                if (a != '.') {
                    result.getOrPut(a) { mutableSetOf() }.add(Vector2(x.toLong(), y.toLong()))
                }
            }
        }

        return result
    }

    private fun Map<Char, Set<Vector2>>.forEachPair(action: (a1: Vector2, a2: Vector2) -> Unit) {
        this.values.forEach { antennas ->
            antennas.forEachIndexed { i, antenna ->
                antennas.drop(i+1).forEach { antenna2 ->
                    action(antenna, antenna2)
                }
            }
        }
    }

    private fun Vector2.isInBounds(gridSize: Pair<Long, Long>): Boolean =
        this.x >= 0 && this.y >= 0 && this.x < gridSize.first && this.y < gridSize.second
}