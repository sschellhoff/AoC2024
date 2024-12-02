package de.sschellhoff

import java.io.File

fun getInput(day: String, test: Boolean = false) = File("src/main/resources/Day$day${if (test) "_test" else ""}.txt").readText()