package de.sschellhoff.utils

import java.util.*

fun <T>List<T>.permute(): List<List<T>> {
    if (isEmpty()) {
        return listOf()
    }
    if (size == 1) {
        return listOf(this)
    }
    return permuteHelper(0, mutableListOf<List<T>>())
}

private fun <T>List<T>.permuteHelper(currentIndex: Int, solutions: MutableList<List<T>>): MutableList<List<T>> {
    if (lastIndex == currentIndex) {
        solutions.add(this.toList())
    }
    (currentIndex..lastIndex).forEach { i ->
        Collections.swap(this, currentIndex, i)
        permuteHelper(currentIndex + 1, solutions)
        Collections.swap(this, i, currentIndex)
    }
    return solutions
}

fun String.permute(): Set<String> = toList().permute().map { it.joinToString("") }.toSet()