package de.sschellhoff.utils

import de.sschellhoff.toIntOrThrow
import java.util.function.Predicate

data class SparseGrid<T>(private val data: MutableMap<Vector2, T> = mutableMapOf(), val bounds: Vector2): Space2D<T> {
    override fun forEachIndexed(action: (x: Long, y: Long, c: T) -> Unit) {
        (0..bounds.y).forEach { y ->
            (0..bounds.x).forEach { x ->
                val d = data[Vector2(x, y)]
                if (d != null) {
                    action(x, y, d)
                }
            }
        }
    }
    override fun forEachIndexedI(action: (x: Int, y: Int, c: T) -> Unit) =
        forEachIndexed { x: Long, y: Long, c: T -> action(x.toIntOrThrow(), y.toIntOrThrow(), c) }

    private fun inBounds(position: Vector2): Boolean =
        position.x in 0..<bounds.x && position.y in 0..<bounds.y

    fun findPositions(predicate: Predicate<T>): Set<Vector2> {
        val result = mutableSetOf<Vector2>()
        forEachIndexed { x: Long, y: Long, c: T ->
            if (predicate.test(c)) {
                result.add(Vector2(x, y))
            }
        }
        return result
    }

    override fun get(position: Vector2): T = data.getValue(position)
    override fun get(position: Vector2i): T = get(position.toVector2())

    override fun getNeighbours(position: Vector2, predicate: Predicate<Vector2>): List<Vector2> {
        if(!inBounds(position)) {
            return emptyList()
        }

        return Direction.entries.map { direction ->
            position.move(direction)
        }.filter { inBounds(it) && predicate.test(it) }
    }

    override fun getNeighbours(position: Vector2i, predicate: Predicate<Vector2i>): List<Vector2i> =
        getNeighbours(position.toVector2()) { predicate.test(it.toVector2i()) }.map { it.toVector2i() }
}