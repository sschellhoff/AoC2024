package de.sschellhoff.utils

import java.util.function.Predicate

fun String.getGridSize(): Pair<Long, Long> = lines().first().length.toLong() to lines().size.toLong()

data class Grid<T>(private val data: List<List<T>>): Space2D<T> {
    private val width: Int = data.first().size
    private val height: Int = data.size

    fun forEach(action: (c: T) -> Unit) {
        data.forEach { line ->
            line.forEach { c ->
                action(c)
            }
        }
    }

    override fun forEachIndexedI(action: (x: Int, y: Int, c: T) -> Unit) {
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                action(x, y, c)
            }
        }
    }

    override fun forEachIndexed(action: (x: Long, y: Long, c: T) -> Unit) =
        forEachIndexedI {x: Int, y: Int, c -> action(x.toLong(), y.toLong(), c)}


    private fun inBounds(position: Vector2i): Boolean =
        position.x in 0..<width && position.y in 0..<height

    fun findPositions(predicate: Predicate<T>): Set<Vector2i> {
        val result = mutableSetOf<Vector2i>()
        forEachIndexedI { x: Int, y: Int, c ->
            if (predicate.test(c)) {
                result.add(Vector2i(x, y))
            }
        }
        return result
    }

    override fun get(position: Vector2i): T = data[position.y][position.x]
    override fun get(position: Vector2): T = get(position.toVector2i())

    override fun getNeighbours(position: Vector2i, predicate: Predicate<Vector2i>): List<Vector2i> {
        if(!inBounds(position)) {
            return emptyList()
        }

        return Direction.entries.map { direction ->
            position.move(direction)
        }.filter { inBounds(it) && predicate.test(it) }
    }

    override fun getNeighbours(position: Vector2, predicate: Predicate<Vector2>): List<Vector2> =
        getNeighbours(position.toVector2i()) { predicate.test(it.toVector2()) }.map { it.toVector2() }

    companion object {
        fun <T>fromString(input: String, toNode: (c: Char) -> T): Grid<T> =
            input.lines().map { line -> line.map { toNode(it) } }.let { Grid(data = it) }
    }
}
