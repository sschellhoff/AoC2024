package de.sschellhoff.utils

import de.sschellhoff.aoc2024.Direction

data class Vector2(val x: Long, val y: Long) {
    operator fun plus(other: Vector2): Vector2 = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Vector2): Vector2 = Vector2(x - other.x, y - other.y)

    infix fun distanceVectorTo(other: Vector2): Vector2 = other - this

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