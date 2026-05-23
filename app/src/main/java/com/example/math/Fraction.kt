package com.example.math

import kotlin.math.abs

data class Fraction(val num: Int, val den: Int) {
    init {
        require(den != 0) { "Denominator cannot be zero" }
    }

    // Reduce the fraction to its simplest terms
    fun simplify(): Fraction {
        val gcdVal = gcd(abs(num), abs(den))
        val rawNum = num / gcdVal
        val rawDen = den / gcdVal
        return if (rawDen < 0) {
            Fraction(-rawNum, -rawDen)
        } else {
            Fraction(rawNum, rawDen)
        }
    }

    operator fun plus(other: Fraction): Fraction {
        val n = this.num * other.den + other.num * this.den
        val d = this.den * other.den
        return Fraction(n, d).simplify()
    }

    operator fun minus(other: Fraction): Fraction {
        val n = this.num * other.den - other.num * this.den
        val d = this.den * other.den
        return Fraction(n, d).simplify()
    }

    operator fun times(other: Fraction): Fraction {
        val n = this.num * other.num
        val d = this.den * other.den
        return Fraction(n, d).simplify()
    }

    operator fun div(other: Fraction): Fraction {
        val n = this.num * other.den
        val d = this.den * other.num
        return Fraction(n, d).simplify()
    }

    fun toDouble(): Double = num.toDouble() / den.toDouble()

    override fun toString(): String {
        val sim = simplify()
        if (sim.den == 1) return "${sim.num}"
        return "${sim.num}/${sim.den}"
    }

    fun toMathString(): String {
        val sim = simplify()
        if (sim.den == 1) return "${sim.num}"
        if (sim.num == 0) return "0"
        return "${sim.num}/${sim.den}"
    }

    companion object {
        val ZERO = Fraction(0, 1)
        val ONE = Fraction(1, 1)

        private fun gcd(a: Int, b: Int): Int {
            var x = a
            var y = b
            while (y != 0) {
                val temp = y
                y = x % y
                x = temp
            }
            return if (x == 0) 1 else x
        }

        fun parse(s: String): Fraction {
            val clean = s.trim()
            if (clean.isEmpty()) return ZERO
            return if (clean.contains("/")) {
                val parts = clean.split("/")
                val n = parts[0].trim().toIntOrNull() ?: 0
                val d = parts[1].trim().toIntOrNull() ?: 1
                if (d == 0) Fraction(n, 1) else Fraction(n, d)
            } else {
                val n = clean.toIntOrNull() ?: 0
                Fraction(n, 1)
            }
        }
    }
}
