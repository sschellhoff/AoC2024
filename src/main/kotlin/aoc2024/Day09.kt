package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day

class Day09: Day(9, 1928, 2858) {
    override fun part1(input: String): Long {
        return input.toFS().defragment().checksum()
    }

    override fun part2(input: String): Long {
        return input.toFS2().defragment().checksum()
    }

    private fun String.toFS(): MutableList<Long> {
        var id = 0L
        var isFile = true
        return this.flatMap { c ->
            val n = c.digitToInt()
            val number = if (isFile) {
                id++
            } else {
                -1
            }
            val l = List(size = n) { number }
            isFile = !isFile
            l
        }.toMutableList()
    }

    private fun String.toFS2(): FS {
        val data = toFS()
        val freeSpace = mutableListOf<FreeSpace>()
        val files = mutableListOf<File>()
        var start = 0
        var id = 0L
        var isFile = true
        forEach { c ->
            val size = c.digitToInt()
            if (isFile) {
                files.add(File(start = start, size = size, id = id))
            } else {
                freeSpace.add(FreeSpace(start = start, size = size))
            }
            if (isFile) {
                id++
            }
            start += size
            isFile = !isFile
        }
        return FS(data = data, freeSpace = freeSpace, files = files)
    }

    private fun MutableList<Long>.defragment(): MutableList<Long> {
        var head = 0
        var tail = this.size - 1
        while (true) {
            while (head < this.size && this[head] != -1L) head++
            while (tail >= 0 && this[tail] == -1L) tail--
            if (head >= tail) {
                return this
            }
            this[head] = this[tail]
            this[tail] = -1
        }
    }

    private fun FS.defragment(): MutableList<Long> {
        files.reversed().forEach { file ->
            val free = freeSpace.firstOrNull { space ->
                space.start < file.start && space.size >= file.size
            }
            if (free != null) {
                (0..<file.size).forEach { i ->
                    data[free.start + i] = file.id
                    data[file.start + i] = -1
                }
                free.size -= file.size
                free.start += file.size
            }
        }
        return this.data
    }

    private fun List<Long>.checksum(): Long = mapIndexed { i, n ->
        if (n > 0) n * i else 0
    }.sum()

    data class FreeSpace(var start: Int, var size: Int)
    data class File(val start: Int, val size: Int, val id: Long)
    data class FS(val data: MutableList<Long>, val freeSpace: List<FreeSpace>, val files: List<File>)
}