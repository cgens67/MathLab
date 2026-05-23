package com.example.math

import kotlin.math.PI
import kotlin.math.sqrt

object Tingkatan2MathSuite {

    // --- 1. PATTERNS AND SEQUENCES ---
    data class SequenceResult(
        val diff: Int,
        val firstTerm: Int,
        val patternDescriptionEn: String,
        val patternDescriptionMs: String,
        val formula: String,
        val steps: List<String>
    )

    fun solveSequence(numbers: List<Int>): SequenceResult {
        if (numbers.size < 3) {
            return SequenceResult(
                0, 0,
                "Need 3+ terms", "Perlu 3+ sebutan", "",
                listOf("Please provide at least 3 numbers to spot the pattern.")
            )
        }

        val first = numbers[0]
        val diff1 = numbers[1] - numbers[0]
        val diff2 = numbers[2] - numbers[1]

        val isArithmetic = diff1 == diff2
        val steps = mutableListOf<String>()

        return if (isArithmetic) {
            val dString = if (diff1 >= 0) "+$diff1" else "$diff1"
            val descEn = "Arithmetic Sequence: Add $diff1 to the previous term (Common difference: $diff1)"
            val descMs = "Jujukan Aritmetik: Tambah $diff1 kepada sebutan sebelumnya (Beza sepunya: $diff1)"
            steps.add("Step 1: Subtract consecutive terms: T₂ − T₁ = ${numbers[1]} − $first = $diff1")
            steps.add("Step 2: Check next pair: T₃ − T₂ = ${numbers[2]} − ${numbers[1]} = $diff1")
            steps.add("Step 3: Confirm constant difference: d = $diff1 (Common difference / Beza sepunya confirmed).")

            val formula = if (diff1 == 0) {
                "T_n = $first"
            } else {
                val constantPart = first - diff1
                val sign = if (constantPart >= 0) " + $constantPart" else " − ${-constantPart}"
                "T_n = ${diff1}n${if (constantPart != 0) sign else ""}"
            }

            steps.add("Step 4: Formulate the general term using school formula: T_n = a + (n − 1)d")
            steps.add("Substitute start term a = $first and a common difference d = $diff1:")
            steps.add("T_n = $first + (n − 1) × ($diff1)")
            steps.add("Simplify: T_n = $first + $diff1 n − $diff1")
            steps.add("Final Form: $formula (For n = 1, 2, 3...)")

            SequenceResult(diff1, first, descEn, descMs, formula, steps)
        } else {
            // Check geometric progression ratio
            val ratio1 = if (numbers[0] != 0) numbers[1].toDouble() / numbers[0] else 0.0
            val ratio2 = if (numbers[1] != 0) numbers[2].toDouble() / numbers[1] else 0.0
            if (ratio1 == ratio2 && ratio1 != 0.0) {
                val rFloat = ratio1
                val descEn = "Geometric Sequence: Multiply by $rFloat (Common ratio: $rFloat)"
                val descMs = "Jujukan Geometri: Darab dengan $rFloat (Nisbah sepunya: $rFloat)"
                steps.add("Step 1: Check ratios of consecutive terms: T₂ ÷ T₁ = ${numbers[1]} ÷ $first = $ratio1")
                steps.add("Step 2: Check next ratio: T₃ ÷ T₂ = ${numbers[2]} ÷ ${numbers[1]} = $ratio1")
                val formula = "T_n = $first × ($ratio1)^(n−1)"
                steps.add("Step 3: Formulate general term T_n = a × r^(n − 1): $formula")
                SequenceResult(ratio1.toInt(), first, descEn, descMs, formula, steps)
            } else {
                val descEn = "Non-linear Custom Sequence: Pattern is complex or irregular."
                val descMs = "Jujukan Khas Bukan Linear: Pola yang kompleks atau tidak sekata."
                steps.add("Check differences: ${numbers[1]} − $first = $diff1, ${numbers[2]} − ${numbers[1]} = $diff2. Not constant.")
                steps.add("Pattern is not a simple linear sequence. Standard form formula T_n is complex.")
                SequenceResult(0, first, descEn, descMs, "Custom Sequence", steps)
            }
        }
    }


    // --- 2. ALGEBRAIC EXPANSION ---
    data class ExpansionResult(
        val expandedString: String,
        val steps: List<String>
    )

