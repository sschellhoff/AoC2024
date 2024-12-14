package de.sschellhoff.utils

import java.util.function.Predicate

fun String.getGridSize(): Pair<Long, Long> = lines().first().length.toLong() to lines().size.toLong()

data class Grid<T>(private val data: List<List<T>>) {
    private val width: Int = data.first().size
    private val height: Int = data.size

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

    private fun inBounds(position: Vector2i): Boolean =
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

    fun get(position: Vector2i): T = data[position.y][position.x]

    fun getNeighbours(position: Vector2i, predicate: Predicate<Vector2i> = Predicate { true }): List<Vector2i> {
        if(!inBounds(position)) {
            return emptyList()
        }

        return Direction.entries.map { direction ->
            position.move(direction)
        }.filter { inBounds(it) && predicate.test(it) }
    }

    companion object {
        fun <T>fromString(input: String, toNode: (c: Char) -> T): Grid<T> =
            input.lines().map { line -> line.map { toNode(it) } }.let { Grid(data = it) }
    }
}

fun <T>Grid<T>.floodFill(start: Vector2i, handleEdgePiece: (position: Vector2i, numberOfMatchingNeighbours: Int) -> Unit): Set<Vector2i> {
    val value = get(start)
    val visited = mutableSetOf<Vector2i>()
    val toCheck = mutableListOf(start)
    while (toCheck.isNotEmpty()) {
        val next = toCheck.removeLast()
        if (!visited.add(next)) {
            continue
        }
        val neighbours = getNeighbours(next) { get(it) == value }
        val numberOfMatchingNeighbours = neighbours.size
        if (numberOfMatchingNeighbours < 4) {
            handleEdgePiece(next, numberOfMatchingNeighbours)
        }
        neighbours.forEach { toCheck.add(it) }
    }
    return visited
}

fun <T>Grid<T>.forDistinctAreas(handleArea: (areaPieces: Set<Vector2i>, edgeInfo: List<Pair<Vector2i, Int>>) -> Unit) {
    val visited = mutableSetOf<Vector2i>()
    forEachIndexed { x, y, _ ->
        val position = Vector2i(x, y)
        if (!visited.contains(position)) {
            val edgeInfo = mutableListOf<Pair<Vector2i, Int>>()
            val region = floodFill(position) { edgePosition, numberOfNeighbours ->
                edgeInfo.add(edgePosition to (numberOfNeighbours))
            }
            handleArea(region, edgeInfo)
            visited.addAll(region)
        }
    }
}