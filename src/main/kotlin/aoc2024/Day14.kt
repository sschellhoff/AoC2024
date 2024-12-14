package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.Vector2

class Day14 : Day(14, 12, -1) {
    override fun part1(input: String): Long {
        val robots = input.toRobots()
        val bounds = if(robots.size < 20) Vector2(11, 7) else Vector2(101, 103)
        robots.forEach { it.move(bounds, 100) }
        // robots.print(bounds)
        val topLeft = robots.count { it.position.x in 0..<(bounds.x/2) && it.position.y in 0..<(bounds.y/2) }.toLong()
        val topRight = robots.count { it.position.x in (1 + bounds.x/2)..bounds.x && it.position.y in 0..<(bounds.y/2) }.toLong()
        val bottomLeft = robots.count { it.position.x in 0..<(bounds.x/2) && it.position.y in (1 + bounds.y/2)..bounds.y }.toLong()
        val bottomRight = robots.count { it.position.x in (1 + bounds.x/2)..bounds.x && it.position.y in (1 + bounds.y/2)..bounds.y }.toLong()

        return topLeft * topRight * bottomLeft * bottomRight
    }

    override fun part2(input: String): Long {
        val robots = input.toRobots()
        if (robots.size < 20) {
            return -1
        }
        val bounds = Vector2(101, 103)
        var i = 0L
        while (true) {
            i += 1
            robots.forEach { it.move(bounds) }
            val positions = mutableSetOf<Vector2>()
            var robotsOnTopOfEachOther = false
            robots.forEach {
                if (!positions.add(it.position)) {
                    robotsOnTopOfEachOther = true
                }
            }
            if (!robotsOnTopOfEachOther) {
                //robots.print(bounds)
                return i
            }
        }
    }

    private data class Robot(var position: Vector2, var velocity: Vector2) {
        fun move(bounds: Vector2, steps: Long = 1) {
            val x = move(pos = position.x, vel = velocity.x, bound = bounds.x, step = steps)
            val y = move(pos = position.y, vel = velocity.y, bound = bounds.y, step = steps)
            position = Vector2(x, y)
        }

        private fun move(pos: Long, vel: Long, bound: Long, step: Long): Long {
            val r = (pos + vel * step) % bound
            return when {
                r >= 0 -> r
                else -> r + bound
            }
        }
    }

    private fun List<Robot>.print(bounds: Vector2) {
        val robotsPerPosition = mutableMapOf<Vector2, Long>()
        this.forEach { robot ->
            robotsPerPosition[robot.position] = robotsPerPosition.getOrDefault(robot.position, 0) + 1L
        }
        robotsPerPosition.print(bounds)
    }

    private fun Map<Vector2, Long>.print(bounds: Vector2) {
        (0..bounds.y).forEach { y ->
            (0..bounds.x).forEach { x ->
                val c = getOrDefault(Vector2(x, y), 0)
                if (c == 0L) {
                    print('.')
                } else {
                    print(c)
                }
            }
            println()
        }
    }

    private fun String.toRobots(): List<Robot> {
        val r = """-?\d+""".toRegex()
        return lines().map { line ->
            val (pX, pY, vX, vY) = r.findAll(line).toList().map { it.value.toLong() }
            Robot(Vector2(pX, pY), Vector2(vX, vY))
        }
    }
}