    fun expandProduct(a: Int, b: Int, c: Int, d: Int): ExpansionResult {
        // (ax + b)(cx + d) = ac*x^2 + (ad + bc)*x + bd
        val firstCoeff = a * c
        val middleCoeff = a * d + b * c
        val lastCoeff = b * d

        val steps = mutableListOf<String>()
        steps.add("Solve using FOIL distribution method: (ax + b) × (cx + d) = ax × cx + ax × d + b × cx + b × d")
        steps.add("Step 1 (First terms): Multiply ($a x) × ($c x) = ${firstCoeff}x²")
        steps.add("Step 2 (Outer terms): Multiply ($a x) × ($d) = ${a * d}x")
        steps.add("Step 3 (Inner terms): Multiply ($b) × ($c x) = ${b * c}x")
        steps.add("Step 4 (Last terms): Multiply ($b) × ($d) = $lastCoeff")

        val combinedText = "Combine outer and inner terms: ${a * d}x + ${b * c}x = ${middleCoeff}x"
        steps.add("Step 5 (Combine like terms): $combinedText")

        val x2Term = "${firstCoeff}x²"
        val xTerm = if (middleCoeff >= 0) " + ${middleCoeff}x" else " − ${-middleCoeff}x"
        val constTerm = if (lastCoeff >= 0) " + $lastCoeff" else " − ${-lastCoeff}"
        val finalString = "$x2Term$xTerm$constTerm"

        steps.add("Result: $finalString")
        return ExpansionResult(finalString, steps)
    }


    // --- 3. QUADRATIC FACTORISATION (Cross-Multiplication) ---
    data class FactorisationResult(
        val success: Boolean,
        val formattedResult: String,
        // Visual diagram coordinates for pendaraban silang
        val col1: Pair<String, String>, // px, rx
        val col2: Pair<String, String>, // q, s
        val col3: Triple<String, String, String>, // psx, qrx, middle term sum
        val steps: List<String>
    )

    fun factoriseQuadratic(a: Int, b: Int, c: Int): FactorisationResult {
        val steps = mutableListOf<String>()
        steps.add("To factorise quadratic of form ax² + bx + c, we find factors.")
        steps.add("Formula: $a x² + ${if (b >= 0) "+$b" else "$b"}x + ${if (c >= 0) "+$c" else "$c"}")

        // Search for px+q and rx+s where (px+q)(rx+s) = a*x^2 + b*x + c
        // satisfying: p*r = a, q*s = c, and p*s + q*r = b
        // Loop through factors of a and c
        val aFactors = findFactorsOf(a)
        val cFactors = findFactorsOf(c)

        for (p in aFactors) {
            val r = a / p
            for (q in cFactors) {
                val s = c / q
                if (p * s + q * r == b) {
                    // Success!
                    val term1 = if (p == 1) "x" else "${p}x"
                    val qSign = if (q >= 0) "+$q" else "−${-q}"
                    val term2 = if (r == 1) "x" else "${r}x"
                    val sSign = if (s >= 0) "+$s" else "−${-s}"

                    val factor1 = "($term1 ${if (q >= 0) "+ $q" else "− ${-q}"})"
                    val factor2 = "($term2 ${if (s >= 0) "+ $s" else "− ${-s}"})"
                    val resultString = "$factor1$factor2"

                    steps.add("Step 1: Find factors of structural coefficient a = $a  (p × r = $a). We chose p = $p, r = $r.")
                    steps.add("Step 2: Find factors of constant coefficient c = $c  (q × s = $c). We chose q = $q, s = $s.")
                    steps.add("Step 3: Perform cross-multiplication verification:")
                    steps.add("   (p × s) + (q × r) = ($p × $s) + ($q × $r) = ${p*s} + ${q*r} = $b (Matches central term b!)")

                    return FactorisationResult(
                        success = true,
                        formattedResult = resultString,
                        col1 = Pair("${p}x", "${r}x"),
                        col2 = Pair(q.toString(), s.toString()),
                        col3 = Triple("${p*s}x", "${q*r}x", "${b}x"),
                        steps = steps
                    )
                }
            }
        }

        // Check if there's a simpler factorization like pulling out a common factor
        // or difference of squares.
        if (b == 0 && c < 0) {
            // Difference of squares a*x^2 - |c| = (sqrt(a)x - sqrt(|c|))(sqrt(a)x + sqrt(|c|))
            val rootA = sqrt(a.toDouble()).toInt()
            val rootC = sqrt((-c).toDouble()).toInt()
            if (rootA * rootA == a && rootC * rootC == -c) {
                val term1 = if (rootA == 1) "x" else "${rootA}x"
                val finalRes = "($term1 − $rootC)($term1 + $rootC)"
                steps.add("Difference of Squares Pattern: (px − q) × (px + q) = p²x² − q²")
                steps.add("Here, ($term1)² − $rootC² = $finalRes")
                return FactorisationResult(
                    success = true,
                    formattedResult = finalRes,
                    col1 = Pair(term1, term1),
                    col2 = Pair("−$rootC", rootC.toString()),
                    col3 = Triple("${rootA*rootC}x", "−${rootA*rootC}x", "0x"),
                    steps = steps
                )
            }
        }

        steps.add("No simple integer factors found via cross multiplication table.")
        steps.add("This expression cannot be factorised beautifully with integers in school homework.")
        return FactorisationResult(
            success = false,
            formattedResult = "Prime (No simple factorisation)",
            col1 = Pair("ax", "x"),
            col2 = Pair("?", "?"),
            col3 = Triple("?", "?", "${b}x"),
            steps = steps
        )
    }

