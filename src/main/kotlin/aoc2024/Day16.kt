package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*
import de.sschellhoff.utils.pathfinding.EdgeInfo
import de.sschellhoff.utils.pathfinding.dijkstra

class Day16: Day(16, 7036, 45) {
    override fun part1(input: String): Long {
        val (grid, points) = setup(input)
        val (startWithDirection, isEnd) = points
        val path = dijkstra(startWithDirection, isEnd) { (n, d) -> getNextNodes(n, d, grid) }
        return path?.second ?: throw IllegalStateException()
    }

    override fun part2(input: String): Long {
        val (grid, points) = setup(input)
        val (startWithDirection, isEnd) = points
        val nodes = findAllNodesOnAllShortestPaths(startWithDirection, isEnd) { (n, d) -> getNextNodes(n, d, grid) }.map { it.first }.toSet()
        return nodes.size.toLong()
    }

    private fun setup(input: String): Pair<Grid<Tile>, Pair<Pair<Vector2i, Direction>, (Pair<Vector2i, Direction>) -> Boolean>> {
        val grid = Grid.fromString(input) { if (it == '#') Tile.Wall else Tile.Empty }
        val (start, end) = input.findStartAndEnd()
        val startWithDirection = start to Direction.EAST
        val isEnd = { n: Pair<Vector2i, Direction> -> n.first == end}
        return grid to (startWithDirection to isEnd)
    }

    private fun getNextNodes(n: Vector2i, d: Direction, grid: Grid<Tile>): List<EdgeInfo<Pair<Vector2i, Direction>>> {
        return listOf(
            EdgeInfo(n to d, n.move(d) to d, 1),
            EdgeInfo(n to d, n.move(d.turnLeft()) to d.turnLeft(), 1001),
            EdgeInfo(n to d, n.move(d.turnRight()) to d.turnRight(), 1001)
        ).filter { info ->
            grid.inBounds(info.b.first) && grid.get(info.b.first) == Tile.Empty
        }
    }

    enum class Tile {
        Wall,
        Empty
    }

    private fun String.findStartAndEnd(): Pair<Vector2i, Vector2i> {
        var start: Vector2i? = null
        var end: Vector2i? = null
        this.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'S') {
                    start = Vector2i(x, y)
                } else if (c == 'E') {
                    end = Vector2i(x, y)
                }
            }
        }
        checkNotNull(start)
        checkNotNull(end)
        return start!! to end!!
    }
}

private fun <NODE>findAllNodesOnAllShortestPaths(start: NODE, isEnd: (node: NODE) -> Boolean, getNextNodes: (node: NODE) -> List<EdgeInfo<NODE>>): Set<NODE> {
    val pathInfo = mutableMapOf(start to NodeInfo2<NODE>(0, mutableListOf()))

    val openNodes = PriorityQueue.from(start to 0) {a, b -> a < b}
    val closedNodes = mutableSetOf<NODE>()

    val endNodes = mutableSetOf<NODE>()

    while (openNodes.isNotEmpty()) {
        val u = openNodes.extract().first
        val costSoFar = pathInfo[u]?.cost ?: throw IllegalStateException()
        closedNodes.add(u)
        if (isEnd(u)) {
            endNodes.add(u)
        }
        getNextNodes(u).forEach { nextNode ->
            val cost = nextNode.cost + costSoFar
            val currentCost = pathInfo[nextNode.b]?.cost
            if (!closedNodes.contains(nextNode.b)) {
                if (currentCost == null || currentCost > cost) {
                    pathInfo[nextNode.b] = NodeInfo2(cost = cost, predecessor = mutableListOf(u))
                    openNodes.insert(nextNode.b, cost)
                } else if (currentCost == cost) {
                    pathInfo[nextNode.b]!!.predecessor.add(u)
                    openNodes.insert(nextNode.b, cost)
                }
            }
        }
    }
    val pathNodes = mutableSetOf<NODE>()
    val minPathLength = endNodes.mapNotNull { pathInfo[it]?.cost }.min()
    pathNodes.addAll(endNodes.filter { pathInfo[it]?.cost == minPathLength })
    while(pathNodes.isNotEmpty()) {
        val node = pathNodes.last()
        pathNodes.remove(node)
        val preds = pathInfo[node]?.predecessor ?: emptyList()
        pathNodes.addAll(preds.filter { !endNodes.contains(it) })
        endNodes.addAll(preds)
    }
    return endNodes
}

private data class NodeInfo2<NODE>(val cost: Long, val predecessor: MutableList<NODE>)