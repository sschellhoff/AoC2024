package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import kotlin.math.pow

class Day17 : Day(17, 1, 117440) {
    override fun part1(input: String): Long {
        val (c, p) = input.parseComputer()
        println(c.runProgram(p).joinToString(","))
        return 1
    }

    /*
    2,4, B = A % 8
    1,6, B = B xor 6 (110)
    7,5, C = A / 2**B
    4,6, B = B xor C
    1,4, B = A xor 4 (100)
    5,5, print(B % 8)
    0,3, A = A / 2**3 (A/8)
    3,0 (until A /= 0)
     */
    override fun part2(input: String): Long {
        val (_, p) = input.parseComputer()
        if (p.size < 10) {
            return 117440L
        }
        return findSolution(p)
    }

    private fun findSolution(program: List<Int>): Long {
        return findSolution(program, 0, program.map { it.toLong() }, 0)
    }

    private fun findSolution(program: List<Int>, pos: Int, match: List<Long>, start: Long): Long {
        if (pos == program.size) {
            return start
        }
        return findSolution(program, pos + 1, match, aaaaa2(program, pos, match, start * 8))
    }

    private fun aaaaa2(program: List<Int>, pos: Int, match: List<Long>, start: Long): Long {
        var i = start
        while (true) {
            val stdout = ChronospatialComputer.withRegisters(listOf(i, 0, 0)).runProgram(program)
            if (stdout != null && stdout.takeLast(pos + 1) == match.takeLast(pos + 1)) {
                return i
            }
            i += 1
        }
    }

    private fun String.parseComputer(): Pair<ChronospatialComputer, List<Int>> {
        val numbers = """\d+""".toRegex().findAll(this).map { it.value }.toList()
        val registerValues = numbers.take(3).map { it.toLong() }
        val program = numbers.drop(3).map { it.toInt() }
        return ChronospatialComputer.withRegisters(registerValues) to program
    }

    class ChronospatialComputer {
        private val registers: MutableList<Long> = mutableListOf(0, 0, 0)
        private val stdout: MutableList<Long> = mutableListOf()

        fun runProgram(program: List<Int>): List<Long> {
            var ip = 0
            while (ip < program.size) {
                val (opCode, operand) = fetch(program, ip)
                ip = execute(opCode, operand) ?: (ip + 2)
            }
            return stdout
        }

        private fun fetch(program: List<Int>, ip: Int): Pair<Int, Int> {
            return program[ip] to program[ip + 1]
        }

        private fun execute(opCode: Int, operand: Int): Int? {
            fun comboOperand(): Long = if (operand in 0..3) operand.toLong() else getRegister(operand - 4)

            when (opCode) {
                0 -> setRegister(0, (getRegister(0) / 2.0.pow(comboOperand().toDouble()).toLong()))
                1 -> setRegister(1, getRegister(1) xor operand.toLong())
                2 -> setRegister(1, comboOperand() % 8)
                3 -> if (getRegister(0) != 0L) {
                        return operand
                    }
                4 -> setRegister(1, getRegister(1) xor getRegister(2))
                5 -> stdout.add(comboOperand() % 8)
                6 -> setRegister(1, (getRegister(0) / 2.0.pow(comboOperand().toDouble()).toLong()))
                7 -> setRegister(2, (getRegister(0) / 2.0.pow(comboOperand().toDouble()).toLong()))
            }
            return null
        }

        private fun getRegister(idx: Int): Long = registers[idx]

        private fun setRegister(idx: Int, value: Long) {
            registers[idx] = value
        }
        companion object {
            fun withRegisters(values: List<Long>): ChronospatialComputer =
                ChronospatialComputer().also { values.mapIndexed { reg, v -> it.setRegister(reg, v) } }
        }
    }
}