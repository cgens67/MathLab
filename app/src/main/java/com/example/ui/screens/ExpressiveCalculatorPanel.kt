package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.Fraction

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpressiveCalculatorPanel(currentLanguage: String) {
    var activeMode by remember { mutableStateOf(0) } // 0: Expand, 1: Factorise, 2: Simplify Fractions, 3: Solve Linear
    val scrollState = rememberScrollState()

    val modes = listOf(
        if (currentLanguage == "English") "Expand" else if (currentLanguage == "Chinese") "展开式" else "Kembangkan",
        if (currentLanguage == "English") "Factorise" else if (currentLanguage == "Chinese") "因式分解" else "Pemfaktoran",
        if (currentLanguage == "English") "Simplify Terms" else if (currentLanguage == "Chinese") "最简分数" else "Permudahkan",
        if (currentLanguage == "English") "Solve Equation" else if (currentLanguage == "Chinese") "解方程" else "Selesaikan"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mode selector
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (currentLanguage == "English") "Textbook Calculator Method:"
                           else if (currentLanguage == "Chinese") "教科书计算模式："
                           else "Kaedah Kalkulator Buku Teks:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    modes.forEachIndexed { index, title ->
                        FilterChip(
                            selected = activeMode == index,
                            onClick = { activeMode = index },
                            label = { Text(title, style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier.testTag("calc_mode_$index")
                        )
                    }
                }
            }
        }

        // Selected panel content
        when (activeMode) {
            0 -> ExpandFractionCalculator(currentLanguage)
            1 -> FactoriseFractionCalculator(currentLanguage)
            2 -> SimplifyFractionCalculator(currentLanguage)
            3 -> SolveFractionCalculator(currentLanguage)
        }
    }
}

// Helper bar for input fraction entries
@Composable
fun FractionInputHelperBar(
    onAppend: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Fraction Helper Key:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            AssistChip(
                onClick = { onAppend("/") },
                label = { Text("/", fontWeight = FontWeight.Bold) }
            )
            AssistChip(
                onClick = { onAppend("1/2") },
                label = { Text("1/2") }
            )
            AssistChip(
                onClick = { onAppend("1/3") },
                label = { Text("1/3") }
            )
            AssistChip(
                onClick = { onAppend("2/3") },
                label = { Text("2/3") }
            )
            AssistChip(
                onClick = { onAppend("3/4") },
                label = { Text("3/4") }
            )
        }
    }
}