    private fun findFactorsOf(num: Int): List<Int> {
        val result = mutableSetOf<Int>()
        if (num == 0) return listOf(0)
        val absNum = kotlin.math.abs(num)
        val limit = kotlin.math.sqrt(absNum.toDouble()).toInt()
        for (i in 1..limit) {
            if (absNum % i == 0) {
                result.add(i)
                result.add(-i)
                result.add(absNum / i)
                result.add(-(absNum / i))
            }
        }
        return result.sorted()
    }


    // --- 4. HCF (FSTB) & LCM (GSTK) SOLVER ---
    data class DivisionRow(
        val divisor: Int,
        val quotients: List<Int>,
        val isCommonDivisor: Boolean
    )

    data class HcfLcmResult(
        val hcf: Int,
        val lcm: Int,
        val divisionRows: List<DivisionRow>,
        val steps: List<String>
    )

    private fun findSmallestPrimeFactor(n: Int): Int {
        if (n < 2) return 1
        if (n % 2 == 0) return 2
        if (n % 3 == 0) return 3
        var i = 5
        while (i * i <= n) {
            if (n % i == 0) return i
            if (n % (i + 2) == 0) return i + 2
            i += 6
        }
        return n
    }

    private fun findSmallestPrimeFactorAtLeast(n: Int, limit: Int): Int {
        if (n < limit) return limit
        var p = findSmallestPrimeFactor(n)
        if (p >= limit) return p
        var temp = n
        while (temp > 1) {
            p = findSmallestPrimeFactor(temp)
            if (p >= limit) return p
            temp /= p
        }
        return n.coerceAtLeast(limit)
    }

