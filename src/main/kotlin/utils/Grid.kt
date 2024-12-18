package de.sschellhoff.utils

import java.util.function.Predicate

fun String.getGridSize(): Pair<Long, Long> = lines().first().length.toLong() to lines().size.toLong()

data class Grid<T>(private val data: List<MutableList<T>>): Space2D<T> {
    val width: Int = data.first().size
    val height: Int = data.size

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


    fun inBounds(position: Vector2i): Boolean =
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

    // TODO maybe think about mutable/immutable grid
    fun set(position: Vector2i, value: T) {
        data[position.y][position.x] = value
    }

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
            input.lines().map { line -> line.map { toNode(it) }.toMutableList() }.let { Grid(data = it) }

        fun <T>initialize(width: Int, height: Int, toNode: (x: Int, y: Int) -> T): Grid<T> {
            val data = mutableListOf<MutableList<T>>()
            (0..<height).forEach { y ->
                val line = mutableListOf<T>()
                (0..<width).forEach { x ->
                    line.add(toNode(x, y))
                }
                data.add(line)
            }
            return Grid(data = data)
        }
    }
}
