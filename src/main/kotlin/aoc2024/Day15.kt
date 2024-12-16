package de.sschellhoff.aoc2024

import de.sschellhoff.utils.*
import java.util.function.Predicate

class Day15 : Day(15, 10092, 9021, "_big", "_big") {
    override fun part1(input: String): Long {
        val (grid, moves) = parseInput(input)
        val robot = grid.findRobotPosition()

        move(robot, moves, grid)

        return grid.score()
    }

    override fun part2(input: String): Long {
        val (grid, moves) = parseInputExtended(input)
        val robot = grid.findRobotPosition { it == ExtendedTile.Robot}

        move(robot, grid, moves)
        return grid.scorePart2()
    }

    //region Part 1
    private fun move(robot: Vector2i, directions: List<Direction>, grid: Grid<Tile>, debug: Boolean = false) {
        if (debug)
            grid.print()
        var r = robot
        directions.forEach { direction ->
            r = move(r, direction, grid)
            if (debug) {
                grid.print()
                println()
            }
        }
    }

    private fun move(robot: Vector2i, direction: Direction, grid: Grid<Tile>): Vector2i {
        val wantedRobotPosition = robot.move(direction)
        var w = wantedRobotPosition
        while (grid.get(w) == Tile.Box) {
            w = w.move(direction)
        }
        if (w != wantedRobotPosition) {
            if (grid.get(w) == Tile.Free) {
                grid.set(w, Tile.Box)
                grid.set(wantedRobotPosition, Tile.Robot)
                grid.set(robot, Tile.Free)
                return wantedRobotPosition
            }
        } else {
            if (grid.get(wantedRobotPosition) == Tile.Free) {
                grid.set(wantedRobotPosition, Tile.Robot)
                grid.set(robot, Tile.Free)
                return wantedRobotPosition
            }
        }
        return robot
    }

    private fun Grid<Tile>.score(): Long {
        var score = 0L
        this.forEachIndexedI { x, y, t ->
            if (t == Tile.Box) {
                score += (100 * y + x)
            }
        }
        return score
    }

    private fun parseInput(input: String): Pair<Grid<Tile>, List<Direction>> {
        val (map, moves) = input.blocks()
        val grid = Grid.fromString(map) { c ->
            when (c) {
                '#' -> Tile.Wall
                '@' -> Tile.Robot
                'O' -> Tile.Box
                '.' -> Tile.Free
                else -> throw IllegalArgumentException(c.toString())
            }
        }
        val directions = moves.lines().flatMap { it.toDirections() }

        return grid to directions
    }

    enum class Tile {
        Wall,
        Robot,
        Box,
        Free
    }

    private fun Tile.toChar(): Char = when (this) {
        Tile.Box -> 'O'
        Tile.Wall -> '#'
        Tile.Free -> '.'
        Tile.Robot -> '@'
    }

    private fun Grid<Tile>.findRobotPosition(): Vector2i {
        return findRobotPosition { it == Tile.Robot }
        //val positions = findPositions { it == Tile.Robot }
        //check(positions.size == 1)
        //return positions.first()
    }

    private fun Grid<Tile>.print() {
        print { it.toChar() }
    }
    //endregion

    //region Part2
    private fun move(robot: Vector2i, grid: Grid<ExtendedTile>, directions: List<Direction>, debug: Boolean = false) {
        if (debug)
            grid.print { it.toChar() }
        var currentPosition = robot
        directions.forEach { direction ->
            currentPosition = move(currentPosition, grid, direction)
            if (debug) {
                grid.print { it.toChar() }
                println()
            }
        }
    }