    fun solveHcfLcm(nums: List<Int>): HcfLcmResult {
        val numbers = nums.filter { it > 0 }.toMutableList()
        val steps = mutableListOf<String>()

        if (numbers.size < 2) {
            return HcfLcmResult(
                1, 1, emptyList(),
                listOf("Provide at least 2 positive numbers.")
            )
        }

        steps.add("We use the Repeated Division Method (Kaedah Pembahagian Berulang):")
        val divisionRows = mutableListOf<DivisionRow>()

        // 1. Calculate HCF / FSTB
        // Keep dividing as long as a common divisor >= 2 exists for ALL elements
        var currentNumsHcf = numbers.toIntArray()
        var hcfValue = 1
        var prime = 2
        while (prime <= (currentNumsHcf.maxOrNull() ?: 1)) {
            val allDivisible = currentNumsHcf.all { it % prime == 0 }
            if (allDivisible) {
                divisionRows.add(DivisionRow(prime, currentNumsHcf.toList(), true))
                currentNumsHcf = currentNumsHcf.map { it / prime }.toIntArray()
                hcfValue *= prime
                steps.add("Stage (HCF): All numbers in group [${currentNumsHcf.joinToString(", ")}] are divisible by common factor: $prime.")
            } else {
                val minActive = currentNumsHcf.filter { it > 1 }.minOrNull()
                if (minActive == null) {
                    break
                } else {
                    val nextPF = findSmallestPrimeFactorAtLeast(minActive, prime)
                    if (nextPF <= prime) {
                        prime++
                    } else {
                        prime = nextPF
                    }
                }
            }
        }
        steps.add("HCF (FSTB) result is the product of all COMMON divisors: HCF = $hcfValue")

        // 2. Clear numbers until all are 1 for LCM / GSTK
        var lcmNums = numbers.toIntArray()
        var lcmValue = 1
        var divisor = 2
        val lcmDivisionRows = mutableListOf<DivisionRow>()

        while (lcmNums.any { it > 1 }) {
            val anyDivisible = lcmNums.any { it % divisor == 0 }
            if (anyDivisible) {
                val beforeDivision = lcmNums.toList()
                val isCommon = lcmNums.all { it % divisor == 0 }
                lcmNums = lcmNums.map {
                    if (it % divisor == 0) it / divisor else it
                }.toIntArray()
                lcmDivisionRows.add(DivisionRow(divisor, beforeDivision, isCommon))
                lcmValue *= divisor
            } else {
                val activeNums = lcmNums.filter { it > 1 }
                var nextDivisorCandidate = divisor + 1
                if (activeNums.isNotEmpty()) {
                    val minPF = activeNums.map { findSmallestPrimeFactor(it) }.minOrNull() ?: (divisor + 1)
                    nextDivisorCandidate = minPF
                }
                if (nextDivisorCandidate <= divisor) {
                    divisor++
                } else {
                    divisor = nextDivisorCandidate
                }
            }
        }
        // Combine rows for displaying the repeated division
        steps.add("LCM (GSTK) result is the product of ALL divisors used to reduce all numbers to 1:")
        steps.add("LCM = " + lcmDivisionRows.map { it.divisor }.joinToString(" × ") + " = $lcmValue")

        return HcfLcmResult(
            hcf = hcfValue,
            lcm = lcmValue,
            divisionRows = lcmDivisionRows,
            steps = steps
        )
    }


    // --- 5. REGULAR POLYGON SOLVER ---
    data class PolygonResult(
        val interiorAngleSum: Double,
        val eachInteriorAngle: Double,
        val eachExteriorAngle: Double,
        val stepsEn: List<String>,
        val stepsMs: List<String>
    )

    fun solvePolygon(n: Int): PolygonResult {
        if (n < 3) return PolygonResult(0.0, 0.0, 0.0, emptyList(), emptyList())

        val interiorSum = (n - 2) * 180.0
        val eachInterior = interiorSum / n
        val eachExterior = 360.0 / n

        val stepsEn = listOf(
            "1. Polygon sides count (n) = $n",
            "2. Sum of interior angles = (n − 2) × 180° = ($n − 2) × 180° = ${n-2} × 180° = $interiorSum°",
            "3. Since it is a Regular Polygon, all $n interior angles are equal: Size of individual interior angle = $interiorSum° ÷ $n = $eachInterior°",
            "4. Individual exterior angle = 360° ÷ n = 360° ÷ $n = $eachExterior°",
            "5. Alternate check: Interior angle + Exterior angle = $eachInterior° + $eachExterior° = 180° (Supplementary angles)"
        )

        val stepsMs = listOf(
            "1. Bilangan sisi poligon (n) = $n",
            "2. Hasil tambah sudut pedalaman = (n − 2) × 180° = ($n − 2) × 180° = ${n-2} × 180° = $interiorSum°",
            "3. Memandangkan poligon sekata, kesemua $n sudut pedalaman adalah sama: Saiz satu sudut pedalaman = $interiorSum° ÷ $n = $eachInterior°",
            "4. Saiz satu sudut peluaran = 360° ÷ n = 360° ÷ $n = $eachExterior°",
            "5. Semakan alternatif: Sudut Pedalaman + Sudut Peluaran = $eachInterior° + $eachExterior° = 180° (Sudut penggenap)"
        )

        return PolygonResult(interiorSum, eachInterior, eachExterior, stepsEn, stepsMs)
    }


    // --- 6. CIRCLE CALCULATIONS ---
    data class CircleResult(
        val circumference: Double,
        val area: Double,
        val arcLength: Double,
        val sectorArea: Double,
        val stepsEn: List<String>,
        val stepsMs: List<String>
    )

