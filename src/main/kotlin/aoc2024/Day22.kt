package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.NTuple4
import de.sschellhoff.utils.then

class Day22: Day(22, 37327623L, 24L) {
    override fun part1(input: String): Long {
        val s = input.lines().map { it.toLong().nextSecret(2000) }
        return s.sum()
    }

    override fun part2(input: String): Long {
        val numBananasPerBuyer = input.lines().map { it.toLong().generateSecrets(2000) }
        val changesPerBuyer = numBananasPerBuyer.map { it.toChanges() }
        val buyer = numBananasPerBuyer.zip(changesPerBuyer)
        return buyer.count()
    }

    private fun List<Pair<List<Long>, List<Long>>>.count(): Long {
        val counts = mutableMapOf<NTuple4<Long, Long, Long, Long>, Long>()
        forEach { buyer ->
            val usedChanges = mutableSetOf<NTuple4<Long, Long, Long, Long>>()
            (0..<buyer.second.size - 3).forEach {
                val t = buyer.second[it] then buyer.second[it + 1] then buyer.second[it + 2] then buyer.second[it + 3]
                if (usedChanges.add(t)) {
                    val newCount = counts.getOrDefault(t, 0) + buyer.first[it + 4]
                    counts[t] = newCount
                }
            }
        }
        return counts.values.max()
    }

    private fun Long.generateSecrets(n: Long): List<Long> {
        val numberOfBananas = mutableListOf<Long>(this.numberOfBananas())
        (1..<n).fold(this) { a, _ ->
            a.nextSecret().also { numberOfBananas.add(it.numberOfBananas()) }
        }
        return numberOfBananas
    }

    private fun List<Long>.toChanges(): List<Long> = zipWithNext { a, b -> b - a }

    private fun Long.numberOfBananas(): Long = this % 10

    private fun Long.nextSecret(n: Int): Long {
        var s = this
        repeat(n) {
            s = s.nextSecret()
        }
        return s
    }

    private fun Long.nextSecret(): Long {
        val m = 16777216L
        val fst = ((this * 64) xor this) % m
        val snd = ((fst / 32) xor fst) % m
        return ((snd * 2048) xor snd) % m
    }
}