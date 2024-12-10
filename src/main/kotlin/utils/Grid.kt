package de.sschellhoff.utils

import java.util.function.Predicate

fun String.getGridSize(): Pair<Long, Long> = lines().first().length.toLong() to lines().size.toLong()

data class Grid<T>(private val data: List<List<T>>) {
    val width: Int = data.first().size
    val height: Int = data.size

    fun forEach(action: (c: T) -> Unit) {
        data.forEach { line ->
            line.forEach { c ->
                action(c)
            }
        }
    }

    fun forEachIndexed(action: (x: Int, y: Int, c: T) -> Unit) {
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                action(x, y, c)
            }
        }
    }

    fun inBounds(position: Vector2i): Boolean =
        position.x in 0..<width && position.y in 0..<height

    fun findPositions(predicate: Predicate<T>): Set<Vector2i> {
        val result = mutableSetOf<Vector2i>()
        forEachIndexed { x, y, c ->
            if (predicate.test(c)) {
                result.add(Vector2i(x, y))
            }
        }
        return result
    }

    fun get(position: Vector2i): T {
        return data[position.y][position.x]
    }

    fun getNeighbours(position: Vector2i, predicate: Predicate<Vector2i> = Predicate { true }): List<Vector2i> {
        if(!inBounds(position)) {
            return emptyList()
        }

        return Direction.entries.map { direction ->
            position.move(direction)
        }.filter { inBounds(it) && predicate.test(it) }
    }
}