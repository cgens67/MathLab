package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SupportDevelopmentModal(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Allows custom full width bottom sheet simulation
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            // Modal Sheet content
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5FAF8) // Muted off-white/mint background matching attached design
                ),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = false) {} // Prevent click propagation
                    .testTag("support_development_modal")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 12.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Drag Handle: "—" matching the top center of attached design
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF505A58).copy(alpha = 0.4f))
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Deep Teal Heart icon centering top
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Support Heart",
                        tint = Color(0xFF00695C), // Greenish teal of OpenTune heart mockup
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
                        color = Color(0xFF191C1B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Paragraph text
                    Text(
                        text = "If you like MathLab T2, consider supporting its development. Your contribution helps keep the project alive and ad-free.",
                        fontSize = 15.sp,
                        color = Color(0xFF3F4946),
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    // Button 1: GitHub Sponsors (Pill, light green background, dark text)
                    Button(
                        onClick = { /* Simulated sponsor redirect */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFCCECE6), // Light green tint from attached design
                            contentColor = Color(0xFF003731) // Dark green text from attached design
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
                            // Custom Octocat Github logo drawn via canvas
                            Canvas(modifier = Modifier.size(24.dp)) {
                                val radius = size.minDimension / 2f
                                // Draw main head circle
                                drawCircle(
                                    color = Color(0xFF003731),
                                    radius = radius * 0.8f,
                                    center = center
                                )
                                // Draw ears
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
                                drawPath(path = earPath, color = Color(0xFF003731))
                                // White face details
                                drawCircle(
                                    color = Color(0xFFCCECE6),
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

                    // Button 2: PayPal (Pill, sharp deep blue background, white text)
                    Button(
                        onClick = { /* Simulated paypal redirect */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00457C), // Sharp blue from PayPal signature mock
                            contentColor = Color.White
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
                            // Custom PayPal stylized 'P' drawn via Canvas
                            Canvas(modifier = Modifier.size(24.dp)) {
                                val w = size.width
                                val h = size.height
                                val pColor = Color.White
                                val pathP = Path().apply {
                                    // Draw an italicized 'P' icon
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