    fun solveCircle(radius: Double, thetaDegrees: Double): CircleResult {
        // Circumference = 2 * PI * r
        // Area = PI * r^2
        // Arc Length = (theta / 360) * 2 * PI * r
        // Sector Area = (theta / 360) * PI * r^2
        val piToUse = 22.0 / 7.0 // standard Malaysian school curriculum pi approximation!
        val circumference = 2 * piToUse * radius
        val area = piToUse * radius * radius
        val arcValue = (thetaDegrees / 360.0) * circumference
        val sectorValue = (thetaDegrees / 360.0) * area

        val stepsEn = listOf(
            "Note: Using Malaysia school-standard π ≈ 22 ÷ 7 for fractional calculations.",
            "1. Circumference = 2 × π × r = 2 × (22 ÷ 7) × $radius = ${circumference.format(3)}",
            "2. Circle Area = π × r² = (22 ÷ 7) × $radius × $radius = ${area.format(3)}",
            "3. Arc Length (length of chord's boundary) subtending $thetaDegrees° = (θ ÷ 360) × (2 × π × r)",
            "   Arc Length = ($thetaDegrees ÷ 360) × ${circumference.format(3)} = ${arcValue.format(3)}",
            "4. Sector Area (region covered by θ) = (θ ÷ 360) × (π × r²)",
            "   Sector Area = ($thetaDegrees ÷ 360) × ${area.format(3)} = ${sectorValue.format(3)}"
        )

        val stepsMs = listOf(
            "Nota: Menggunakan standard sekolah Malaysia π ≈ 22 ÷ 7 untuk pengiraan pecahan.",
            "1. Lilitan Bulatan = 2 × π × j = 2 × (22 ÷ 7) × $radius = ${circumference.format(3)}",
            "2. Luas Bulatan = π × j² = (22 ÷ 7) × $radius × $radius = ${area.format(3)}",
            "3. Panjang Lengkok (mencakup sudut $thetaDegrees°) = (θ ÷ 360) × (2 × π × j)",
            "   Panjang Lengkok = ($thetaDegrees ÷ 360) × ${circumference.format(3)} = ${arcValue.format(3)}",
            "4. Luas Sektor (kawasan dilitupi sudut θ) = (θ ÷ 360) × (π × j²)",
            "   Luas Sektor = ($thetaDegrees ÷ 360) × ${area.format(3)} = ${sectorValue.format(3)}"
        )

        return CircleResult(circumference, area, arcValue, sectorValue, stepsEn, stepsMs)
    }

    private fun Double.format(digits: Int) = String.format("%.${digits}f", this)


    // --- 7. THREE DIMENSIONAL GEOMETRY (3D SHAPES) ---
    data class Shape3DResult(
        val surfaceArea: Double,
        val volume: Double,
        val stepsEn: List<String>,
        val stepsMs: List<String>
    )

