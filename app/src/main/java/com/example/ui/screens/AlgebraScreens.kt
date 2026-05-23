package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.TableChart
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
import com.example.math.AlgebraEngine
import com.example.math.Tingkatan2MathSuite
import com.example.math.FormulaTemplate
import com.example.ui.components.ExpressiveCarousel
import com.example.ui.settings.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgebraScreens(
    currentLanguage: String,
    useSystemFont: Boolean,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        Localization.get("subject_title", currentLanguage),
        Localization.get("expansion", currentLanguage),
        Localization.get("factorisation", currentLanguage),
        Localization.get("hcf_lcm", currentLanguage)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("topic_algebra", currentLanguage)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("algebra_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = Localization.get("back", currentLanguage)
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
            // M3 Scrollable Tab Row to switch components effortlessly
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth().testTag("algebra_tab_row")
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
                        modifier = Modifier.testTag("algebra_tab_$index")
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (selectedTab) {
                    0 -> SubjectChangePanel(currentLanguage, useSystemFont)
                    1 -> ExpansionPanel(currentLanguage)
                    2 -> FactorisationPanel(currentLanguage)
                    3 -> HcfLcmPanel(currentLanguage)
                }
            }
        }
    }
}

// --- PANEL 1: SUBJECT CHANGE (Rumus Algebra) ---
@Composable
fun SubjectChangePanel(currentLanguage: String, useSystemFont: Boolean) {
    val scrollState = rememberScrollState()

    // Loaded formulas from AlgebraEngine
    val formulas = AlgebraEngine.templates
    var selectedFormula by remember { mutableStateOf(formulas[0]) }
    var selectedTarget by remember { mutableStateOf(selectedFormula.variables[0]) }

    // Re-verify Target when formula template shifts
    LaunchedEffect(selectedFormula) {
        if (selectedTarget !in selectedFormula.variables) {
            selectedTarget = selectedFormula.variables[0]
        }
    }

    val steps = remember(selectedFormula, selectedTarget) {
        selectedFormula.generateSteps(selectedTarget)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropdown Selection for Formula
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pilih Rumus / Choose Formula Template:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                formulas.forEach { item ->
                    val isSelected = item.id == selectedFormula.id
                    val displayName = if (currentLanguage == "Bahasa Melayu") item.formulaNameMs else item.formulaNameEn

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .clickable {
                                selectedFormula = item
                                selectedTarget = item.variables[0]
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                selectedFormula = item
                                selectedTarget = item.variables[0]
                            },
                            modifier = Modifier.testTag("radio_formula_${item.id}")
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = item.originalFormula,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Variable Subject selector
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Localization.get("select_subject", currentLanguage),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedFormula.variables.forEach { v ->
                        val isSubject = v == selectedTarget
                        FilterChip(
                            selected = isSubject,
                            onClick = { selectedTarget = v },
                            label = { Text("Isolate $v") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chip_target_$v")
                        )
                    }
                }
            }
        }

        // Carousel Swiper helper tip
        Text(
            text = Localization.get("swipe_to_see", currentLanguage),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Custom M3 Expressive swipeable carousel steps
        ExpressiveCarousel(
            steps = steps,
            language = currentLanguage,
            useSystemFont = useSystemFont,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


// --- PANEL 2: EXPANSION (Kembangan) ---
@Composable
fun ExpansionPanel(currentLanguage: String) {
    var aInput by remember { mutableStateOf("1") }
    var bInput by remember { mutableStateOf("3") }
    var cInput by remember { mutableStateOf("1") }
    var dInput by remember { mutableStateOf("2") }

    var expandedResult by remember { mutableStateOf<Tingkatan2MathSuite.ExpansionResult?>(null) }
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
                    text = "${Localization.get("expand_title", currentLanguage)}: (ax + b)(cx + d)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Input rows
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = aInput,
                        onValueChange = { aInput = it },
                        label = { Text("a") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("exp_input_a")
                    )
                    OutlinedTextField(
                        value = bInput,
                        onValueChange = { bInput = it },
                        label = { Text("b") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("exp_input_b")
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = cInput,
                        onValueChange = { cInput = it },
                        label = { Text("c") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("exp_input_c")
                    )
                    OutlinedTextField(
                        value = dInput,
                        onValueChange = { dInput = it },
                        label = { Text("d") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("exp_input_d")
                    )
                }

                Button(
                    onClick = {
                        val aVal = aInput.toIntOrNull() ?: 1
                        val bVal = bInput.toIntOrNull() ?: 0
                        val cVal = cInput.toIntOrNull() ?: 1
                        val dVal = dInput.toIntOrNull() ?: 0
                        expandedResult = Tingkatan2MathSuite.expandProduct(aVal, bVal, cVal, dVal)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_expand_solve")
                ) {
                    Icon(Icons.Default.Calculate, contentDescription = "Solve")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Localization.get("solve", currentLanguage))
                }
            }
        }

        // Show Result
        AnimatedVisibility(
            visible = expandedResult != null,
            enter = fadeIn() + expandVertically() + slideInVertically(initialOffsetY = { 40 }),
            exit = fadeOut() + shrinkVertically()
        ) {
            val result = expandedResult ?: return@AnimatedVisibility
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${Localization.get("result", currentLanguage)}:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = result.expandedString,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                    Text(
                        text = "${Localization.get("explanation", currentLanguage)}:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
                        fontWeight = FontWeight.Bold
                    )

                    result.steps.forEach { step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(step, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}


// --- HELPER FOR NATURAL EXPRESSION PARSING ---
fun parseNaturalFactorisation(input: String): Triple<Int, Int, Int>? {
    var cleaned = input.lowercase()
        .replace("factorise", "")
        .replace("factorize", "")
        .replace(" ", "")
        .trim()
    
    if (cleaned.isEmpty()) return null
    cleaned = cleaned.replace("²", "^2")
    val variableChar = cleaned.firstOrNull { it in 'a'..'z' } ?: 'x'
    
    val terms = mutableListOf<String>()
    var currentTerm = ""
    for (char in cleaned) {
        if (char == '+' || char == '-') {
            if (currentTerm.isNotEmpty()) {
                terms.add(currentTerm)
            }
            currentTerm = char.toString()
        } else {
            currentTerm += char
        }
    }
    if (currentTerm.isNotEmpty()) {
        terms.add(currentTerm)
    }
    
    var a = 0
    var b = 0
    var c = 0
    
    for (term in terms) {
        val t = term.trim()
        if (t.isEmpty()) continue
        if (t.contains("$variableChar^2")) {
            val rawCoeff = t.replace("$variableChar^2", "")
            a = when {
                rawCoeff.isEmpty() -> 1
                rawCoeff == "+" -> 1
                rawCoeff == "-" -> -1
                else -> rawCoeff.toIntOrNull() ?: 1
            }
        } else if (t.contains(variableChar.toString())) {
            val rawCoeff = t.replace(variableChar.toString(), "")
            b = when {
                rawCoeff.isEmpty() -> 1
                rawCoeff == "+" -> 1
                rawCoeff == "-" -> -1
                else -> rawCoeff.toIntOrNull() ?: 0
            }
        } else {
            c = t.toIntOrNull() ?: 0
        }
    }
    return Triple(a, b, c)
}

// --- PANEL 3: FACTORISATION (Pemfaktoran Kaedah Pendaraban Silang) ---
@Composable
fun FactorisationPanel(currentLanguage: String) {
    var naturalInput by remember { mutableStateOf("") }
    var aInput by remember { mutableStateOf("1") }
    var bInput by remember { mutableStateOf("5") }
    var cInput by remember { mutableStateOf("6") }

    var factorResult by remember { mutableStateOf<Tingkatan2MathSuite.FactorisationResult?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Natural Input Card
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
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (currentLanguage == "English") "Natural Expression Solver" 
                           else if (currentLanguage == "Chinese") "自然语言公式求解器"
                           else "Penyelesai Ekspresi Semulajadi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Text(
                    text = if (currentLanguage == "English") "Type directly (e.g., \"factorise 9m² - 100\" or \"x² + 5x + 6\"):" 
                           else if (currentLanguage == "Chinese") "直接输入代数式（例如 \"factorise 9m² - 100\" 或 \"x² + 5x + 6\"）："
                           else "Taip formula terus (cth., \"factorise 9m² - 100\" atau \"x² + 5x + 6\"):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = naturalInput,
                    onValueChange = { newValue ->
                        naturalInput = newValue
                        val parsed = parseNaturalFactorisation(newValue)
                        if (parsed != null) {
                            aInput = parsed.first.toString()
                            bInput = parsed.second.toString()
                            cInput = parsed.third.toString()
                        }
                    },
                    placeholder = { Text("factorise 9m² - 100") },
                    modifier = Modifier.fillMaxWidth().testTag("fact_natural_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                
                if (naturalInput.isNotEmpty()) {
                    val parsed = parseNaturalFactorisation(naturalInput)
                    if (parsed != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (currentLanguage == "English") "Parsed Coefficients:" 
                                           else if (currentLanguage == "Chinese") "解析系数："
                                           else "Pekali Diperoleh:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text("a = ${parsed.first}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                Text("b = ${parsed.second}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                Text("c = ${parsed.third}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${Localization.get("factor_title", currentLanguage)} (ax² + bx + c)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = aInput,
                        onValueChange = { aInput = it },
                        label = { Text("a") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("fact_input_a")
                    )
                    OutlinedTextField(
                        value = bInput,
                        onValueChange = { bInput = it },
                        label = { Text("b") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("fact_input_b")
                    )
                    OutlinedTextField(
                        value = cInput,
                        onValueChange = { cInput = it },
                        label = { Text("c") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("fact_input_c")
                    )
                }

                Button(
                    onClick = {
                        val aVal = aInput.toIntOrNull() ?: 1
                        val bVal = bInput.toIntOrNull() ?: 0
                        val cVal = cInput.toIntOrNull() ?: 0
                        factorResult = Tingkatan2MathSuite.factoriseQuadratic(aVal, bVal, cVal)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_fact_solve")
                ) {
                    Icon(Icons.Default.Calculate, contentDescription = "Factorise")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Localization.get("solve", currentLanguage))
                }
            }
        }

        // Display results + visually drawn cross multiplication table matrix
        AnimatedVisibility(
            visible = factorResult != null,
            enter = fadeIn() + expandVertically() + slideInVertically(initialOffsetY = { 40 }),
            exit = fadeOut() + shrinkVertically()
        ) {
            val result = factorResult ?: return@AnimatedVisibility
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${Localization.get("result", currentLanguage)}:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = result.formattedResult,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (result.success) {
                        Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                        // Visual Cross Multiplication grid layout (Kaedah Pendaraban Silang)
                        Text(
                            text = "Kaedah Pendaraban Silang (Cross-Multiplication Table):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Column Headers
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Factors a", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text("Factors c", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text("Cross Sum (b)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                }

                                Divider()

                                // Line 1
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(result.col1.first, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(result.col2.first, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(result.col3.first, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                }

                                // Cross Visual Lines
                                Text("╳", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)

                                // Line 2
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(result.col1.second, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(result.col2.second, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(result.col3.second, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                }

                                Divider()

                                // Sum Row (equals product verification)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${aInput}x²", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(cInput, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                    Text(result.col3.third, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }

                    // Explanations steps below
                    Text(
                        text = "${Localization.get("explanation", currentLanguage)}:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                        fontWeight = FontWeight.Bold
                    )

                    result.steps.forEach { step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(step, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}


// --- PANEL 4: HCF & LCM (FSTB & GSTK Kaedah Pembahagian Berulang) ---
@Composable
fun HcfLcmPanel(currentLanguage: String) {
    var rawInput by remember { mutableStateOf("12, 18, 30") }
    var calcResult by remember { mutableStateOf<Tingkatan2MathSuite.HcfLcmResult?>(null) }
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
                    text = Localization.get("hcf_title", currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = Localization.get("num_list_prompt", currentLanguage),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = rawInput,
                    onValueChange = { rawInput = it },
                    placeholder = { Text("e.g. 12, 18, 30") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("hcf_raw_input")
                )

                Button(
                    onClick = {
                        val parsed = rawInput.split(",")
                            .mapNotNull { it.trim().toIntOrNull() }
                        if (parsed.isNotEmpty()) {
                            calcResult = Tingkatan2MathSuite.solveHcfLcm(parsed)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_hcf_solve")
                ) {
                    Icon(Icons.Default.Calculate, contentDescription = "Solve")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Localization.get("solve", currentLanguage))
                }
            }
        }

        calcResult?.let { result ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${Localization.get("result", currentLanguage)}:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("HCF (FSTB)", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text("${result.hcf}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                            }
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("LCM (GSTK)", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text("${result.lcm}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    // Repeated Division table representations
                    if (result.divisionRows.isNotEmpty()) {
                        Divider()

                        Text(
                            text = "${Localization.get("hcf_desc", currentLanguage)} (Repeated Division Chart):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                result.divisionRows.forEach { row ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        // Divider block
                                        Text(
                                            text = "${row.divisor} ┤",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 18.sp,
                                            color = if (row.isCommonDivisor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.width(50.dp),
                                            textAlign = TextAlign.End
                                        )

                                        Spacer(modifier = Modifier.width(16.dp))

                                        // Numbers row being divided
                                        Text(
                                            text = row.quotients.joinToString(" ,  "),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                // Bottom terminal row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "   └──",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.width(50.dp),
                                        textAlign = TextAlign.End
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = List(result.divisionRows.first().quotients.size) { "1" }.joinToString(" ,  "),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    // Steps explanations list
                    Text(
                        text = "${Localization.get("explanation", currentLanguage)}:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                        fontWeight = FontWeight.Bold
                    )

                    result.steps.forEach { step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(step, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
