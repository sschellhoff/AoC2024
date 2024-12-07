package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day06 : Day(6, 41, 6) {
    override fun part1(input: String): Long {
        val problem = Problem.from(input)
        return problem.moveUntilOutOfBounds().second.size.toLong()
    }

    override fun part2(input: String): Long {
        val problem = Problem.from(input)
        var count = 0
        problem.copy().moveUntilOutOfBounds().second.forEach {  (x, y) ->
            val newProblem = problem.copy()
            if (newProblem.lab.add(Vector2(x, y))) {
                val (reason, _) = newProblem.moveUntilOutOfBounds()
                if (reason == Problem.TerminationReason.Loop) {
                    count += 1
                }
                newProblem.lab.remove(Vector2(x, y))
            }
        }
        return count.toLong()
    }

    data class Problem(var position: Vector2, var direction: Direction, val lab: Lab) {
        fun moveUntilOutOfBounds(): Pair<TerminationReason, Set<Vector2>> {
            val positions = mutableSetOf(position)
            val positionsWithDirection = mutableSetOf(position to direction)
            while (true) {
                val nextPosition = position.move(direction)
                if (lab.isObstructed(nextPosition)) {
                    direction = direction.turnRight()
                } else if (lab.isOutOfBounds(nextPosition)) {
                    return TerminationReason.OutOfBounds to positions
                } else if (positionsWithDirection.contains(nextPosition to direction)) {
                    return TerminationReason.Loop to positions
                } else {
                    position = nextPosition
                    positions.add(position)
                    positionsWithDirection.add(position to direction)
                }
            }
        }

        enum class TerminationReason {
            OutOfBounds,
            Loop
        }

        companion object {
            fun from(input: String): Problem {
                val obstructions = mutableSetOf<Vector2>()
                var position = Vector2.Zero
                val direction = Direction.NORTH
                input.lines().forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        when (c) {
                            '#' -> obstructions.add(Vector2(x.toLong(), y.toLong()))
                            '^' -> position = Vector2(x.toLong(), y.toLong())
                        }
                    }
                }
                return Problem(
                    position,
                    direction,
                    Lab(obstructions, input.lines().first().length.toLong(), input.lines().size.toLong())
                )
            }
        }
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}

fun Direction.turnLeft(): Direction {
    return when (this) {
        Direction.NORTH -> Direction.WEST
        Direction.EAST -> Direction.NORTH
        Direction.SOUTH -> Direction.EAST
        Direction.WEST -> Direction.SOUTH
    }
}

fun Direction.turnRight(): Direction {
    return when (this) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
    }
}

fun Direction.turnAround(): Direction {
    return when (this) {
        Direction.NORTH -> Direction.SOUTH
        Direction.EAST -> Direction.WEST
        Direction.SOUTH -> Direction.NORTH
        Direction.WEST -> Direction.EAST
    }
}

data class Vector2(val x: Long, val y: Long) {
    operator fun plus(other: Vector2): Vector2 = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Vector2): Vector2 = Vector2(x - other.x, y - other.y)

    companion object {
        val Zero = Vector2(0, 0)
        val Up = Vector2(0, -1)
        val Down = Vector2(0, 1)
        val Left = Vector2(-1, 0)
        val Right = Vector2(1, 0)
    }
}

fun Vector2.move(direction: Direction): Vector2 = when (direction) {
    Direction.NORTH -> Vector2(x, y) + Vector2.Up
    Direction.EAST -> Vector2(x, y) + Vector2.Right
    Direction.SOUTH -> Vector2(x, y) + Vector2.Down
    Direction.WEST -> Vector2(x, y) + Vector2.Left
}

data class Lab(private val obstructions: MutableSet<Vector2>, val width: Long, val height: Long) {
    fun add(position: Vector2): Boolean = obstructions.add(position)
    fun isObstructed(position: Vector2): Boolean = obstructions.contains(position)
    fun isOutOfBounds(position: Vector2): Boolean =
        position.x < 0 || position.y < 0 || position.x >= width || position.y >= height

    fun remove(vector: Vector2) {
        obstructions.remove(vector)
    }
}