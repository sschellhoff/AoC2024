package de.sschellhoff

import java.io.File

fun getInput(day: String, test: Boolean = false, suffix: String = "") = File("src/main/resources/Day$day${if (test) "_test" else ""}$suffix.txt").readText()