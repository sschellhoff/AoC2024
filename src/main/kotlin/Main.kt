package de.sschellhoff

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.getDaysForYear


fun main() {
    getDaysForYear(2024).forEach { day ->
        day.run(Day.RunMode.BOTH)
    }
}

