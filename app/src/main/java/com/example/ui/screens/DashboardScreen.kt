package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwitchAccessShortcut
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.settings.Localization

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RocketLaunch
import com.example.ui.components.SupportDevelopmentModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentLanguage: String,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToAppearance: () -> Unit
) {
    var showSupportModal by remember { mutableStateOf(false) }
    val isDark = MaterialTheme.colorScheme.background.run { (red + green + blue) < 1.5f }

    val (patternsBg, patternsText) = if (isDark) Pair(ColorPatternsBgDark, ColorPatternsTextDark) else Pair(ColorPatternsBgLight, ColorPatternsTextLight)
    val (algebraBg, algebraText) = if (isDark) Pair(ColorFactoriseBgDark, ColorFactoriseTextDark) else Pair(ColorFactoriseBgLight, ColorFactoriseTextLight)
    val (polygonsBg, polygonsText) = if (isDark) Pair(ColorPolygonsBgDark, ColorPolygonsTextDark) else Pair(ColorPolygonsBgLight, ColorPolygonsTextLight)
    val (circlesBg, circlesText) = if (isDark) Pair(ColorCirclesBgDark, ColorCirclesTextDark) else Pair(ColorCirclesBgLight, ColorCirclesTextLight)
    val (shapesBg, shapesText) = if (isDark) Pair(ColorShapesBgDark, ColorShapesTextDark) else Pair(ColorShapesBgLight, ColorShapesTextLight)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = Localization.get("app_title", currentLanguage),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = Localization.get("subtitle", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showSupportModal = true },
                        modifier = Modifier.testTag("nav_support_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Support Development",
                            tint = Color(0xFFE91E63) // Pinkish red for visual pop and warmth
                        )
                    }
                    IconButton(
                        onClick = onNavigateToLanguage,
                        modifier = Modifier.testTag("nav_language_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Change Language"
                        )
                    }
                    IconButton(
                        onClick = onNavigateToAppearance,
                        modifier = Modifier.testTag("nav_appearance_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Appearance Settings"
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
                .padding(16.dp)
        ) {
            // Student Banner matching Section 1 Linear Equation Solver style
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("student_tip_banner")
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Tip",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = Localization.get("student_tip", currentLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = Localization.get("welcome", currentLanguage),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Dynamic grid layout displaying the maths topics of Tingkatan 2
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Topic booster: Form 1 Catch-Up Booster Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToTopic("catchup") }
                            .testTag("booster_catchup_card"),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = "Catch Up Booster",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentLanguage == "English") "Let's Start Over & Catch Up! \uD83D\uDE80" else "Mari Mula Semula Untuk Kejar! \uD83D\uDE80",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (currentLanguage == "English") 
                                        "Struggling? Master core Form 1 Math skills (Rational Numbers, Algebra, Equations, Polygons) to boost your Form 2 journey!"
                                        else "Sukar faham? Kuasai topik teras Matematik T1 untuk melonjakkan prestasi T2 anda!",
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }

                // Topic 1: Patterns and Sequences
                item {
                    TopicCard(
                        title = Localization.get("topic_patterns", currentLanguage),
                        description = Localization.get("pola_desc", currentLanguage),
                        icon = Icons.Default.Timeline,
                        containerColor = patternsBg,
                        contentColor = patternsText,
                        onClick = { onNavigateToTopic("patterns") },
                        testTag = "topic_patterns"
                    )
                }

                // Topic 2: Algebraic Formulae
                item {
                    TopicCard(
                        title = Localization.get("topic_algebra", currentLanguage),
                        description = Localization.get("formulae_desc", currentLanguage),
                        icon = Icons.Default.SwitchAccessShortcut,
                        containerColor = algebraBg,
                        contentColor = algebraText,
                        onClick = { onNavigateToTopic("algebra") },
                        testTag = "topic_algebra"
                    )
                }

                // Topic 3: Polygons
                item {
                    TopicCard(
                        title = Localization.get("topic_polygons", currentLanguage),
                        description = Localization.get("polygons_desc", currentLanguage),
                        icon = Icons.Default.Category,
                        containerColor = polygonsBg,
                        contentColor = polygonsText,
                        onClick = { onNavigateToTopic("polygons") },
                        testTag = "topic_polygons"
                    )
                }

                // Topic 4: Circles
                item {
                    TopicCard(
                        title = Localization.get("topic_circles", currentLanguage),
                        description = Localization.get("circles_desc", currentLanguage),
                        icon = Icons.Default.PieChart,
                        containerColor = circlesBg,
                        contentColor = circlesText,
                        onClick = { onNavigateToTopic("circles") },
                        testTag = "topic_circles"
                    )
                }

                // Topic 5: 3D Shapes
                item {
                    TopicCard(
                        title = Localization.get("topic_shapes", currentLanguage),
                        description = Localization.get("shapes_desc", currentLanguage),
                        icon = Icons.Default.Memory,
                        containerColor = shapesBg,
                        contentColor = shapesText,
                        onClick = { onNavigateToTopic("shapes") },
                        testTag = "topic_shapes"
                    )
                }

                // Support Development Callout Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE0F2F1), // Soft teal mint matching attached layout
                            contentColor = Color(0xFF004D40)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSupportModal = true }
                            .testTag("booster_support_card"),
                        border = BorderStroke(1.dp, Color(0xFF00796B).copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFB2DFDB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Support Heart",
                                    tint = Color(0xFF004D40),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentLanguage == "English") "Support MathLab Development \u2764\uFE0F" else "Sokong Pembangunan MathLab \u2764\uFE0F",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF004D40)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (currentLanguage == "English") 
                                        "If you love this ad-free, interactive tool, click to see how you can support our continued development!"
                                        else "Klik untuk menyokong pembangunan aplikasi pendidikan percuma dan bebas iklan ini!",
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF004D40).copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSupportModal) {
        SupportDevelopmentModal(onDismiss = { showSupportModal = false })
    }
}

@Composable
fun TopicCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor.copy(alpha = 0.85f),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.75f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
