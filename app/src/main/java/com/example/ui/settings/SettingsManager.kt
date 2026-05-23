package com.example.ui.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SettingsManager {
    private const val PREFS_NAME = "math_expressive_prefs"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_DYNAMIC_THEME = "dynamic_theme"
    private const val KEY_DARK_THEME = "dark_theme"
    private const val KEY_DISABLE_BLURS = "disable_blurs"
    private const val KEY_SYSTEM_FONT = "system_font"
    private const val KEY_THEME_PRESET = "theme_preset"
    private const val KEY_FONT_SCALE = "font_scale"
    private const val KEY_SHAPE_ROUNDED = "shape_rounded"

    // Languages supported as requested in language list
    val LANGUAGES = listOf(
        LanguageItem("English", "English", "GB"),
        LanguageItem("Bahasa Melayu", "Malay", "MY"),
        LanguageItem("French", "Français", "FR"),
        LanguageItem("German", "Deutsch", "DE"),
        LanguageItem("Italian", "Italiano", "IT"),
        LanguageItem("Japanese", "日本語", "JP"),
        LanguageItem("Korean", "한국어", "KR"),
        LanguageItem("Portuguese", "Português", "PT"),
        LanguageItem("Russian", "Русский", "RU")
    )

    data class LanguageItem(
        val originalName: String,
        val localizedName: String,
        val countryCode: String
    )

    // Flow states for Compose observation
    private val _language = MutableStateFlow("English")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _enableDynamicTheme = MutableStateFlow(true)
    val enableDynamicTheme: StateFlow<Boolean> = _enableDynamicTheme.asStateFlow()

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    private val _disableBlurEffects = MutableStateFlow(false)
    val disableBlurEffects: StateFlow<Boolean> = _disableBlurEffects.asStateFlow()

    private val _useSystemFont = MutableStateFlow(true)
    val useSystemFont: StateFlow<Boolean> = _useSystemFont.asStateFlow()

    private val _themePreset = MutableStateFlow("Lavender")
    val themePreset: StateFlow<String> = _themePreset.asStateFlow()

    private val _fontSizeScale = MutableStateFlow(1.0f)
    val fontSizeScale: StateFlow<Float> = _fontSizeScale.asStateFlow()

    private val _shapeRoundedness = MutableStateFlow("Modern Balanced")
    val shapeRoundedness: StateFlow<String> = _shapeRoundedness.asStateFlow()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _language.value = prefs.getString(KEY_LANGUAGE, "English") ?: "English"
        _enableDynamicTheme.value = prefs.getBoolean(KEY_DYNAMIC_THEME, true)
        _darkTheme.value = prefs.getBoolean(KEY_DARK_THEME, false)
        _disableBlurEffects.value = prefs.getBoolean(KEY_DISABLE_BLURS, false)
        _useSystemFont.value = prefs.getBoolean(KEY_SYSTEM_FONT, true)
        _themePreset.value = prefs.getString(KEY_THEME_PRESET, "Lavender") ?: "Lavender"
        _fontSizeScale.value = prefs.getFloat(KEY_FONT_SCALE, 1.0f)
        _shapeRoundedness.value = prefs.getString(KEY_SHAPE_ROUNDED, "Modern Balanced") ?: "Modern Balanced"
    }

    fun setLanguage(context: Context, lang: String) {
        _language.value = lang
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, lang)
            .apply()
    }

    fun setEnableDynamicTheme(context: Context, enabled: Boolean) {
        _enableDynamicTheme.value = enabled
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DYNAMIC_THEME, enabled)
            .apply()
    }

    fun setDarkTheme(context: Context, enabled: Boolean) {
        _darkTheme.value = enabled
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_THEME, enabled)
            .apply()
    }

    fun setDisableBlurEffects(context: Context, disabled: Boolean) {
        _disableBlurEffects.value = disabled
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DISABLE_BLURS, disabled)
            .apply()
    }

    fun setUseSystemFont(context: Context, enabled: Boolean) {
        _useSystemFont.value = enabled
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_SYSTEM_FONT, enabled)
            .apply()
    }

    fun setThemePreset(context: Context, preset: String) {
        _themePreset.value = preset
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME_PRESET, preset)
            .apply()
    }

    fun setFontSizeScale(context: Context, scale: Float) {
        _fontSizeScale.value = scale
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putFloat(KEY_FONT_SCALE, scale)
            .apply()
    }

    fun setShapeRoundedness(context: Context, style: String) {
        _shapeRoundedness.value = style
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SHAPE_ROUNDED, style)
            .apply()
    }
}
