package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BlurOff
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.settings.Localization
import com.example.ui.settings.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredLanguages = remember(searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            SettingsManager.LANGUAGES
        } else {
            SettingsManager.LANGUAGES.filter {
                it.originalName.contains(searchQuery, ignoreCase = true) ||
                        it.localizedName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("app_lang", currentLanguage)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("language_back_button")
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
                .padding(16.dp)
        ) {
            // Search Bar matching Screen 1 exactly
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("language_search_input"),
                placeholder = { Text(Localization.get("search_placeholder", currentLanguage)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            // Scrollable List of languages matching layout 1
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredLanguages) { lang ->
                    val isSelected = lang.originalName == currentLanguage
                    val flagSymbol = when (lang.countryCode) {
                        "GB" -> "🇬🇧"
                        "MY" -> "🇲🇾"
                        "FR" -> "🇫🇷"
                        "DE" -> "🇩🇪"
                        "IT" -> "🇮🇹"
                        "JP" -> "🇯🇵"
                        "KR" -> "🇰🇷"
                        "PT" -> "🇵🇹"
                        "RU" -> "🇷🇺"
                        else -> "🏳️"
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                SettingsManager.setLanguage(context, lang.originalName)
                            }
                            .testTag("lang_item_${lang.originalName}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = flagSymbol,
                                    fontSize = 28.sp,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                                Column {
                                    Text(
                                        text = lang.originalName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                    if (lang.originalName != lang.localizedName) {
                                        Text(
                                            text = lang.localizedName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    SettingsManager.setLanguage(context, lang.originalName)
                                },
                                modifier = Modifier.testTag("radio_${lang.originalName}")
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    currentLanguage: String,
    onBack: () -> Unit,
    enableDynamic: Boolean,
    darkTheme: Boolean,
    disableBlurs: Boolean,
    useSystemFont: Boolean
) {
    val context = LocalContext.current
    val themePreset by SettingsManager.themePreset.collectAsState()
    val fontSizeScale by SettingsManager.fontSizeScale.collectAsState()
    val shapeRoundedness by SettingsManager.shapeRoundedness.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.get("appearance", currentLanguage)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("appearance_back_button")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Section Heading matching screenshot 2 green/teal color
                Text(
                    text = Localization.get("theme_sec", currentLanguage),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Item 1: Enable Dynamic Theme
            item {
                SettingsSwitchRow(
                    title = Localization.get("enable_dynamic", currentLanguage),
                    description = null,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Dynamic Theme Preset",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    checked = enableDynamic,
                    onCheckedChange = { SettingsManager.setEnableDynamicTheme(context, it) },
                    testTag = "switch_dynamic_theme"
                )
            }

            // Item 2: Dark Theme (with descriptive "Off"/"On" subtitle matching Layout 2)
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { SettingsManager.setDarkTheme(context, !darkTheme) }
                        .testTag("card_dark_theme")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = "Dark mode toggle",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = Localization.get("dark_theme", currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (darkTheme) Localization.get("on", currentLanguage) else Localization.get("off", currentLanguage),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = darkTheme,
                            onCheckedChange = { SettingsManager.setDarkTheme(context, it) },
                            modifier = Modifier.testTag("switch_dark_theme")
                        )
                    }
                }
            }

            // SECTION 1.5: THEME PRESETS SELECTOR (NEW!)
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (currentLanguage == "English") "Theme Presets" else "Pilihan Tema Warna",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val presets = listOf(
                            Triple("Natural Forest", "Forest", Color(0xFF2E7D32)),
                            Triple("Forest Emerald", "Emerald", Color(0xFF00695C)),
                            Triple("Royal Ocean", "Ocean", Color(0xFF1565C0)),
                            Triple("Sunset Copper", "Copper", Color(0xFFD84315))
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            presets.forEach { (rawName, label, colorIndicator) ->
                                val isSelected = themePreset == rawName
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .clickable { SettingsManager.setThemePreset(context, rawName) }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(colorIndicator)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = label, 
                                            fontSize = 11.sp, 
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 1.6: FONT SIZE SCALE (NEW!)
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (currentLanguage == "English") "Font Size Scale" else "Skala Saiz Tulisan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${fontSizeScale}x",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        val fontOptions = listOf(0.8f, 1.0f, 1.2f)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            fontOptions.forEach { scale ->
                                val isSelected = fontSizeScale == scale
                                val label = when (scale) {
                                    0.8f -> if (currentLanguage == "English") "Small" else "Kecil"
                                    1.0f -> if (currentLanguage == "English") "Normal" else "Asal"
                                    else -> if (currentLanguage == "English") "Large" else "Besar"
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .clickable { SettingsManager.setFontSizeScale(context, scale) }
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 1.7: CORNER ROUNDEDNESS (NEW!)
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (currentLanguage == "English") "Corner Roundedness" else "Kelembutan Sudut Kad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = shapeRoundedness,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val shapeOptions = listOf("Classic Brutalist", "Modern Balanced", "Organic Fluid")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            shapeOptions.forEach { item ->
                                val isSelected = shapeRoundedness == item
                                val label = when (item) {
                                    "Classic Brutalist" -> if (currentLanguage == "English") "Sharp" else "Tajam"
                                    "Modern Balanced" -> if (currentLanguage == "English") "Balanced" else "Imbang"
                                    else -> if (currentLanguage == "English") "Fluid" else "Cecair"
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .clickable { SettingsManager.setShapeRoundedness(context, item) }
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Item 3: Disable Blur Effects matching Screenshot 2 with title & desc
            item {
                SettingsSwitchRow(
                    title = Localization.get("disable_blurs", currentLanguage),
                    description = Localization.get("disable_blurs_desc", currentLanguage),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BlurOff,
                            contentDescription = "No Blur",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    checked = disableBlurs,
                    onCheckedChange = { SettingsManager.setDisableBlurEffects(context, it) },
                    testTag = "switch_disable_blurs"
                )
            }

            // Item 4: Use System Font matching Screenshot 2 with title & desc
            item {
                SettingsSwitchRow(
                    title = Localization.get("use_sys_font", currentLanguage),
                    description = Localization.get("use_sys_font_desc", currentLanguage),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "System font settings",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    checked = useSystemFont,
                    onCheckedChange = { SettingsManager.setUseSystemFont(context, it) },
                    testTag = "switch_use_system_font"
                )
            }
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    description: String?,
    icon: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .testTag("card_$testTag")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.testTag(testTag)
            )
        }
    }
}
