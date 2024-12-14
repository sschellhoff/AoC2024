package de.sschellhoff.utils

import java.util.function.Predicate

interface Space2D<T> {
    fun get(position: Vector2): T
    fun get(position: Vector2i): T
    fun getNeighbours(position: Vector2, predicate: Predicate<Vector2> = Predicate { true }): List<Vector2>
    fun getNeighbours(position: Vector2i, predicate: Predicate<Vector2i> = Predicate { true }): List<Vector2i>
    fun forEachIndexed(action: (x: Long, y: Long, c: T) -> Unit)
    fun forEachIndexedI(action: (x: Int, y: Int, c: T) -> Unit)
}

fun <T> Space2D<T>.floodFill(
    start: Vector2,
    handleEdgePiece: (position: Vector2, numberOfMatchingNeighbours: Int) -> Unit
): Set<Vector2> {
    val value = get(start)
    val visited = mutableSetOf<Vector2>()
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

fun <T> Space2D<T>.floodFill(
    start: Vector2i,
    handleEdgePiece: (position: Vector2i, numberOfMatchingNeighbours: Int) -> Unit
): Set<Vector2i> =
    floodFill(
        start.toVector2(),
        handleEdgePiece = { position, numberOfMatchingNeighbours ->
            handleEdgePiece(
                position.toVector2i(),
                numberOfMatchingNeighbours
            )
        }).map { it.toVector2i() }.toSet()

fun <T> Space2D<T>.forDistinctAreasI(handleArea: (areaPieces: Set<Vector2i>, edgeInfo: List<Pair<Vector2i, Int>>) -> Unit) {
    val visited = mutableSetOf<Vector2i>()
    forEachIndexedI { x: Int, y: Int, _ ->
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
fun <T> Space2D<T>.forDistinctAreas(handleArea: (areaPieces: Set<Vector2>, edgeInfo: List<Pair<Vector2, Int>>) -> Unit) {
    val visited = mutableSetOf<Vector2>()
    forEachIndexed { x: Long, y: Long, _ ->
        val position = Vector2(x, y)
        if (!visited.contains(position)) {
            val edgeInfo = mutableListOf<Pair<Vector2, Int>>()
            val region = floodFill(position) { edgePosition, numberOfNeighbours ->
                edgeInfo.add(edgePosition to (numberOfNeighbours))
            }
            handleArea(region, edgeInfo)
            visited.addAll(region)
        }
    }
}