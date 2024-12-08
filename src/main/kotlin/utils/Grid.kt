package de.sschellhoff.utils

fun String.getGridSize(): Pair<Long, Long> = lines().first().length.toLong() to lines().size.toLong()