    private fun move(robot: Vector2i, grid: Grid<ExtendedTile>, direction: Direction): Vector2i {
        val wantedPosition = robot.move(direction)
        if (direction.isHorizontal()) {
            var p = wantedPosition
            while (grid.get(p) in listOf(ExtendedTile.BoxLeft, ExtendedTile.BoxRight)) {
                p = p.move(direction)
            }
            if (grid.get(p) == ExtendedTile.Free) {
                val xs = if (direction == Direction.WEST) (p.x..robot.x) else { (p.x downTo robot.x) }
                xs.zipWithNext { x1, x2 ->
                    grid.set(Vector2i(x1, p.y), grid.get(Vector2i(x2, p.y)))
                }
                grid.set(robot, ExtendedTile.Free)
                return wantedPosition
            }
        } else {
            if (grid.get(wantedPosition) == ExtendedTile.Free) {
                grid.set(wantedPosition, ExtendedTile.Robot)
                grid.set(robot, ExtendedTile.Free)
                return wantedPosition
            }
            if (grid.get(wantedPosition) == ExtendedTile.Wall) {
                return robot
            }
            val positionsToMove = mutableListOf<Set<Vector2i>>()
            var currentWave = grid.getBoxTiles(wantedPosition)
            while (currentWave.isNotEmpty()) {
                positionsToMove.add(currentWave)
                currentWave = currentWave.getNextWave(direction, grid) ?: return robot
            }
            positionsToMove.reversed().forEach { positions ->
                positions.forEach { position ->
                    grid.set(position.move(direction), grid.get(position))
                    grid.set(position, ExtendedTile.Free)
                }
            }
            grid.set(wantedPosition, ExtendedTile.Robot)
            grid.set(robot, ExtendedTile.Free)
            return wantedPosition
        }
        return robot
    }

    private fun Grid<ExtendedTile>.getBoxTiles(position: Vector2i): Set<Vector2i> = when (get(position)) {
        ExtendedTile.BoxLeft -> setOf(position, position.move(Direction.EAST))
        ExtendedTile.BoxRight -> setOf(position, position.move(Direction.WEST))
        else -> setOf()
    }

    private fun Set<Vector2i>.getNextWave(direction: Direction, grid: Grid<ExtendedTile>): Set<Vector2i>? {
        val result = mutableSetOf<Vector2i>()
        this.forEach { pos ->
            val next = pos.move(direction)
            if (grid.get(next) == ExtendedTile.Wall) {
                return null
            }
            result.addAll(grid.getBoxTiles(next))
        }
        return result
    }

    private fun Grid<ExtendedTile>.scorePart2(): Long {
        var result = 0L
        this.forEachIndexedI { x, y, t ->
            if (t == ExtendedTile.BoxLeft) {
                result += 100 * y + x
            }
        }
        return result
    }

    private fun String.extend(): String = map {
        when (it) {
            '.' -> ".."
            'O' -> "[]"
            '#' -> "##"
            '@' -> "@."
            '\n' -> "\n"
            else -> throw IllegalArgumentException(it.toString())
        }
    }.joinToString("")
    enum class ExtendedTile {
        Wall,
        BoxLeft,
        BoxRight,
        Free,
        Robot
    }

    private fun ExtendedTile.toChar(): Char = when(this) {
        ExtendedTile.Wall -> '#'
        ExtendedTile.BoxLeft -> '['
        ExtendedTile.BoxRight -> ']'
        ExtendedTile.Free -> '.'
        ExtendedTile.Robot -> '@'
    }

    private fun Char.toExtendedTile(): ExtendedTile = when(this) {
        '.' -> ExtendedTile.Free
        '[' -> ExtendedTile.BoxLeft
        ']' -> ExtendedTile.BoxRight
        '#' -> ExtendedTile.Wall
        '@' -> ExtendedTile.Robot
        else -> throw IllegalArgumentException("cannot extend char '$this'")
    }

    private fun parseInputExtended(input: String): Pair<Grid<ExtendedTile>, List<Direction>> {
        val (map, moves) = input.blocks()
        val grid = Grid.fromString(map.extend()) { it.toExtendedTile() }
        val directions = moves.lines().flatMap { it.toDirections() }

        return grid to directions
    }
    //endregion


    private fun <T>Grid<T>.print(toChar: (t: T) -> Char) {
        var currentY = 0
        forEachIndexedI { _, y, t ->
            if (y != currentY) {
                println()
            }
            val c = toChar(t)
            print(c)
            currentY = y
        }
        println()
    }

    private fun <T>Grid<T>.findRobotPosition(predicate: Predicate<T>): Vector2i {
        val positions = findPositions { predicate.test(it) }
        check(positions.size == 1)
        return positions.first()
    }
}