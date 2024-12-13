package de.sschellhoff.utils

fun String.blocks() = split("\n\n")

fun String.blockLines(): List<List<String>> = blocks().map { it.lines() }