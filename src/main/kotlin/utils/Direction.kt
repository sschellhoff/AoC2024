package de.sschellhoff.utils

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