// 1. Expand Panel
@Composable
fun ExpandFractionCalculator(currentLanguage: String) {
    var aStr by remember { mutableStateOf("1/2") }
    var bStr by remember { mutableStateOf("3") }
    var cStr by remember { mutableStateOf("2/3") }
    var dStr by remember { mutableStateOf("-4") }
    var lastFocused by remember { mutableStateOf(0) } // 0:a, 1:b, 2:c, 3:d

    val explanationSteps = remember(aStr, bStr, cStr, dStr) {
        try {
            val a = Fraction.parse(aStr)
            val b = Fraction.parse(bStr)
            val c = Fraction.parse(cStr)
            val d = Fraction.parse(dStr)

            val ac = a * c
            val ad = a * d
            val bc = b * c
            val bd = b * d
            val adPlusBc = ad + bc

            // Strings
            val aText = a.toMathString()
            val bText = b.toMathString()
            val cText = c.toMathString()
            val dText = d.toMathString()

            val acText = ac.toMathString()
            val bdText = bd.toMathString()
            val adPlusBcText = adPlusBc.toMathString()

            val eq1 = "(${aText}x + $bText)(${cText}x + $dText)"
            val eq2 = "($aText x × $cText x) + ($aText x × $dText) + ($bText × $cText x) + ($bText × $dText)"
            
            val steps = mutableListOf<String>()
            steps.add("Expression: $eq1")
            steps.add("Using FOIL distribution: (ax + b)(cx + d) = ax·cx + ax·d + b·cx + b·d")
            steps.add("Step 1 (First): Multiply coefficients: $aText × $cText = $acText x²")
            steps.add("Step 2 (Outers): Multiply coefficients: $aText × $dText = (${(a*d).toMathString()})x")
            steps.add("Step 3 (Inners): Multiply coefficients: $bText × $cText = (${(b*c).toMathString()})x")
            steps.add("Step 4 (Last): Multiply constant: $bText × $dText = $bdText")
            steps.add("Step 5 (Combine like terms): (${(a*d).toMathString()})x + (${(b*c).toMathString()})x = $adPlusBcText x")

            val sign1 = if (adPlusBc.num >= 0) " + " else " - "
            val term1 = if (adPlusBc.num >= 0) adPlusBcText else adPlusBc.times(Fraction(-1, 1)).toMathString()
            val sign2 = if (bd.num >= 0) " + " else " - "
            val term2 = if (bd.num >= 0) bdText else bd.times(Fraction(-1, 1)).toMathString()

            val ans = "${acText}x²$sign1${term1}x$sign2$term2"
            steps.add("Final Simplified Equation: $ans")
            Pair(ans, steps)
        } catch (e: Exception) {
            Pair("Invalid coefficients", listOf("Please enter valid integers or fractions (e.g. 1/2)."))
        }
    }

    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Expand Fractional Terms: (ax + b)(cx + d)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = aStr,
                    onValueChange = { aStr = it },
                    label = { Text("a") },
                    modifier = Modifier.weight(1f).testTag("calc_a"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bStr,
                    onValueChange = { bStr = it },
                    label = { Text("b") },
                    modifier = Modifier.weight(1f).testTag("calc_b"),
                    singleLine = true
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = cStr,
                    onValueChange = { cStr = it },
                    label = { Text("c") },
                    modifier = Modifier.weight(1f).testTag("calc_c"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = dStr,
                    onValueChange = { dStr = it },
                    label = { Text("d") },
                    modifier = Modifier.weight(1f).testTag("calc_d"),
                    singleLine = true
                )
            }

            FractionInputHelperBar { append ->
                // Custom insertion helper
                aStr = aStr + append
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Solution & Steps:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = explanationSteps.first,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )
            }

            explanationSteps.second.forEach { step ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Black)
                    Text(step, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

// 2. Factorise Panel
@Composable
fun FactoriseFractionCalculator(currentLanguage: String) {
    var aStr by remember { mutableStateOf("3") }
    var bStr by remember { mutableStateOf("5") }
    var cStr by remember { mutableStateOf("-2") }

    val factResult = remember(aStr, bStr, cStr) {
        val a = aStr.toIntOrNull() ?: 1
        val b = bStr.toIntOrNull() ?: 0
        val c = cStr.toIntOrNull() ?: 0

        val steps = mutableListOf<String>()
        steps.add("Expression: ${a}x² + ${if (b >= 0) "+" else ""}${b}x + ${if (c >= 0) "+" else ""}$c")

        if (a == 0) {
            Pair("Linear or invalid", listOf("Coeff 'a' cannot be zero for quadratic factorisation."))
        } else {
            // Find integers p, q, r, s such that (px + r)(qx + s) = ax^2 + bx + c
            // p*q = a, r*s = c, p*s + q*r = b
            var solved = false
            var pAns = 0
            var qAns = 0
            var rAns = 0
            var sAns = 0

            val aFactors = findFactorsOf(a)
            val cFactors = findFactorsOf(c)

            for (p in aFactors) {
                val q = a / p
                for (r in cFactors) {
                    val s = if (r != 0) c / r else 0
                    if (p * s + q * r == b) {
                        pAns = p
                        qAns = q
                        rAns = r
                        sAns = s
                        solved = true
                        break
                    }
                }
                if (solved) break
            }

            if (solved) {
                steps.add("Using Cross Multiplication Method:")
                steps.add("1. Find factors of constant $c and absolute leading coeff $a.")
                steps.add("2. Try factors: ($pAns, $qAns) for x² and ($rAns, $sAns) for constant.")
                steps.add("3. Multiply cross-wise: ($pAns × $sAns) + ($qAns × $rAns) = ${pAns*sAns} + ${qAns*rAns} = $b (Matches middle term!)")
                
                // Show grid
                steps.add("   Cross Table Layout:")
                steps.add("     $pAns x       $rAns   -->   ${qAns*rAns}x")
                steps.add("     $qAns x       $sAns   -->   ${pAns*sAns}x")
                steps.add("     ---------------------------------")
                steps.add("     ${pAns*qAns}x²      $c          ${b}x")

                val part1 = "(${if (pAns == 1) "" else pAns}x ${if (rAns >= 0) "+ $rAns" else "- ${-rAns}"})"
                val part2 = "(${if (qAns == 1) "" else qAns}x ${if (sAns >= 0) "+ $sAns" else "- ${-sAns}"})"
                val ans = "$part1$part2"
                steps.add("Product expression: $ans")
                Pair(ans, steps)
            } else {
                steps.add("This term does not factorize cleanly into rational numbers.")
                steps.add("Try applying the quadratic discriminant check: b² - 4ac = ${b*b} - 4*($a)*($c) = ${b*b - 4*a*c}")
                Pair("Not factorable cleanly", steps)
            }
        }
    }

    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Factorise Quadratic: ax² + bx + c",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = aStr,
                    onValueChange = { aStr = it },
                    label = { Text("a") },
                    modifier = Modifier.weight(1f).testTag("fact_a")
                )
                OutlinedTextField(
                    value = bStr,
                    onValueChange = { bStr = it },
                    label = { Text("b") },
                    modifier = Modifier.weight(1f).testTag("fact_b")
                )
                OutlinedTextField(
                    value = cStr,
                    onValueChange = { cStr = it },
                    label = { Text("c") },
                    modifier = Modifier.weight(1f).testTag("fact_c")
                )
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Solution & Steps (Cross multiplication):", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = factResult.first,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )
            }

            factResult.second.forEach { step ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Black)
                    Text(step, style = MaterialTheme.typography.bodyMedium, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

// Helper to find factors
private fun findFactorsOf(num: Int): List<Int> {
    if (num == 0) return listOf(0)
    val factors = mutableListOf<Int>()
    val n = kotlin.math.abs(num)
    for (i in 1..n) {
        if (n % i == 0) {
            factors.add(i)
            factors.add(-i)
        }
    }
    return factors.distinct()
}

// 3. Simplify Panel
@Composable
fun SimplifyFractionCalculator(currentLanguage: String) {
    var term1Num by remember { mutableStateOf("1") }
    var term1Den by remember { mutableStateOf("2") }
    var term2Num by remember { mutableStateOf("1") }
    var term2Den by remember { mutableStateOf("3") }
    var operationSign by remember { mutableStateOf("+") } // "+" or "-"

    val simplifyResult = remember(term1Num, term1Den, term2Num, term2Den, operationSign) {
        try {
            val f1 = Fraction(term1Num.toIntOrNull() ?: 1, term1Den.toIntOrNull() ?: 1)
            val f2 = Fraction(term2Num.toIntOrNull() ?: 1, term2Den.toIntOrNull() ?: 1)

            val result = if (operationSign == "+") f1 + f2 else f1 - f2
            val steps = mutableListOf<String>()
            
            steps.add("Term 1: $f1 x")
            steps.add("Term 2: $f2 x")
            steps.add("Operation: ($f1 x) $operationSign ($f2 x)")
            steps.add("Step 1: Find a common denominator: ${f1.den} × ${f2.den} = ${f1.den * f2.den}")
            val term1EqNum = f1.num * f2.den
            val term2EqNum = f2.num * f1.den
            steps.add("Step 2: Balance numerators: term 1 = $term1EqNum, term 2 = $term2EqNum")
            steps.add("Step 3: Combine numerators: $term1EqNum $operationSign $term2EqNum = ${if (operationSign == "+") term1EqNum+term2EqNum else term1EqNum-term2EqNum}")
            steps.add("Step 4: Reduce to simplest exact terms: $result")

            Pair("$result x", steps)
        } catch (e: Exception) {
            Pair("Error: Invalid inputs", listOf("Please enter valid, non-zero denominators."))
        }
    }

    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Simplify Algebraic Fractions: (a/b)x ± (c/d)x",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coefficient 1
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = term1Num,
                        onValueChange = { term1Num = it },
                        label = { Text("Num 1") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("——", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = term1Den,
                        onValueChange = { term1Den = it },
                        label = { Text("Den 1") },
                        singleLine = true
                    )
                }

                // Operator
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { operationSign = if (operationSign == "+") "-" else "+" },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Text(operationSign, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Text("x", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                // Coefficient 2
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = term2Num,
                        onValueChange = { term2Num = it },
                        label = { Text("Num 2") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("——", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = term2Den,
                        onValueChange = { term2Den = it },
                        label = { Text("Den 2") },
                        singleLine = true
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Simplification Outputs:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = simplifyResult.first,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )
            }

            simplifyResult.second.forEach { step ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Black)
                    Text(step, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

// 4. Solve Linear Equation Panel
@Composable
fun SolveFractionCalculator(currentLanguage: String) {
    var aStr by remember { mutableStateOf("1/2") }
    var bStr by remember { mutableStateOf("3") }
    var cStr by remember { mutableStateOf("7") }

    val solveResult = remember(aStr, bStr, cStr) {
        try {
            val a = Fraction.parse(aStr)
            val b = Fraction.parse(bStr)
            val c = Fraction.parse(cStr)

            val steps = mutableListOf<String>()
            steps.add("Equation to solve: ($a)x + ($b) = $c")

            if (a.num == 0) {
                Pair("Error: 'a' cannot be 0", listOf("The coefficient of x must not be zero to solve for x."))
            } else {
                // Step 1: Subtract b from both sides
                val cMinusB = c - b
                steps.add("Step 1: Subtract constant term $b from both sides:")
                steps.add("        ($a)x = $c - ($b)")
                steps.add("        ($a)x = $cMinusB")

                // Step 2: Multiply by inverse of a
                val recA = Fraction(a.den, a.num)
                val ans = cMinusB * recA
                steps.add("Step 2: Multiply both sides by the reciprocal of $a (which is $recA):")
                steps.add("        x = $cMinusB × $recA")
                steps.add("        x = $ans")

                val finalAns = "x = $ans"
                Pair(finalAns, steps)
            }
        } catch (e: Exception) {
            Pair("Parsing Error", listOf("Please enter valid integers or fractions (e.g. 1/2)."))
        }
    }

    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Solve Linear Fraction Equation: ax + b = c",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = aStr,
                    onValueChange = { aStr = it },
                    label = { Text("a") },
                    modifier = Modifier.weight(1f).testTag("solve_a"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bStr,
                    onValueChange = { bStr = it },
                    label = { Text("b") },
                    modifier = Modifier.weight(1f).testTag("solve_b"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = cStr,
                    onValueChange = { cStr = it },
                    label = { Text("c") },
                    modifier = Modifier.weight(1f).testTag("solve_c"),
                    singleLine = true
                )
            }

            FractionInputHelperBar { append ->
                // Custom insertion helper
                aStr = aStr + append
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Textbook Balance Method Steps:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = solveResult.first,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )
            }

            solveResult.second.forEach { step ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Bold)
                    Text(step, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
