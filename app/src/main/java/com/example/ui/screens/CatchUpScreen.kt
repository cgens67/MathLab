package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import com.example.ui.theme.appRoundedCornerShape as RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.*
import com.example.ui.settings.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatchUpScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    var activeTab by remember { mutableStateOf(0) }
    
    val tabs = listOf(
        Pair("Nombor Nisbah\n(Rational Numbers)", Icons.Default.HorizontalRule),
        Pair("Ungkapan Algebra\n(Algebaic Terms)", Icons.Default.Timeline),
        Pair("Persamaan Linear\n(Linear Equations)", Icons.Default.Scale),
        Pair("Poligon Asas\n(Basic Polygons)", Icons.Default.Architecture)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (currentLanguage == "English") "Form 1 Math Booster" else "Peningkatan Matematik T1",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (currentLanguage == "English") "Catch up to Form 2 easily!" else "Kuasai silibus asas Tingkatan 1!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("catchup_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
            // Interactive Tab row matching M3 standards with beautiful dynamic pills
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, (title, icon) ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        },
                        icon = {
                            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    )
                }
            }

            // Scrollable Content Pane showing interactive visualizer for the selected module
            Box(modifier = Modifier.weight(1f)) {
                when (activeTab) {
                    0 -> RationalNumbersSection(currentLanguage)
                    1 -> AlgebraicExpressionsSection(currentLanguage)
                    2 -> LinearEquationsSection(currentLanguage)
                    3 -> BasicPolygonsSection(currentLanguage)
                }
            }
        }
    }
}

