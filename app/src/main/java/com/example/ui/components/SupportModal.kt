package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt

@Composable
fun SupportDevelopmentModal(
    onDismiss: () -> Unit
) {
    // Smooth entry transition states
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateIn = true
    }

    // Drag to Dismiss engine
    var dragOffsetY by remember { mutableStateOf(0f) }
    var isDismissing by remember { mutableStateOf(false) }

    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isDismissing) 1200f else dragOffsetY,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.85f),
        label = "DragDismiss",
        finishedListener = { value ->
            if (isDismissing || value >= 1000f) {
                onDismiss()
            }
        }
    )

    // Dynamic MD3 Colors - Now completely respecting the app's theme and light/dark settings
    val containerBg = MaterialTheme.colorScheme.surface
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant
    val lineCol = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    
    val gitHubBtnBg = MaterialTheme.colorScheme.secondaryContainer
    val gitHubBtnText = MaterialTheme.colorScheme.onSecondaryContainer
    
    val payPalBtnBg = MaterialTheme.colorScheme.tertiaryContainer
    val payPalBtnText = MaterialTheme.colorScheme.onTertiaryContainer

    Dialog(
        onDismissRequest = {
            isDismissing = true
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { 
                    isDismissing = true 
                }
                .testTag("support_development_modal_overlay"),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = animateIn && !isDismissing,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(dampingRatio = 0.85f, stiffness = 300f)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, animatedOffsetY.roundToInt()) }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = true
                    ) { /* Prevent click propagation to background */ }
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = containerBg,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("support_development_content")
                        // Drag handler applied to drag handle or entire surface
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (dragOffsetY > 180f) {
                                        isDismissing = true
                                    } else {
                                        dragOffsetY = 0f
                                    }
                                },
                                onDragCancel = {
                                    dragOffsetY = 0f
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    // Only allow downward dragging
                                    dragOffsetY = (dragOffsetY + dragAmount.y).coerceAtLeast(0f)
                                }
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 24.dp)
                            .padding(top = 12.dp, bottom = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Drag indicator replication
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(5.dp)
                                .clip(CircleShape)
                                .background(lineCol)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Heart icon
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Support Heart",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(72.dp)
                                .testTag("support_heart_icon")
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Title
                        Text(
                            text = "Support MathLab T2",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Paragraph text
                        Text(
                            text = "If you like MathLab T2, consider supporting its development. Your contribution helps keep the project alive and ad-free.",
                            fontSize = 15.sp,
                            color = textSecondary,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(36.dp))

                        // Button 1: GitHub Sponsors
                        Button(
                            onClick = { /* Simulated sponsor redirect */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = gitHubBtnBg,
                                contentColor = gitHubBtnText
                            ),
                            shape = RoundedCornerShape(50.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("sponsor_github_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Canvas(modifier = Modifier.size(24.dp)) {
                                    val radius = size.minDimension / 2f
                                    drawCircle(
                                        color = gitHubBtnText,
                                        radius = radius * 0.8f,
                                        center = center
                                    )
                                    val earPath = Path().apply {
                                        moveTo(center.x - radius * 0.5f, center.y - radius * 0.4f)
                                        lineTo(center.x - radius * 0.7f, center.y - radius * 0.9f)
                                        lineTo(center.x - radius * 0.3f, center.y - radius * 0.7f)
                                        close()
                                        moveTo(center.x + radius * 0.5f, center.y - radius * 0.4f)
                                        lineTo(center.x + radius * 0.7f, center.y - radius * 0.9f)
                                        lineTo(center.x + radius * 0.3f, center.y - radius * 0.7f)
                                        close()
                                    }
                                    drawPath(path = earPath, color = gitHubBtnText)
                                    drawCircle(
                                        color = gitHubBtnBg,
                                        radius = radius * 0.3f,
                                        center = Offset(center.x, center.y + radius * 0.1f)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "GitHub Sponsors",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button 2: PayPal
                        Button(
                            onClick = { /* Simulated paypal redirect */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = payPalBtnBg,
                                contentColor = payPalBtnText
                            ),
                            shape = RoundedCornerShape(50.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("sponsor_paypal_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Canvas(modifier = Modifier.size(24.dp)) {
                                    val w = size.width
                                    val h = size.height
                                    val pColor = payPalBtnText
                                    val pathP = Path().apply {
                                        moveTo(w * 0.25f, h * 0.85f)
                                        lineTo(w * 0.4f, h * 0.15f)
                                        quadraticTo(w * 0.75f, h * 0.15f, w * 0.75f, h * 0.45f)
                                        quadraticTo(w * 0.75f, h * 0.65f, w * 0.45f, h * 0.65f)
                                        lineTo(w * 0.32f, h * 0.85f)
                                        close()
                                    }
                                    drawPath(path = pathP, color = pColor)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "PayPal",
                                    fontSize = 16.sp,
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
