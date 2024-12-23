package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day23 : Day(23, 7, 1) {
    override fun part1(input: String): Long {
        val connections = input.toConnections()
        val starts = connections.keys.filter { it.startsWith("t") }
        val three = mutableSetOf<String>()
        starts.forEach { start ->
            connections[start]?.forEach { middle ->
                connections[middle]?.forEach { end ->
                    if (connections[end]?.contains(start) == true) {
                        three.add(listOf(start, middle, end).sorted().toString())
                    }
                }
            }
        }

        return three.size.toLong()
    }

    override fun part2(input: String): Long {
        val connections = input.toConnections()
        val r = bronKerbosch(mutableSetOf(), connections.keys.toMutableSet(), mutableSetOf(), connections)
        println(r.maxBy { it.length })

        return 1
    }

    private fun bronKerbosch(r: Set<String>, p: MutableSet<String>, x: MutableSet<String>, neighbours: Map<String, Set<String>>): Set<String> {
        if (p.isEmpty() && x.isEmpty()) {
            return setOf(r.sorted().joinToString(","))
        }
        val result = mutableSetOf<String>()
        p.toSet().forEach { v ->
            val neighboursOfV = neighbours[v] ?: emptySet()
            result.addAll(bronKerbosch(r + v, p.intersect(neighboursOfV).toMutableSet(), x.intersect(neighboursOfV).toMutableSet(), neighbours))
            p.remove(v)
            x.add(v)
        }
        return result
    }

    private fun String.toConnections(): Map<String, MutableSet<String>> {
        val connections = mutableMapOf<String, MutableSet<String>>()
        lines().map { connection ->
            connection.split("-").let {
                connections.getOrPut(it.first()) { mutableSetOf() }.add(it[1])
                connections.getOrPut(it[1]) { mutableSetOf() }.add(it.first())
            }
        }
        return connections
    }
}