    fun solve3D(shape: String, params: Map<String, Double>): Shape3DResult {
        // keys possible: h (height), r (radius), l (length), w (width), s (slant height)
        val h = params["h"] ?: 0.0
        val r = params["r"] ?: 0.0
        val l = params["l"] ?: 0.0
        val w = params["w"] ?: 0.0
        val s = params["s"] ?: 0.0

        val piToUse = 22.0 / 7.0
        val stepsEn = mutableListOf<String>()
        val stepsMs = mutableListOf<String>()
        var area = 0.0
        var vol = 0.0

        when (shape) {
            "prism" -> {
                // Triangular Prism with standard equilateral triangle base (length l, side w, prism height h)
                // Base area of triangular cross-section = 0.5 * width(base) * vertical height (we can estimate pyramid or simple prism)
                // Let's model a right-angled triangular prism: Base Width w, Base Height l, Prism Length h
                val baseArea = 0.5 * w * l
                val hypotenuse = sqrt(w*w + l*l)
                vol = baseArea * h
                area = (2 * baseArea) + (w * h) + (l * h) + (hypotenuse * h)

                stepsEn.add("Triangular Prism formulas:")
                stepsEn.add("1. Cross-section triangle area = 0.5 × Base Width (w=$w) × Base Height (l=$l) = $baseArea")
                stepsEn.add("2. Hypotenuse of base triangle = √($w² + $l²) = ${hypotenuse.format(3)}")
                stepsEn.add("3. Perimeter of base = $w + $l + ${hypotenuse.format(3)} = ${(w+l+hypotenuse).format(3)}")
                stepsEn.add("4. Volume = Base Triangle Area × Prism Height (h=$h) = $baseArea × $h = ${vol.format(3)}")
                stepsEn.add("5. Surface Area = 2 × Base Triangle Area + (Perimeter × Prism Height)")
                stepsEn.add("   Surface Area = 2 × $baseArea + (${(w+l+hypotenuse).format(3)} × $h) = ${area.format(3)}")

                stepsMs.add("Formula Prisma Segi Tiga:")
                stepsMs.add("1. Luas keratan rentas segi tiga = 0.5 × Lebar Tapak (l=$w) × Tinggi Tapak (p=$l) = $baseArea")
                stepsMs.add("2. Hipotenusa segi tiga tapak = √($w² + $l²) = ${hypotenuse.format(3)}")
                stepsMs.add("3. Perimeter tapak segi tiga = $w + $l + ${hypotenuse.format(3)} = ${(w+l+hypotenuse).format(3)}")
                stepsMs.add("4. Isipadu = Luas Tapak Segi Tiga × Tinggi Prisma (t=$h) = $baseArea × $h = ${vol.format(3)}")
                stepsMs.add("5. Jumlah Luas Permukaan = 2 × Luas Segi Tiga + (Perimeter × Tinggi Prisma)")
                stepsMs.add("   Luas Permukaan = 2 × $baseArea + (${(w+l+hypotenuse).format(3)} × $h) = ${area.format(3)}")
            }
            "pyramid" -> {
                // Square Pyramid (Base length l, Base width l (square), vertical height h, slant height s)
                // If lateral slant height not provided, we can estimate s = sqrt((l/2)^2 + h^2)
                val halfL = l / 2.0
                val slantHeightCalculated = if (s > 0) s else sqrt(halfL*halfL + h*h)
                val baseArea = l * l
                val lateralArea = 4 * (0.5 * l * slantHeightCalculated)
                area = baseArea + lateralArea
                vol = (1.0 / 3.0) * baseArea * h

                stepsEn.add("Square Pyramid formulas:")
                stepsEn.add("1. Square Base Area = l × l = $l × $l = $baseArea")
                stepsEn.add("2. Slant Height (s) of triangular face = √((l ÷ 2)² + h²) = √($halfL² + $h²) = ${slantHeightCalculated.format(3)}")
                stepsEn.add("3. Area of 4 lateral triangular faces = 4 × (0.5 × Base Base l=$l × Slant s=${slantHeightCalculated.format(3)}) = ${lateralArea.format(3)}")
                stepsEn.add("4. Surface Area = Base Area + 4 × (Face Area) = $baseArea + ${lateralArea.format(3)} = ${area.format(3)}")
                stepsEn.add("5. Volume = (1 ÷ 3) × Base Area × Vertical Height (h=$h) = (1 ÷ 3) × $baseArea × $h = ${vol.format(3)}")

                stepsMs.add("Formula Piramid Tapak Segi Empat:")
                stepsMs.add("1. Luas Tapak Segi Empat = p × p = $l × $l = $baseArea")
                stepsMs.add("2. Tinggi Serong muka segi tiga (s) = √((p ÷ 2)² + t²) = √($halfL² + $h²) = ${slantHeightCalculated.format(3)}")
                stepsMs.add("3. Luas 4 muka segi tiga condong = 4 × (0.5 × p=$l × s=${slantHeightCalculated.format(3)}) = ${lateralArea.format(3)}")
                stepsMs.add("4. Jumlah Luas Permukaan = Luas Tapak + Luas 4 Segi Tiga = $baseArea + ${lateralArea.format(3)} = ${area.format(3)}")
                stepsMs.add("5. Isipadu = (1 ÷ 3) × Luas Tapak × Tinggi Tegak (t=$h) = (1 ÷ 3) × $baseArea × $h = ${vol.format(3)}")
            }
            "cylinder" -> {
                // Cylinder (Radius r, Height h)
                val baseArea = piToUse * r * r
                val curvedArea = 2 * piToUse * r * h
                area = (2 * baseArea) + curvedArea
                vol = baseArea * h

                stepsEn.add("Cylinder formulas:")
                stepsEn.add("1. Circular Base Area = π × r² = (22 ÷ 7) × $r² = ${baseArea.format(3)}")
                stepsEn.add("2. Curved Surface Area = 2 × π × r × h = 2 × (22 ÷ 7) × $r × $h = ${curvedArea.format(3)}")
                stepsEn.add("3. Total Surface Area = 2 × Base Area + Curved Area = 2 × ${baseArea.format(3)} + ${curvedArea.format(3)} = ${area.format(3)}")
                stepsEn.add("4. Volume = Base Area × Height = ${baseArea.format(3)} × $h = ${vol.format(3)}")

                stepsMs.add("Formula Silinder:")
                stepsMs.add("1. Luas Tapak Bulatan = π × j² = (22 ÷ 7) × $r² = ${baseArea.format(3)}")
                stepsMs.add("2. Luas Permukaan Melengkung = 2 × π × j × t = 2 × (22 ÷ 7) × $r × $h = ${curvedArea.format(3)}")
                stepsMs.add("3. Jumlah Luas Permukaan = 2 × Luas Tapak + Luas Melengkung = 2 × ${baseArea.format(3)} + ${curvedArea.format(3)} = ${area.format(3)}")
                stepsMs.add("4. Isipadu = Luas Tapak × Tinggi = ${baseArea.format(3)} × $h = ${vol.format(3)}")
            }
            "cone" -> {
                // Cone (Radius r, Height h, Slant s)
                val slantHeightCalculated = if (s > 0) s else sqrt(r*r + h*h)
                val baseArea = piToUse * r * r
                val curvedArea = piToUse * r * slantHeightCalculated
                area = baseArea + curvedArea
                vol = (1.0 / 3.0) * baseArea * h

                stepsEn.add("Cone formulas:")
                stepsEn.add("1. Circular Base Area = π × r² = (22 ÷ 7) × $r² = ${baseArea.format(3)}")
                stepsEn.add("2. Slant Height (s) = √(r² + h²) = √($r² + $h²) = ${slantHeightCalculated.format(3)}")
                stepsEn.add("3. Curved Surface Area = π × r × s = (22 ÷ 7) × $r × ${slantHeightCalculated.format(3)} = ${curvedArea.format(3)}")
                stepsEn.add("4. Total Surface Area = Base Area + Curved Area = ${baseArea.format(3)} + ${curvedArea.format(3)} = ${area.format(3)}")
                stepsEn.add("5. Volume = (1 ÷ 3) × Base Area × Height = (1 ÷ 3) × ${baseArea.format(3)} × $h = ${vol.format(3)}")

                stepsMs.add("Formula Kon:")
                stepsMs.add("1. Luas Tapak Bulatan = π × j² = (22 ÷ 7) × $r² = ${baseArea.format(3)}")
                stepsMs.add("2. Tinggi Serong (s) = √(j² + t²) = √($r² + $h²) = ${slantHeightCalculated.format(3)}")
                stepsMs.add("3. Luas Permukaan Melengkung = π × j × s = (22 ÷ 7) × $r × ${slantHeightCalculated.format(3)} = ${curvedArea.format(3)}")
                stepsMs.add("4. Jumlah Luas Permukaan = Luas Tapak + Luas Melengkung = ${baseArea.format(3)} + ${curvedArea.format(3)} = ${area.format(3)}")
                stepsMs.add("5. Isipadu = (1 ÷ 3) × Luas Tapak × Tinggi = (1 ÷ 3) × ${baseArea.format(3)} × $h = ${vol.format(3)}")
            }
            "sphere" -> {
                // Sphere (Radius r)
                area = 4 * piToUse * r * r
                vol = (4.0 / 3.0) * piToUse * r * r * r

                stepsEn.add("Sphere formulas:")
                stepsEn.add("1. Surface Area = 4 × π × r² = 4 × (22 ÷ 7) × $r² = ${area.format(3)}")
                stepsEn.add("2. Volume = (4 ÷ 3) × π × r³ = (4 ÷ 3) × (22 ÷ 7) × $r³ = ${vol.format(3)}")

                stepsMs.add("Formula Sfera:")
                stepsMs.add("1. Jumlah Luas Permukaan = 4 × π × j² = 4 × (22 ÷ 7) × $r² = ${area.format(3)}")
                stepsMs.add("2. Isipadu = (4 ÷ 3) × π × j³ = (4 ÷ 3) × (22 ÷ 7) × $r³ = ${vol.format(3)}")
            }
        }

        return Shape3DResult(area, vol, stepsEn, stepsMs)
    }
}
