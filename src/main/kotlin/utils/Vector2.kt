package de.sschellhoff.utils

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

data class Vector2i(val x: Int, val y: Int) {
    operator fun plus(other: Vector2i) = Vector2i(x + other.x, y + other.y)
    operator fun minus(other: Vector2i) = Vector2i(x - other.x, y - other.y)

    infix fun distanceVectorTo(other: Vector2i) = other - this

    companion object {
        val Zero = Vector2i(0, 0)
        val Up = Vector2i(0, -1)
        val Down = Vector2i(0, 1)
        val Left = Vector2i(-1, 0)
        val Right = Vector2i(1, 0)
    }
}

fun Vector2i.move(direction: Direction): Vector2i = when (direction) {
    Direction.NORTH -> Vector2i(x, y) + Vector2i.Up
    Direction.EAST -> Vector2i(x, y) + Vector2i.Right
    Direction.SOUTH -> Vector2i(x, y) + Vector2i.Down
    Direction.WEST -> Vector2i(x, y) + Vector2i.Left
}
