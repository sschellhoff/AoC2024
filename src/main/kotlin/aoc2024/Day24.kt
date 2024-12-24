package de.sschellhoff.aoc2024

import de.sschellhoff.utils.Day
import de.sschellhoff.utils.blockLines

class Day24: Day(24, 2024, 1) {
    override fun part1(input: String): Long {
        val (wires, expressions) = input.parse(false)
        return evaluate(wires, expressions)
    }

    private fun eval(expressions: MutableMap<String, Expr>, wires: MutableMap<String, Boolean>): Boolean {
        expressions.keys.forEach { target ->
            val expr = expressions[target]!!
            val result = expr.eval(wires)
            if (result != null) {
                wires[target] = result
                expressions.remove(target)
                return true
            }
        }
        return false
    }

    override fun part2(input: String): Long {
        val (_, expressions) = input.parse(true)
        // tried to find wrong bits in the results
        // looked through the needed rules and drawn the adders by hand to find the bad connections
        // idea to automate
        // start loop from z00 to z45
        // recognize the sub parts of the full adder
        // detect the bad connection
        // this should be possible, because the wanted swap is always inside each adder
        println(listOf("hjm",
                        "mcq",
                        "z37",
                        "dsd",
                        "sbg",
                        "z19",
                        "djg",
                        "z12").sorted().joinToString(","))

        return calc(0, 0, expressions)
    }

    private fun calc(a: Long, b: Long, expressions: Map<String, Expr>): Long {
        val wires = mutableMapOf<String, Boolean>()
        (0..44).forEach {
            wires["x${it.toString().padStart(2, '0')}"] = ((a shr it) % 2) == 1L
            wires["y${it.toString().padStart(2, '0')}"] = ((b shr it) % 2) == 1L
        }
        return evaluate(wires, expressions.toMutableMap())
    }

    private fun evaluate(wires: MutableMap<String, Boolean>, expressions: MutableMap<String, Expr>): Long {
        while (expressions.isNotEmpty() && eval(expressions, wires)) {}
        var r = 0L
        wires.keys.filter { it.startsWith("z") }.sorted().reversed().forEach { b ->
            r *= 2
            r += if (wires[b]!!) 1 else 0
        }
        return r
    }

    private fun String.parse(withSwap: Boolean): Pair<MutableMap<String, Boolean>, MutableMap<String, Expr>> {
        val (initialInputs, circuits) = blockLines()
        val wires = mutableMapOf<String, Boolean>()
        initialInputs.forEach {
            it.split(": ").let { (name, value) ->
                check(!wires.containsKey(name)) { "doubled input value for '$name'" }
                wires[name] = value == "1"
            }
        }
        val expressions = mutableMapOf<String, Expr>()
        circuits.forEach { desc ->
            val nDesc = if (withSwap) swapRule(desc) else desc
            val (a, o, b, _, t) = nDesc.split(" ")
            val expr = when (o) {
                "AND" -> Expr(a, b, Op.And)
                "OR" -> Expr(a, b, Op.Or)
                "XOR" -> Expr(a, b, Op.Xor)
                else -> throw IllegalArgumentException(o)
            }
            expressions[t] = expr
        }
        return wires to expressions
    }

    private fun swapRule(original: String): String = when (original) {
        "jsb AND njf -> z12" -> "jsb AND njf -> djg"
        "njf XOR jsb -> djg" -> "njf XOR jsb -> z12"
        //"x19 AND y19 -> z19" -> "x19 AND y19 -> z20"
        //"ckt XOR jwg -> z20" -> "ckt XOR jwg -> z19"
        //"spj OR gqf -> z37" -> "spj OR gqf -> z38"
        //"dsd XOR spf -> z38" -> "dsd XOR spf -> z37"
        //"jrr XOR hjm -> z24" -> "jrr XOR hjm -> wpd"
        //"sqr AND wpd -> tpj" -> "sqr AND wpd -> hjm"
        "x19 AND y19 -> z19" -> "x19 AND y19 -> sbg"
        "qjc XOR kbs -> sbg" -> "qjc XOR kbs -> z19"
        "hhp XOR tjm -> dsd" -> "hhp XOR tjm -> z37"
        "spj OR gqf -> z37" -> "spj OR gqf -> dsd"
        "x24 XOR y24 -> mcq" -> "x24 XOR y24 -> hjm"
        "y24 AND x24 -> hjm" -> "y24 AND x24 -> mcq"
        else -> original
    }

    enum class Op {
        And,
        Or,
        Xor
    }

    data class Expr(val a: String, val b: String, val op: Op) {
        fun eval(inputs: Map<String, Boolean>): Boolean? {
            val a = inputs[a] ?: return null
            val b = inputs[b] ?: return null
            return when (op) {
                Op.And -> a && b
                Op.Or -> a || b
                Op.Xor -> a != b
            }
        }
    }
}