// ==========================================
// MODULE 1: RATIONAL NUMBERS (NOMBOR NISBAH)
// ==========================================
@Composable
fun RationalNumbersSection(lang: String) {
    var numValue by remember { mutableStateOf(2f) }
    var scaleFactor by remember { mutableStateOf(1f) }
    
    // Quiz State
    var selectedOption by remember { mutableStateOf(-1) }
    var showQuizFeedback by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (lang == "English") "Negative Numbers & Operations" else "Nombor Negatif & Operasi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (lang == "English") 
                            "Multiplying integers has easy signs rules:\n\u2022 (+) \u00d7 (+) = (+)\n\u2022 (-) \u00d7 (-) = (+)\n\u2022 (+) \u00d7 (-) = (-)\nAdding negative integers moves you to the left on the number line!"
                            else 
                            "Pendaraban integer mematuhi tanda mudah:\n\u2022 (+) \u00d7 (+) = (+)\n\u2022 (-) \u00d7 (-) = (+)\n\u2022 (+) \u00d7 (-) = (-)\nPenambahan integer negatif menggerakkan anda ke kiri pada garis nombor!",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Interactive Number Line Canvas!
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (lang == "English") "Interactive Number Line" else "Garis Nombor Interaktif",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Calculation dynamic preview
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = if (lang == "English") 
                                "0 + ($numValue) \u00d7 $scaleFactor = ${(numValue * scaleFactor)}" 
                                else "0 + ($numValue) \u00d7 $scaleFactor = ${(numValue * scaleFactor)}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Canvas representing interactive number line
                    val linePosition = numValue * scaleFactor
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val secondaryColor = MaterialTheme.colorScheme.secondary

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        val width = size.width
                        val centerY = size.height / 2f
                        val padding = 30f
                        val lineLength = width - (padding * 2)

                        // Draw baseline
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(padding, centerY),
                            end = Offset(width - padding, centerY),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Draw tick marks from -5 to 5
                        for (i in -5..5) {
                            val tickX = padding + ((i + 5) / 10f) * lineLength
                            drawLine(
                                color = Color.Gray,
                                start = Offset(tickX, centerY - 15f),
                                end = Offset(tickX, centerY + 15f),
                                strokeWidth = 1.5.dp.toPx()
                            )
                        }

                        // Draw pointer representing current value
                        val pointerX = padding + ((linePosition + 5) / 10f).coerceIn(0f, 1f) * lineLength
                        // Draw vector arrow from origin to current position
                        val originX = padding + (5 / 10f) * lineLength
                        drawLine(
                            color = primaryColor.copy(alpha = 0.4f),
                            start = Offset(originX, centerY),
                            end = Offset(pointerX, centerY),
                            strokeWidth = 6.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        drawCircle(
                            color = primaryColor,
                            radius = 8.dp.toPx(),
                            center = Offset(pointerX, centerY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4.dp.toPx(),
                            center = Offset(pointerX, centerY)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("-5", style = MaterialTheme.typography.labelSmall)
                        Text("0", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text("5", style = MaterialTheme.typography.labelSmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Control buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { numValue = (numValue - 1f).coerceIn(-5f, 5f) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = when (lang) {
                                    "English" -> "- Left"
                                    "Chinese" -> "- 向左"
                                    else -> "- Kiri"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        
                        Button(
                            onClick = { scaleFactor = if (scaleFactor == 1f) -1f else 1f },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer, contentColor = MaterialTheme.colorScheme.onTertiaryContainer),
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (scaleFactor == 1f) "× -1" else "× +1",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        Button(
                            onClick = { numValue = (numValue + 1f).coerceIn(-5f, 5f) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = when (lang) {
                                    "English" -> "+ Right"
                                    "Chinese" -> "+ 向右"
                                    else -> "+ Kanan"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Booster Quiz
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Booster Quiz \u26A1 Nominal Nisbah",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Evaluate: (-3) \u00d7 (-4) + (-5) = ?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val options = listOf("a) -17", "b) 7", "c) 17", "d) -7")
                    options.forEachIndexed { i, opt ->
                        val isSelected = selectedOption == i
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = i
                                    showQuizFeedback = true
                                }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = isSelected, onClick = {
                                selectedOption = i
                                showQuizFeedback = true
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opt)
                        }
                    }

                    if (showQuizFeedback) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val isCorrect = selectedOption == 1 // "b) 7" (-3*-4 = 12, 12 - 5 = 7)
                        Surface(
                            color = if (isCorrect) Color(0xFFC2F0E3) else Color(0xFFFFD8E4),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    tint = if (isCorrect) Color(0xFF004D40) else Color(0xFFB71C1C),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isCorrect) 
                                        "Correct! (-3) \u00d7 (-4) = 12. Then 12 + (-5) = 7. Awesome job!"
                                        else "Incorrect. Let's try again! Tip: (-) \u00d7 (-) is positive",
                                    color = if (isCorrect) Color(0xFF00201A) else Color(0xFF31111D),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// MODULE 2: ALGEBRAIC EXPRESSIONS
// ==========================================
@Composable
fun AlgebraicExpressionsSection(lang: String) {
    var stepGroup by remember { mutableStateOf(0) }
    var selectedQuizIndex by remember { mutableStateOf(-1) }
    var showQuiz2Result by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (lang == "English") "Simplifying Terms Together" else "Mempermudahkan Sebutan Algebra",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (lang == "English") 
                            "Like terms are those with the same variable letter (e.g. 3x and 5x). You CAN combine them! Unlike terms (e.g. 3x and 2y) cannot be added or subtracted!"
                            else 
                            "Sebutan serupa mempunyai pemboleh ubah yang sama (contoh: 3x dan 5x). Anda BOLEH menggabungkannya! Sebutan tidak serupa (contoh: 3x dan 2y) tidak boleh ditambah/tolak!",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Live Algebra Combiner Game
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (lang == "English") "Like-Terms Grouping Simulator" else "Simulasi Pengumpulan Sebutan Serupa",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Reduce:  3x + 5y + 4x - 2y",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Step slider / progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Original
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (stepGroup == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (stepGroup == 0) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { stepGroup = 0 }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("1. Unordered", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Sorted
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (stepGroup == 1) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (stepGroup == 1) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { stepGroup = 1 }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("2. Grouped", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Final
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (stepGroup == 2) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (stepGroup == 2) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { stepGroup = 2 }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("3. Unified", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AnimatedVisibility(
                        visible = stepGroup == 0,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TermBlock("3x", ColorPatternsBgLight, ColorPatternsTextLight)
                            TermBlock("+ 5y", ColorCirclesBgLight, ColorCirclesTextLight)
                            TermBlock("+ 4x", ColorPatternsBgLight, ColorPatternsTextLight)
                            TermBlock("- 2y", ColorCirclesBgLight, ColorCirclesTextLight)
                        }
                    }

                    AnimatedVisibility(
                        visible = stepGroup == 1,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Gather identical variable families:", style = MaterialTheme.typography.bodySmall, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.border(1.dp, ColorPatternsTextLight, RoundedCornerShape(8.dp)).padding(6.dp)) {
                                    TermBlock("3x", ColorPatternsBgLight, ColorPatternsTextLight)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    TermBlock("+ 4x", ColorPatternsBgLight, ColorPatternsTextLight)
                                }
                                Row(modifier = Modifier.border(1.dp, ColorCirclesTextLight, RoundedCornerShape(8.dp)).padding(6.dp)) {
                                    TermBlock("+ 5y", ColorCirclesBgLight, ColorCirclesTextLight)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    TermBlock("- 2y", ColorCirclesBgLight, ColorCirclesTextLight)
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = stepGroup == 2,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TermBlock("7x", ColorPatternsBgLight, ColorPatternsTextLight)
                                Text(" (from 3+4)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Text("+", fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TermBlock("3y", ColorCirclesBgLight, ColorCirclesTextLight)
                                Text(" (from 5-2)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // Quick booster quiz 2
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Booster Quiz \u26A1 Ungkapan Algebra",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Identify coefficient of 'ab' in the expression:  5 - 9ab + 2c",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val opts = listOf("a) 9", "b) -9", "c) 5", "d) ab")
                    opts.forEachIndexed { idx, opt ->
                        val isSelected = selectedQuizIndex == idx
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedQuizIndex = idx
                                    showQuiz2Result = true
                                }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = isSelected, onClick = {
                                selectedQuizIndex = idx
                                showQuiz2Result = true
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opt)
                        }
                    }

                    if (showQuiz2Result) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val isCorrect = selectedQuizIndex == 1 // "-9"
                        Surface(
                            color = if (isCorrect) Color(0xFFC2F0E3) else Color(0xFFFFD8E4),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    tint = if (isCorrect) Color(0xFF004D40) else Color(0xFFB71C1C),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isCorrect) 
                                        "Correct! The coefficient is -9, which is the complete multiplier of the variable product 'ab', sign included."
                                        else "Incorrect. Always include the preceding sign. If it is -9ab, the coefficient of ab is -9.",
                                    color = if (isCorrect) Color(0xFF00201A) else Color(0xFF31111D),
                                    fontSize = 13.sp
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
fun TermBlock(term: String, bgColor: Color, txtColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(term, color = txtColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

// ==========================================
// MODULE 3: LINEAR EQUATIONS (PERSAMAAN LINEAR)
// ==========================================
@Composable
fun LinearEquationsSection(lang: String) {
    var isSolved by remember { mutableStateOf(false) }
    var selectedAnsIndex by remember { mutableStateOf(-1) }
    var showQuiz3Result by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (lang == "English") "The Rules of Balancing Scale" else "Kaedah Imbangan Persamaan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (lang == "English") 
                            "An equation x + 3 = 8 represents a matching scale. Whatever subtraction, addition or division you do to one side, you MUST do to the other to keep it level!"
                            else 
                            "Persamaan x + 3 = 8 diibaratkan nerdacing seimbang. Sebarang operasi (+, -, \u00d7, \u00f7) yang dilakukan pada satu bahagian, WAJIB dilakukan juga di bahagian bertentangan!",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Scale balance solver game in Canvas with drag
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Equation Balancer Game",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (!isSolved) "Solve:  x + 3 = 7" else "Status:  Solved (x = 4)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (!isSolved) MaterialTheme.colorScheme.primary else Color(0xFF00796B)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Animate the balance tilt based on solved state
                    val tiltAngle by animateFloatAsState(targetValue = if (isSolved) 0f else -10f)

                    val scaleColor = MaterialTheme.colorScheme.outline
                    val weightColor = MaterialTheme.colorScheme.primary
                    val solvedColor = Color(0xFF00796B)

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        val w = size.width
                        val h = size.height
                        val cx = w / 2f
                        val cy = h - 20f

                        // Draw base stand
                        drawLine(color = scaleColor, start = Offset(cx, cy - 50f), end = Offset(cx, cy), strokeWidth = 8f)
                        drawLine(color = scaleColor, start = Offset(cx - 30f, cy), end = Offset(cx + 30f, cy), strokeWidth = 10f)

                        // Top beam pivot
                        drawCircle(color = scaleColor, radius = 6f, center = Offset(cx, cy - 50f))

                        // Draw tilted beam line based on angle
                        val beamHalfLen = w * 0.25f
                        val rad = Math.toRadians(tiltAngle.toDouble())
                        val offsetX = (beamHalfLen * Math.cos(rad)).toFloat()
                        val offsetY = (beamHalfLen * Math.sin(rad)).toFloat()

                        val leftBeamX = cx - offsetX
                        val leftBeamY = (cy - 50f) - offsetY
                        val rightBeamX = cx + offsetX
                        val rightBeamY = (cy - 50f) + offsetY

                        drawLine(color = scaleColor, start = Offset(leftBeamX, leftBeamY), end = Offset(rightBeamX, rightBeamY), strokeWidth = 6f)

                        // Draw hanging scales
                        // Left scale
                        drawLine(color = Color.LightGray, start = Offset(leftBeamX, leftBeamY), end = Offset(leftBeamX - 15f, leftBeamY + 30f), strokeWidth = 3f)
                        drawLine(color = Color.LightGray, start = Offset(leftBeamX, leftBeamY), end = Offset(leftBeamX + 15f, leftBeamY + 30f), strokeWidth = 3f)
                        drawLine(color = scaleColor, start = Offset(leftBeamX - 25f, leftBeamY + 30f), end = Offset(leftBeamX + 25f, leftBeamY + 30f), strokeWidth = 5f)

                        // Right scale
                        drawLine(color = Color.LightGray, start = Offset(rightBeamX, rightBeamY), end = Offset(rightBeamX - 15f, rightBeamY + 30f), strokeWidth = 3f)
                        drawLine(color = Color.LightGray, start = Offset(rightBeamX, rightBeamY), end = Offset(rightBeamX + 15f, rightBeamY + 30f), strokeWidth = 3f)
                        drawLine(color = scaleColor, start = Offset(rightBeamX - 25f, rightBeamY + 30f), end = Offset(rightBeamX + 25f, rightBeamY + 30f), strokeWidth = 5f)

                        // Draw items on the scales
                        if (!isSolved) {
                            // Left has x and 3 weights (heavy, tilts left)
                            drawCircle(color = weightColor, radius = 9f, center = Offset(leftBeamX - 10f, leftBeamY + 20f))
                            drawCircle(color = Color.Gray, radius = 6f, center = Offset(leftBeamX + 5f, leftBeamY + 23f))
                            drawCircle(color = Color.Gray, radius = 6f, center = Offset(leftBeamX + 12f, leftBeamY + 14f))

                            // Right has 7 weights
                            for (p in 0..5) {
                                drawCircle(color = Color.DarkGray, radius = 5f, center = Offset(rightBeamX - 15f + (p * 6f), rightBeamY + 22f))
                            }
                        } else {
                            // Balanced! Left has x
                            drawCircle(color = solvedColor, radius = 9f, center = Offset(leftBeamX, leftBeamY + 20f))
                            // Right has 4 weights
                            for (p in 0..3) {
                                drawCircle(color = Color.DarkGray, radius = 6f, center = Offset(rightBeamX - 10f + (p * 6f), rightBeamY + 22f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (!isSolved) "Left side is heavier: x + 3 vs 7" else "Scale Balanced perfectly to isolate x!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isSolved = !isSolved },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isSolved) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary)
                    ) {
                        Text(if (isSolved) "Reset Game" else "Apply -3 to both sides!")
                    }
                }
            }
        }

        // Quiz booster 3
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Booster Quiz \u26A1 Linear Equations",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Solve for x:   4x - 5 = 11",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val options = listOf("a) x = 3", "b) x = -4", "c) x = 4", "d) x = 16")
                    options.forEachIndexed { i, opt ->
                        val isSelected = selectedAnsIndex == i
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAnsIndex = i
                                    showQuiz3Result = true
                                }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = isSelected, onClick = {
                                selectedAnsIndex = i
                                showQuiz3Result = true
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opt)
                        }
                    }

                    if (showQuiz3Result) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val isCorrect = selectedAnsIndex == 2 // "c) x = 4"
                        Surface(
                            color = if (isCorrect) Color(0xFFC2F0E3) else Color(0xFFFFD8E4),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    tint = if (isCorrect) Color(0xFF004D40) else Color(0xFFB71C1C),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isCorrect) 
                                        "Correct! Add 5 to both sides -> 4x = 16. Divide both by 4 -> x = 4. Perfection!"
                                        else "Incorrect. Hint: Shift -5 across sign first, which turns to +5 -> 11 + 5 = 16.",
                                    color = if (isCorrect) Color(0xFF00201A) else Color(0xFF31111D),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// MODULE 4: BASIC POLYGONS (POLIGON ASAS)
// ==========================================
@Composable
fun BasicPolygonsSection(lang: String) {
    // Interactive Triangle vertices manipulation
    var dragOffsetY by remember { mutableStateOf(0f) }
    var selectedSideOption by remember { mutableStateOf(-1) }
    var showQuiz4Result by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (lang == "English") "Triangles Interior Angles" else "Sudut Pedalaman Segi Tiga",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (lang == "English") 
                            "In any 3-sided shape, the three interior angles ALWAYS sum to exactly 180\u00b0! If you stretch a corner, the other sides shrink to keep the sum constant."
                            else 
                            "Dalam sebarang bentuk 3-sisi, hasil tambah tiga sudut pedalaman SENTIASA bersamaan tepat 180\u00b0! Jika bucu ditarik, sudut lain mengecil bagi mengekalkan jumlah tersebut.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Live triangle vertex dragging simulator
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Drag Top Vertex to Modulate Angles",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Live calculation values
                    val topAngle = (50f + (dragOffsetY / 5f)).coerceIn(20f, 130f)
                    val baseAngleLeft = ((180f - topAngle) / 2f)
                    val baseAngleRight = 180f - topAngle - baseAngleLeft

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = "${topAngle.toInt()}\u00b0 + ${baseAngleLeft.toInt()}\u00b0 + ${baseAngleRight.toInt()}\u00b0 = 180\u00b0",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val outlineCol = MaterialTheme.colorScheme.primary
                    val fillCol = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    dragOffsetY = (dragOffsetY + dragAmount.y).coerceIn(-150f, 150f)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            val leftX = w * 0.25f
                            val leftY = h * 0.75f

                            val rightX = w * 0.75f
                            val rightY = h * 0.75f

                            // Modulating top vertex coordinate based on drag
                            val topX = w * 0.5f
                            val topY = (h * 0.45f + dragOffsetY).coerceIn(20f, h * 0.7f)

                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(topX, topY)
                                lineTo(leftX, leftY)
                                lineTo(rightX, rightY)
                                close()
                            }

                            drawPath(path = path, color = fillCol)
                            drawPath(path = path, color = outlineCol, style = Stroke(width = 3.dp.toPx()))

                            // Vertex markers
                            drawCircle(color = Color.Red, radius = 6.dp.toPx(), center = Offset(topX, topY))
                            drawCircle(color = outlineCol, radius = 4.dp.toPx(), center = Offset(leftX, leftY))
                            drawCircle(color = outlineCol, radius = 4.dp.toPx(), center = Offset(rightX, rightY))
                        }

                        // Hand drag pointer
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "Swipe",
                            tint = Color.Red,
                            modifier = Modifier
                                .offset(y = (-30.dp + (dragOffsetY / 12f).dp))
                                .size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Drag up/down in the gray area to deform the triangle",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        // Quiz booster 4
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Booster Quiz \u26A1 Basic Polygons",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "An isosceles triangle has one angle equal to 80\u00b0 at the apex. What is the value of each of the base angles?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val options = listOf("a) 80\u00b0", "b) 100\u00b0", "c) 50\u00b0", "d) 60\u00b0")
                    options.forEachIndexed { i, opt ->
                        val isSelected = selectedSideOption == i
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSideOption = i
                                    showQuiz4Result = true
                                }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = isSelected, onClick = {
                                selectedSideOption = i
                                showQuiz4Result = true
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opt)
                        }
                    }

                    if (showQuiz4Result) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val isCorrect = selectedSideOption == 2 // "c) 50\u00b0"
                        Surface(
                            color = if (isCorrect) Color(0xFFC2F0E3) else Color(0xFFFFD8E4),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    tint = if (isCorrect) Color(0xFF004D40) else Color(0xFFB71C1C),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isCorrect) 
                                        "Correct! Sum is 180\u00b0. Apex is 80\u00b0, leaving 100\u00b0. Isosceles base angles are equal: 100\u00b0 \u00f7 2 = 50\u00b0 each!"
                                        else "Incorrect. Triangles sum is 180\u00b0. Apex is 80\u00b0, so base angles sum is 100\u00b0. Divided equally, that is 50\u00b0.",
                                    color = if (isCorrect) Color(0xFF00201A) else Color(0xFF31111D),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
