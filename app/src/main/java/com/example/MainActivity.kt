package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.settings.SettingsManager

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Initialize preferences dynamically from storage
    SettingsManager.init(applicationContext)
    com.example.ui.settings.Localization.init(applicationContext)

    enableEdgeToEdge()
    setContent {
      // Collect flow states dynamically
      val currentLanguage by SettingsManager.language.collectAsState()
      val enableDynamicTheme by SettingsManager.enableDynamicTheme.collectAsState()
      val darkTheme by SettingsManager.darkTheme.collectAsState()
      val disableBlurEffects by SettingsManager.disableBlurEffects.collectAsState()
      val useSystemFont by SettingsManager.useSystemFont.collectAsState()

      val themePreset by SettingsManager.themePreset.collectAsState()
      val fontSizeScale by SettingsManager.fontSizeScale.collectAsState()
      val shapeRoundedness by SettingsManager.shapeRoundedness.collectAsState()

      // Navigation stack utilizing mutableStateListOf for high responsiveness
      val navigationStack = remember { mutableStateListOf("dashboard") }
      val currentScreen = navigationStack.lastOrNull() ?: "dashboard"
      var showSupportModal by remember { mutableStateOf(false) }

      MyApplicationTheme(
          darkTheme = darkTheme,
          dynamicColor = enableDynamicTheme,
          themePreset = themePreset,
          fontSizeScale = fontSizeScale,
          shapeRoundedness = shapeRoundedness
      ) {
          // Add system back button handler so the app doesn't close when navigating
          androidx.activity.compose.BackHandler(enabled = navigationStack.size > 1) {
              navigationStack.removeAt(navigationStack.size - 1)
          }

          androidx.compose.foundation.layout.Box(
              modifier = Modifier
                  .fillMaxSize()
                  .background(MaterialTheme.colorScheme.background)
          ) {
              AnimatedContent(
                  targetState = currentScreen,
                  modifier = Modifier.fillMaxSize(),
                  transitionSpec = {
                      fadeIn(animationSpec = spring(stiffness = 250f)) + slideInHorizontally(
                          animationSpec = spring(stiffness = 250f),
                          initialOffsetX = { 350 }
                      ) togetherWith
                      fadeOut(animationSpec = spring(stiffness = 250f)) + slideOutHorizontally(
                          animationSpec = spring(stiffness = 250f),
                          targetOffsetX = { -350 }
                      )
                  },
                  label = "ScreenTransition"
              ) { screen ->
                  when (screen) {
                      "dashboard" -> {
                          DashboardScreen(
                              currentLanguage = currentLanguage,
                              onNavigateToTopic = { topic ->
                                  navigationStack.add(topic)
                              },
                              onNavigateToSettings = {
                                  navigationStack.add("settings")
                              }
                          )
                      }
                      "settings" -> {
                          SettingsScreen(
                              currentLanguage = currentLanguage,
                              onNavigateToLanguage = {
                                  navigationStack.add("language")
                              },
                              onNavigateToAppearance = {
                                  navigationStack.add("appearance")
                              },
                              onNavigateToAbout = {
                                  navigationStack.add("about")
                              },
                              onNavigateToCatchUp = {
                                  navigationStack.add("catchup")
                              },
                              onShowSupport = {
                                  showSupportModal = true
                              },
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "about" -> {
                          AboutScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "language" -> {
                          LanguageScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "appearance" -> {
                          AppearanceScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              },
                              enableDynamic = enableDynamicTheme,
                              darkTheme = darkTheme,
                              disableBlurs = disableBlurEffects,
                              useSystemFont = useSystemFont
                          )
                      }
                      "patterns" -> {
                          PatternsScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "algebra" -> {
                          AlgebraScreens(
                              currentLanguage = currentLanguage,
                              useSystemFont = useSystemFont,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "polygons" -> {
                          PolygonsScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "circles" -> {
                          CirclesScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "shapes" -> {
                          ThreeDShapesScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "catchup" -> {
                          CatchUpScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                      "quiz" -> {
                          QuizScreen(
                              currentLanguage = currentLanguage,
                              onBack = {
                                  if (navigationStack.size > 1) {
                                      navigationStack.removeAt(navigationStack.size - 1)
                                  }
                              }
                          )
                      }
                  }
              }
              if (showSupportModal) {
                  com.example.ui.components.SupportDevelopmentModal(onDismiss = { showSupportModal = false })
              }
          }
      }
    }
  }
}
