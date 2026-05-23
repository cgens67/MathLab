package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.example.ui.theme.appRoundedCornerShape as RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.Tingkatan2MathSuite
import com.example.ui.components.CircleDiagram
import com.example.ui.components.NetsVisualizer
import com.example.ui.components.PolygonDiagram
import com.example.ui.settings.Localization
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape
import com.example.ui.components.LoadingIndicator

// ============================================
// 1. PATTERNS AND SEQUENCES SCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternsScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        if (currentLanguage == "English") "Sequence Solver" else if (currentLanguage == "Chinese") "序列求解" else "Penyelesai Jujukan",
        if (currentLanguage == "English") "Fibonacci Sequence" else if (currentLanguage == "Chinese") "斐波那契" else "Jujukan Fibonacci",
        if (currentLanguage == "English") "Pascal's Triangle" else if (currentLanguage == "Chinese") "帕斯卡三角" else "Segi Tiga Pascal"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("topic_patterns", currentLanguage)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("patterns_back")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Elegant M3 Primary Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth().testTag("patterns_tab_row")
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        modifier = Modifier.testTag("patterns_tab_$index")
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> SequenceSolverTab(currentLanguage)
                    1 -> FibonacciVisualizerTab(currentLanguage)
                    2 -> PascalTriangleVisualizerTab(currentLanguage)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequenceSolverTab(currentLanguage: String) {
    var rawInput by remember { mutableStateOf("2, 5, 8, 11") }
    var result by remember { mutableStateOf<Tingkatan2MathSuite.SequenceResult?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = Localization.get("enter_seq", currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = rawInput,
                    onValueChange = {
                        rawInput = it
                        errorText = null
                    },
                    placeholder = { Text("e.g. 2, 5, 8, 11") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("seq_input_text")
                )

                Button(
                    onClick = {
                        val parsed = rawInput.split(",")
                            .mapNotNull { it.trim().toIntOrNull() }
                        if (parsed.size >= 3) {
                            coroutineScope.launch {
                                isLoading = true
                                kotlinx.coroutines.delay(650) // Sweet simulated parsing indicator
                                result = Tingkatan2MathSuite.solveSequence(parsed)
                                errorText = null
                                isLoading = false
                            }
                        } else {
                            errorText = Localization.get("seq_error", currentLanguage)
                            result = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_seq_solve")
                ) {
                    Icon(Icons.Default.Calculate, contentDescription = "Solve")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Localization.get("solve", currentLanguage))
                }

                errorText?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Custom M3 Loading Indicator usage for Patterns standard calculations
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        AnimatedVisibility(
            visible = !isLoading && result != null,
            enter = fadeIn() + expandVertically() + slideInVertically(initialOffsetY = { 40 }),
            exit = fadeOut() + shrinkVertically()
        ) {
            val res = result ?: return@AnimatedVisibility
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = Localization.get("pattern_is", currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = if (currentLanguage == "Bahasa Melayu") res.patternDescriptionMs 
                               else if (currentLanguage == "Chinese") {
                                   if (res.patternDescriptionEn.contains("decreasing", ignoreCase = true)) "递减等差数列"
                                   else "等差数列"
                               } else res.patternDescriptionEn,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (currentLanguage == "Chinese") "数列项排布与公差：" else "Interactive Sequence & Common Differences:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val parsedNums = rawInput.split(",").mapNotNull { it.trim().toIntOrNull() }
                        parsedNums.forEachIndexed { idx, num ->
                            var itemVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(rawInput, isLoading) {
                                itemVisible = false
                                kotlinx.coroutines.delay(idx * 100L)
                                itemVisible = true
                            }
                            AnimatedVisibility(
                                visible = itemVisible,
                                enter = scaleIn(animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f)) + fadeIn()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = num.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            
                            if (idx < parsedNums.size - 1) {
                                val diff = parsedNums[idx + 1] - num
                                var diffVisible by remember { mutableStateOf(false) }
                                LaunchedEffect(rawInput, isLoading) {
                                    diffVisible = false
                                    kotlinx.coroutines.delay(idx * 100L + 50L)
                                    diffVisible = true
                                }
                                AnimatedVisibility(
                                    visible = diffVisible,
                                    enter = fadeIn() + slideInVertically(initialOffsetY = { -15 })
                                ) {
                                    Text(
                                        text = if (diff >= 0) " +$diff " else " $diff ",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.padding(horizontal = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = Localization.get("general_term", currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                    ) {
                        Text(
                            text = res.formula,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp).fillMaxWidth()
                        )
                    }

                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        text = "${Localization.get("explanation", currentLanguage)}:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    val stepsToShow = if (currentLanguage == "Bahasa Melayu") res.stepsMs else res.stepsEn
                    stepsToShow.forEach { step ->
                        val translatedStep = if (currentLanguage == "Chinese") {
                            var s = step
                            if (s.contains("Common difference", ignoreCase = true)) {
                                val d = s.substringAfter("d = ").trim()
                                "公差： d = $d"
                            } else if (s.contains("First term", ignoreCase = true)) {
                                val a = s.substringAfter("a = ").trim()
                                "首项元素： a = $a"
                            } else if (s.contains("Substitute into general formula", ignoreCase = true)) {
                                "代入等差通项公式： Tn = a + (n - 1)d"
                            } else if (s.contains("Simplify", ignoreCase = true)) {
                                "最简通项公式： Tn = $s"
                            } else {
                                s
                            }
                        } else step

                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(translatedStep, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FibonacciVisualizerTab(currentLanguage: String) {
    var terms by remember { mutableStateOf(8f) }
    val n = terms.toInt()
    val scrollState = rememberScrollState()
    
    val fibList = remember(n) {
        val list = mutableListOf(1, 1)
        for (i in 2 until n) {
            list.add(list[i - 1] + list[i - 2])
        }
        list.take(n)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (currentLanguage == "English") "Fibonacci Sequence Generator" 
                           else if (currentLanguage == "Chinese") "斐波那契数列生成器"
                           else "Penjana Jujukan Fibonacci",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = if (currentLanguage == "English") 
                        "Rule: Each term is the sum of the two preceding ones. Formula: Fn = Fn-1 + Fn-2"
                        else if (currentLanguage == "Chinese")
                        "规律：除首两项外，每个数字是前两个数字之和。公式：Fn = Fn-1 + Fn-2"
                        else "Pola: Setiap nombor selepas dua sebutan pertama ialah hasil tambah dua sebutan sebelumnya. Rumus: Fn = Fn-1 + Fn-2",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Terms: $n", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = terms,
                        onValueChange = { terms = it },
                        valueRange = 3f..12f,
                        steps = 8,
                        modifier = Modifier.weight(1f).testTag("fib_terms_slider")
                    )
                }
                
                Text(
                    text = if (currentLanguage == "Chinese") "数列项排布：" else "Generated Sequence Terms:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp)
                ) {
                    fibList.forEachIndexed { idx, value ->
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(n) {
                            isVisible = false
                            kotlinx.coroutines.delay(idx * 70L)
                            isVisible = true
                        }
                        
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = scaleIn(animationSpec = spring(0.75f, 300f)) + fadeIn()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = value.toString(),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontSize = 16.sp
                                    )
                                }
                                Text(
                                    text = "F$idx",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                        
                        if (idx < fibList.size - 1) {
                            Text("+", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
                
                if (n >= 4) {
                    val last2 = fibList[n - 1].toDouble()
                    val last1 = fibList[n - 2].toDouble()
                    val ratio = last2 / last1
                    
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (currentLanguage == "English") "The Golden Ratio (\u03c6) Convergence"
                                       else if (currentLanguage == "Chinese") "黄金比例 (\u03c6) 收敛规律"
                                       else "Konvergens ke arah Nisbah Keemasan (\u03c6)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (currentLanguage == "English")
                                    "As terms increase, the ratio F(n)/F(n-1) approaches \u03c6 \u2248 1.61803...\nCalculation: F(${n-1})/F(${n-2}) = $last2 ÷ $last1 = ${String.format("%.5f", ratio)}"
                                    else if (currentLanguage == "Chinese")
                                    "随着项数增加，相邻项的比值 F(n)/F(n-1) 逼近 \u03c6 \u2248 1.61803...\n计算：F(${n-1})/F(${n-2}) = $last2 ÷ $last1 = ${String.format("%.5f", ratio)}"
                                    else "Nisbah dua sebutan bersebelahan F(n) ÷ F(n-1) akan menghampiri \u03c6 \u2248 1.61803...\nNisbah: F(${n-1}) ÷ F(${n-2}) = $last2 ÷ $last1 = ${String.format("%.5f", ratio)}",
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }

                Text(
                    text = if (currentLanguage == "English") "Fibonacci Geometric Squares Visual:" 
                           else if (currentLanguage == "Chinese") "斐波那契几何堆叠正方形："
                           else "Visual Geometrik Segi Empat Fibonacci:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        fibList.forEachIndexed { idx, valItem ->
                            val scaleHeight = (valItem * 8).coerceAtMost(140)
                            val scaleWidth = (valItem * 6).coerceIn(12, 44)
                            
                            var boxHeight by remember { mutableStateOf(0f) }
                            LaunchedEffect(n) {
                                boxHeight = 0f
                                kotlinx.coroutines.delay(idx * 75L)
                                boxHeight = scaleHeight.toFloat()
                            }
                            
                            val animBoxHeight by animateFloatAsState(
                                targetValue = boxHeight,
                                animationSpec = spring(dampingRatio = 0.75f, stiffness = 150f)
                            )
                            
                            Box(
                                modifier = Modifier
                                    .width(scaleWidth.dp)
                                    .height(animBoxHeight.dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        if (idx % 2 == 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = valItem.toString(),
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PascalTriangleVisualizerTab(currentLanguage: String) {
    var rowsVal by remember { mutableStateOf(6f) }
    val rowCount = rowsVal.toInt()
    
    var highlightOdds by remember { mutableStateOf(false) }
    var showRowSums by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    
    val triangle = remember(rowCount) {
        val result = mutableListOf<List<Int>>()
        for (i in 0 until rowCount) {
            val row = mutableListOf<Int>()
            for (j in 0..i) {
                if (j == 0 || j == i) {
                    row.add(1)
                } else {
                    val prevRow = result[i - 1]
                    row.add(prevRow[j - 1] + prevRow[j])
                }
            }
            result.add(row)
        }
        result
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (currentLanguage == "English") "Pascal's Triangle Visualizer" 
                           else if (currentLanguage == "Chinese") "帕斯卡/杨辉三角形"
                           else "Segi Tiga Pascal Interaktif",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = if (currentLanguage == "English")
                        "Rule: The sum of two adjacent items in a row determines the number underneath. Great for algebra binom expansions (a + b)^n."
                        else if (currentLanguage == "Chinese")
                        "规律：上一排两个相邻单元数字相加，即为正下方的数。杨辉三角的每行对应二项式展开 (a + b)^n 的展开系数。"
                        else "Pola: Hasil tambah dua nombor bersebelahan di baris atas menentukan nombor di baris bawah.\nSangat membantu dalam kembangan binomial (a + b)^n.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Rows: $rowCount", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = rowsVal,
                        onValueChange = { rowsVal = it },
                        valueRange = 3f..8f,
                        steps = 4,
                        modifier = Modifier.weight(1f).testTag("pascal_rows_slider")
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier.weight(1f).clickable { highlightOdds = !highlightOdds }
                    ) {
                        Checkbox(checked = highlightOdds, onCheckedChange = { highlightOdds = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentLanguage == "English") "Fractal Pattern" 
                                   else if (currentLanguage == "Chinese") "谢尔宾斯基分形"
                                   else "Fraktal Sierpinski", 
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier.weight(1f).clickable { showRowSums = !showRowSums }
                    ) {
                        Checkbox(checked = showRowSums, onCheckedChange = { showRowSums = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentLanguage == "English") "Sums (2ⁿ)" 
                                   else if (currentLanguage == "Chinese") "行项之和 (2ⁿ)"
                                   else "Jumlah Baris (2ⁿ)", 
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        triangle.forEachIndexed { rIdx, row ->
                            var rowVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(rowCount) {
                                rowVisible = false
                                kotlinx.coroutines.delay(rIdx * 75L)
                                rowVisible = true
                            }
                            
                            AnimatedVisibility(
                                visible = rowVisible,
                                enter = fadeIn() + slideInVertically(initialOffsetY = { -20 })
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    row.forEachIndexed { cIdx, element ->
                                        val isOdd = element % 2 != 0
                                        val highlight = highlightOdds && isOdd
                                        
                                        val bubbleBg = when {
                                            highlight -> MaterialTheme.colorScheme.primary
                                            isOdd -> MaterialTheme.colorScheme.secondaryContainer
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                        val bubbleText = when {
                                            highlight -> MaterialTheme.colorScheme.onPrimary
                                            isOdd -> MaterialTheme.colorScheme.onSecondaryContainer
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                        
                                        Box(
                                            modifier = Modifier
                                                .size(width = 38.dp, height = 34.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(bubbleBg),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = element.toString(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = bubbleText,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    
                                    if (showRowSums) {
                                        val sum = row.sum()
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(width = 44.dp, height = 28.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "=$sum",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================
// 2. POLYGONS SCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolygonsScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    var sides by remember { mutableStateOf(5) } // default pentagon (5 sides)
    val polyResult = remember(sides) { Tingkatan2MathSuite.solvePolygon(sides) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("topic_polygons", currentLanguage)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("polygons_back")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live Interactive Vector Diagram of the chosen N-sided polygon!
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(260.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    PolygonDiagram(sides = sides, modifier = Modifier.fillMaxSize())
                }
            }

            // Slider to change sides count
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Localization.get("polygon_sides", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                "n = $sides",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Slider(
                        value = sides.toFloat(),
                        onValueChange = { sides = it.toInt() },
                        valueRange = 3f..12f,
                        steps = 8, // 3,4,5,6,7,8,9,10,11,12 side options
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .testTag("polygon_sides_slider")
                    )

                    val shapeNames = when (sides) {
                        3 -> "Segi Tiga (Triangle)"
                        4 -> "Segi Empat (Quadrilateral)"
                        5 -> "Pentagon (Pentagon)"
                        6 -> "Heksagon (Hexagon)"
                        7 -> "Heptagon (Heptagon)"
                        8 -> "Oktagon (Octagon)"
                        9 -> "Nonagon (Nonagon)"
                        10 -> "Dekagon (Decagon)"
                        else -> "Poligon Sisi $sides"
                    }

                    Text(
                        text = "Shape: $shapeNames",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Results Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${Localization.get("result", currentLanguage)}:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Sum of Angles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("interior_sum", currentLanguage), style = MaterialTheme.typography.bodyMedium)
                        Text("${polyResult.interiorAngleSum.toInt()}°", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    // Individual Interior Angle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("each_interior", currentLanguage), style = MaterialTheme.typography.bodyMedium)
                        Text("${String.format("%.1f", polyResult.eachInteriorAngle)}°", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    // Individual Exterior Angle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("each_exterior", currentLanguage), style = MaterialTheme.typography.bodyMedium)
                        Text("${String.format("%.1f", polyResult.eachExteriorAngle)}°", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = Localization.get("calculation_steps", currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )

                    val stepsToUse = if (currentLanguage == "Bahasa Melayu") polyResult.stepsMs else polyResult.stepsEn
                    stepsToUse.forEach { s ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black)
                            Text(s, style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}


// ============================================
// 3. CIRCLES SCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CirclesScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    var radius by remember { mutableStateOf(7.0) } // School standard radius division compatible with 7
    var angle by remember { mutableStateOf(90f) }   // default quarter quadrant (90 degrees)
    var piType by remember { mutableStateOf("22/7") }

    val circleResult = remember(radius, angle, piType) { Tingkatan2MathSuite.solveCircle(radius, angle.toDouble(), piType) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("topic_circles", currentLanguage)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("circles_back")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live Vector Diagram of Circular Sector & Chord lines
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(260.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircleDiagram(subtendedAngle = angle, modifier = Modifier.fillMaxSize())
                }
            }

            // Interactive sliders for radius and sweep angle
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Radius Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Localization.get("circle_radius", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "r = ${String.format("%.1f", radius)} cm",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Slider(
                        value = radius.toFloat(),
                        onValueChange = { radius = it.toDouble() },
                        valueRange = 1f..21f,
                        steps = 20,
                        modifier = Modifier.testTag("circle_radius_slider")
                    )

                    // Theta Angle Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Localization.get("circle_angle", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "θ = ${angle.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Slider(
                        value = angle,
                        onValueChange = { angle = it },
                        valueRange = 10f..360f,
                        modifier = Modifier.testTag("circle_angle_slider")
                    )
                }
            }

            // Pi Selector Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(24.dp)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentLanguage == "English") "Selected Pi (π) Value:"
                                   else if (currentLanguage == "Chinese") "选用圆周率 (π) 值："
                                   else "Pilihan Nilai Pi (π):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = if (piType == "22/7") "π ≈ 22/7" else "π ≈ 3.142",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = if (currentLanguage == "English") "School questions specify whether to use 22/7 (fractional) or 3.142 (decimal). Choose to match your textbook:"
                               else if (currentLanguage == "Chinese") "学校题目通常会指定使用 22/7 或 3.142，请选择与您教科书题目匹配的值："
                               else "Soalan sekolah menentukan sama ada ingin menggunakan 22/7 atau 3.142. Pilih untuk dipadankan:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { piType = "22/7" },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (piType == "22/7") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "22 / 7",
                                color = if (piType == "22/7") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { piType = "3.142" },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (piType == "3.142") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "3.142",
                                color = if (piType == "3.142") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Results Board
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${Localization.get("result", currentLanguage)}:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Circumference
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("circumference", currentLanguage), style = MaterialTheme.typography.bodySmall)
                        Text("${String.format("%.2f", circleResult.circumference)} cm", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    // Circle Area
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("circle_area", currentLanguage), style = MaterialTheme.typography.bodySmall)
                        Text("${String.format("%.2f", circleResult.area)} cm²", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f), modifier = Modifier.padding(vertical = 4.dp))

                    // Arc Length
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("arc_length", currentLanguage), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        Text("${String.format("%.2f", circleResult.arcLength)} cm", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                    }

                    // Sector Area
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("sector_area", currentLanguage), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        Text("${String.format("%.2f", circleResult.sectorArea)} cm²", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = Localization.get("calculation_steps", currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )

                    val stepsToUse = if (currentLanguage == "Bahasa Melayu") circleResult.stepsMs else circleResult.stepsEn
                    stepsToUse.forEach { s ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black)
                            Text(s, style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}


// ============================================
// 4. 3D GEOMETRY SCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeDShapesScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    var selectedShape by remember { mutableStateOf("cylinder") } // default cylinder bounds
    var hVal by remember { mutableStateOf(10.0) } // height
    var rVal by remember { mutableStateOf(7.0) }  // radius
    var wVal by remember { mutableStateOf(5.0) }  // base width for prisms
    var lVal by remember { mutableStateOf(6.0) }  // base length for pyramids & prisms
    var sVal by remember { mutableStateOf(0.0) }  // custom slant height (auto computed if 0)

    val shapeOptions = listOf(
        Pair("cylinder", Localization.get("cylinder", currentLanguage)),
        Pair("cone", Localization.get("cone", currentLanguage)),
        Pair("sphere", Localization.get("sphere", currentLanguage)),
        Pair("prism", Localization.get("prism", currentLanguage)),
        Pair("pyramid", Localization.get("pyramid", currentLanguage))
    )

    val scoreParams = mapOf(
        "h" to hVal,
        "r" to rVal,
        "w" to wVal,
        "l" to lVal,
        "s" to sVal
    )

    var piType by remember { mutableStateOf("22/7") }

    val shapeResult = remember(selectedShape, hVal, rVal, wVal, lVal, sVal, piType) {
        Tingkatan2MathSuite.solve3D(selectedShape, scoreParams, piType)
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("topic_shapes", currentLanguage)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("shapes_back")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Unfolded flat net layout matching standard school geometry
            Text(
                text = "${Localization.get("nets_visualizer", currentLanguage)}:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(240.dp)
                    .border(2.dp, MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(24.dp))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    NetsVisualizer(shape = selectedShape, modifier = Modifier.fillMaxSize())
                }
            }

            // Selection buttons of shape
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = Localization.get("shape_type", currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Scrollable chip bar for shapes
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        shapeOptions.forEach { pair ->
                            val isSelected = pair.first == selectedShape
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedShape = pair.first },
                                label = { Text(pair.second, maxLines = 1) },
                                modifier = Modifier.testTag("chip_shape_${pair.first}")
                            )
                        }
                    }
                }
            }

            // Interactive parameters sliders based on shape requirements
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (currentLanguage == "English") "Dimensions Parameters:"
                               else if (currentLanguage == "Chinese") "尺寸维度参数："
                               else "Parameter Dimensi:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (selectedShape == "cylinder" || selectedShape == "cone" || selectedShape == "sphere") {
                        // Radius Slider
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Localization.get("r_val", currentLanguage), fontWeight = FontWeight.Bold)
                            Text("${rVal.toInt()} cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = rVal.toFloat(),
                            onValueChange = { rVal = it.toDouble() },
                            valueRange = 1f..14f,
                            steps = 13,
                            modifier = Modifier.testTag("shape_slider_r")
                        )
                    }

                    if (selectedShape == "cylinder" || selectedShape == "cone" || selectedShape == "prism" || selectedShape == "pyramid") {
                        // Height Slider
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Localization.get("h_val", currentLanguage), fontWeight = FontWeight.Bold)
                            Text("${hVal.toInt()} cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = hVal.toFloat(),
                            onValueChange = { hVal = it.toDouble() },
                            valueRange = 1f..20f,
                            steps = 19,
                            modifier = Modifier.testTag("shape_slider_h")
                        )
                    }

                    if (selectedShape == "prism" || selectedShape == "pyramid") {
                        // Width Slider (Base side width)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Localization.get("w_val", currentLanguage), fontWeight = FontWeight.Bold)
                            Text("${wVal.toInt()} cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = wVal.toFloat(),
                            onValueChange = { wVal = it.toDouble() },
                            valueRange = 1f..15f,
                            steps = 14,
                            modifier = Modifier.testTag("shape_slider_w")
                        )

                        // Length Slider (base height/length)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Localization.get("l_val", currentLanguage), fontWeight = FontWeight.Bold)
                            Text("${lVal.toInt()} cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Slider(
                            value = lVal.toFloat(),
                            onValueChange = { lVal = it.toDouble() },
                            valueRange = 1f..15f,
                            steps = 14,
                            modifier = Modifier.testTag("shape_slider_l")
                        )
                    }
                }
            }

            // Pi Selector Card (3D Geometry)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(24.dp)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentLanguage == "English") "Selected Pi (π) Value:"
                                   else if (currentLanguage == "Chinese") "选用圆周率 (π) 值："
                                   else "Pilihan Nilai Pi (π):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = if (piType == "22/7") "π ≈ 22/7" else "π ≈ 3.142",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = if (currentLanguage == "English") "Toggle to choose which Pi value to use for surface area and volume:"
                               else if (currentLanguage == "Chinese") "切换计算表面积和体积时选用的圆周率值："
                               else "Tukar untuk memilih nilai Pi untuk luas permukaan dan isipadu:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { piType = "22/7" },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (piType == "22/7") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "22 / 7",
                                color = if (piType == "22/7") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { piType = "3.142" },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (piType == "3.142") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "3.142",
                                color = if (piType == "3.142") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Results calculations Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${Localization.get("result", currentLanguage)}:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Surface Area
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("surface_area", currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text("${String.format("%.2f", shapeResult.surfaceArea)} cm²", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    }

                    // Volume
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(Localization.get("volume", currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text("${String.format("%.2f", shapeResult.volume)} cm³", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.tertiary)
                    }

                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = Localization.get("calculation_steps", currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )

                    val stepsToUse = if (currentLanguage == "Bahasa Melayu") shapeResult.stepsMs else shapeResult.stepsEn
                    stepsToUse.forEach { s ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black)
                            Text(s, style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
