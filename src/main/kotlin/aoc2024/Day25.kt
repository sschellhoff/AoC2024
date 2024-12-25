package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.blockLines
import java.util.BitSet

class Day25: Day(25, 3, 50) {
    override fun part1(input: String): Long {
        val (keys, locks) = input.parse()
        return keys.sumOf { key -> locks.filter { !key.intersects(it) }.size }.toLong()
    }

    override fun part2(input: String): Long {
        return 50
    }

    private fun String.parse(): Pair<List<BitSet>, List<BitSet>> {
        val keys = mutableListOf<BitSet>()
        val locks = mutableListOf<BitSet>()
        blockLines().forEach { block ->
            block.toKeyOrLock().let {
                if (it.isKey())
                    keys.add(it)
                else
                    locks.add(it)
            }
        }
        return keys to locks
    }

    private fun BitSet.isKey(): Boolean = get(0)

    private fun List<String>.toKeyOrLock(): BitSet {
        val data = BitSet(5 * 7)
        forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    data.set(y * 5 + x)
                }
            }
        }
        return data
    }
}

fun main() {
    Day25().run(Day.RunMode.BOTH)
}