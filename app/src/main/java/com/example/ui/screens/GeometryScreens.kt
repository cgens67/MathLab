package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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

// ============================================
// 1. PATTERNS AND SEQUENCES SCREEN
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternsScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    var rawInput by remember { mutableStateOf("2, 5, 8, 11") }
    var result by remember { mutableStateOf<Tingkatan2MathSuite.SequenceResult?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

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
                                result = Tingkatan2MathSuite.solveSequence(parsed)
                                errorText = null
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

            result?.let { res ->
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
                            text = if (currentLanguage == "Bahasa Melayu") res.patternDescriptionMs else res.patternDescriptionEn,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

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

                        res.steps.forEach { step ->
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
                            fontWeight = FontWeight.Bold
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
                        text = "Langkah Pengiraan (Steps):",
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

    val circleResult = remember(radius, angle) { Tingkatan2MathSuite.solveCircle(radius, angle.toDouble()) }
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
                        text = "Langkah Pengiraan (Steps):",
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

    val shapeResult = remember(selectedShape, hVal, rVal, wVal, lVal, sVal) {
        Tingkatan2MathSuite.solve3D(selectedShape, scoreParams)
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
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        shapeOptions.forEach { pair ->
                            val isSelected = pair.first == selectedShape
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedShape = pair.first },
                                label = { Text(pair.second) },
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
                        text = "Dimensi / Dimensions Parameters:",
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
                        text = "Formula & Langkah (Calculation steps):",
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
