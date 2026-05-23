package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwitchAccessShortcut
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.settings.Localization
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currentLanguage: String,
    onNavigateToTopic: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
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
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("nav_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
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
                .padding(horizontal = 16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                // Header Banner: Slim and elegant text banner (spans full 2 columns)
                item(span = { GridItemSpan(2) }) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .testTag("student_tip_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Spark",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = Localization.get("student_tip", currentLanguage),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 15.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Title Welcome (spans full 2 columns)
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = Localization.get("welcome", currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }

                // Grid 1/5: Patterns
                item {
                    CompactTopicCard(
                        title = Localization.get("topic_patterns", currentLanguage),
                        description = Localization.get("pola_desc", currentLanguage),
                        icon = Icons.Default.Timeline,
                        containerColor = patternsBg,
                        contentColor = patternsText,
                        onClick = { onNavigateToTopic("patterns") },
                        testTag = "topic_patterns"
                    )
                }

                // Grid 2/5: Algebraic Formulae
                item {
                    CompactTopicCard(
                        title = Localization.get("topic_algebra", currentLanguage),
                        description = Localization.get("formulae_desc", currentLanguage),
                        icon = Icons.Default.SwitchAccessShortcut,
                        containerColor = algebraBg,
                        contentColor = algebraText,
                        onClick = { onNavigateToTopic("algebra") },
                        testTag = "topic_algebra"
                    )
                }

                // Grid 3/5: Polygons
                item {
                    CompactTopicCard(
                        title = Localization.get("topic_polygons", currentLanguage),
                        description = Localization.get("polygons_desc", currentLanguage),
                        icon = Icons.Default.Category,
                        containerColor = polygonsBg,
                        contentColor = polygonsText,
                        onClick = { onNavigateToTopic("polygons") },
                        testTag = "topic_polygons"
                    )
                }

                // Grid 4/5: Circles
                item {
                    CompactTopicCard(
                        title = Localization.get("topic_circles", currentLanguage),
                        description = Localization.get("circles_desc", currentLanguage),
                        icon = Icons.Default.PieChart,
                        containerColor = circlesBg,
                        contentColor = circlesText,
                        onClick = { onNavigateToTopic("circles") },
                        testTag = "topic_circles"
                    )
                }

                // Grid 5/5: 3D Shapes (spans full 2 columns for perfect symmetry)
                item(span = { GridItemSpan(2) }) {
                    FullWidthTopicCard(
                        title = Localization.get("topic_shapes", currentLanguage),
                        description = Localization.get("shapes_desc", currentLanguage),
                        icon = Icons.Default.Calculate,
                        containerColor = shapesBg,
                        contentColor = shapesText,
                        onClick = { onNavigateToTopic("shapes") },
                        testTag = "topic_shapes"
                    )
                }
            }
        }
    }
}

@Composable
fun CompactTopicCard(
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
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor.copy(alpha = 0.85f),
                modifier = Modifier.size(28.dp)
            )
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    maxLines = 1,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = contentColor.copy(alpha = 0.75f),
                    lineHeight = 13.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun FullWidthTopicCard(
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
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(contentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = contentColor.copy(alpha = 0.75f),
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}
