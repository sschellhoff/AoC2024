package de.sschellhoff

import java.io.File
import kotlin.math.abs

fun part1(): Int {
    val (a, b) = getInput("01").toLists()
    return a.sorted().zip(b.sorted()).sumOf { (a, b) -> abs(b - a) }
}

fun part2(): Int {
    val (a, b) = getInput("01").toLists()
    return a.sumOf { elem -> b.count { it == elem } * elem }
}

fun String.toLists() = lines().map { line ->
    val (a, b) = line.split("\\s+".toRegex()).map { it.toInt() }
    a to b
}.unzip()

fun getInput(day: String, test: Boolean = false) = File("src/main/resources/Day$day${if (test) "_test" else ""}.txt").readText()