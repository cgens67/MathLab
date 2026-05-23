package com.example.ui.components

import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.AlgebraStep
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    shapes: List<Any> = emptyList()
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0.3f at 0
                1.0f at 200
                0.3f at 400
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse1"
    )
    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0.3f at 100
                1.0f at 300
                0.3f at 500
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse2"
    )
    val pulse3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0.3f at 200
                1.0f at 400
                0.3f at 600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse3"
    )

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val r = (size.minDimension / 2) * pulse1
            drawCircle(color = color, radius = r)
        }
        Canvas(modifier = Modifier.size(24.dp)) {
            val s = 24.dp.toPx() * pulse2
            val offset = (24.dp.toPx() - s) / 2
            drawRoundRect(
                color = color,
                topLeft = Offset(offset, offset),
                size = Size(s, s),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
        }
        Canvas(modifier = Modifier.size(24.dp)) {
            val s = 24.dp.toPx() * pulse3
            val offset = (24.dp.toPx() - s) / 2
            val path = Path().apply {
                moveTo(offset + s / 2, offset)
                lineTo(offset, offset + s)
                lineTo(offset + s, offset + s)
                close()
            }
            drawPath(path = path, color = color)
        }
    }
}

fun translateExplanationToZh(en: String): String {
    if (en.contains("already isolated", ignoreCase = true)) {
        val variable = en.substringBefore(" is already").substringAfterLast(" ").trim()
        return "公式已经完成了对 $variable 的孤立。此项即为公式主项。"
    }
    if (en.contains("We want to isolate", ignoreCase = true)) {
        val variable = en.substringAfter("isolate ").substringBefore(".").trim()
        return "我们想要孤立 $variable。让我们从原始公式开始。"
    }
    if (en.contains("Subtract ", ignoreCase = true) && en.contains("from both sides", ignoreCase = true)) {
        val term = en.substringAfter("Subtract ").substringBefore(" from").trim()
        return "等式两边同时减去 $term，将其余项移过等号。"
    }
    if (en.contains("Multiply both sides by", ignoreCase = true)) {
        val term = en.substringAfter("sides by ").substringBefore(" to").trim()
        return "等式两边同时乘以 $term，消去分母。"
    }
    if (en.contains("Divide both sides by", ignoreCase = true)) {
        val term = en.substringAfter("sides by ").substringBefore(" to").trim()
        return "等式两边同时除以 $term，从而孤立变量。"
    }
    if (en.contains("Rearrange the formula to write the subject", ignoreCase = true)) {
        val variable = en.substringAfter("subject ").substringBefore(" on").trim()
        return "调整公式顺序，将公式主项 $variable 写在等号左侧。求解完成！"
    }

    val dict = mapOf(
        "Add a² to both sides to cancel out −a²." to "等式两边同时加上 a² 以消去 −a²。",
        "Take the square root of both sides to cancel the square exponent." to "等式两边同时开平方根，消去二次方指数。",
        "Multiply both sides by s to cancel the division." to "等式两边同时乘以 s 消除对它的除法限制。",
        "Subtract 2 × a from both sides to cancel it out." to "等式两边同时减去 2 × a 以消去此部分。",
        "Multiply both sides by 2 to cancel out the division." to "等式两边同时乘以 2 以消去除数限制。",
        "Swap sides so the y term is on the left side." to "交换两边，使得 y 对应的项在左侧。",
        "Multiply both sides by w to isolate s fully." to "两边同时乘以 w 以完全孤立 s。 "
    )

    val matched = dict[en]
    if (matched != null) return matched

    var result = en
    if (result.contains("Subtract ", ignoreCase = true)) {
        result = result.replace("Subtract ", "等式两边同时减去 ").replace(" from both sides", "")
    }
    if (result.contains("Add ", ignoreCase = true)) {
        result = result.replace("Add ", "等式两边同时加上 ").replace(" to both sides", "")
    }
    if (result.contains("Multiply both sides by ", ignoreCase = true)) {
        result = result.replace("Multiply both sides by ", "等式两边同时乘以 ")
    }
    if (result.contains("Divide both sides by ", ignoreCase = true)) {
        result = result.replace("Divide both sides by ", "等式两边同时除以 ")
    }
    if (result.contains("Take the square root of both sides", ignoreCase = true)) {
        result = result.replace("Take the square root of both sides", "等式两边同时开平方根")
    }

    return result
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpressiveCarousel(
    steps: List<AlgebraStep>,
    language: String,
    useSystemFont: Boolean,
    modifier: Modifier = Modifier
) {
    if (steps.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { steps.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("algebra_carousel_container")
    ) {
        // Horizontal Pager for M3 Carousel effect
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .height(290.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 16.dp
        ) { page ->
            val step = steps[page]
            val explanation = if (language == "Bahasa Melayu") {
                step.explanationMs
            } else if (language == "Chinese") {
                translateExplanationToZh(step.explanationEn)
            } else {
                step.explanationEn
            }

            // Dynamic scale and opacity animation as you swipe
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = 1f - (kotlin.math.abs(pageOffset) * 0.12f).coerceIn(0f, 1f)
            val alpha = 1f - (kotlin.math.abs(pageOffset) * 0.35f).coerceIn(0f, 1f)

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .testTag("carousel_card_page_$page")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Step Tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = if (language == "Bahasa Melayu") "Langkah ${page + 1}"
                                       else if (language == "Chinese") "步骤 ${page + 1}"
                                       else "Step ${page + 1}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Step Guide",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }

                    // Equation Box with proper big division line replacements
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = step.equation,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontFamily = if (useSystemFont) FontFamily.Default else FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Text explanation
                    Text(
                        text = explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pager indicators and next/prev buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Prev Button
            IconButton(
                onClick = {
                    if (pagerState.currentPage > 0) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                enabled = pagerState.currentPage > 0,
                modifier = Modifier.testTag("carousel_prev_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous step"
                )
            }

            // Dot indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                repeat(steps.size) { index ->
                    val isActive = pagerState.currentPage == index
                    val width by animateFloatAsState(
                        targetValue = if (isActive) 24f else 8f,
                        animationSpec = spring(stiffness = 300f)
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(width.dp)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (isActive) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            // Next Button
            IconButton(
                onClick = {
                    if (pagerState.currentPage < steps.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                enabled = pagerState.currentPage < steps.size - 1,
                modifier = Modifier.testTag("carousel_next_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next step"
                )
            }
        }
    }
}

@Composable
fun PolygonDiagram(sides: Int, modifier: Modifier = Modifier) {
    val primColor = MaterialTheme.colorScheme.primary
    val tertColor = MaterialTheme.colorScheme.tertiary
    val onSurfColor = MaterialTheme.colorScheme.onSurface

    Canvas(
        modifier = modifier
            .size(240.dp)
            .padding(16.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width * 0.4f

        if (sides < 3) return@Canvas

        val angleStep = (2 * Math.PI) / sides
        val vertexPoints = mutableListOf<Offset>()

        // Generate polygon vertices (start at -PI/2 to align upwards)
        for (i in 0 until sides) {
            val angle = -Math.PI / 2 + i * angleStep
            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()
            vertexPoints.add(Offset(x, y))
        }

        // Draw Polygon sides
        val path = Path()
        path.moveTo(vertexPoints[0].x, vertexPoints[0].y)
        for (i in 1 until sides) {
            path.lineTo(vertexPoints[i].x, vertexPoints[i].y)
        }
        path.close()

        // Shape fill & stroke
        drawPath(
            path = path,
            color = primColor.copy(alpha = 0.12f)
        )
        drawPath(
            path = path,
            color = primColor,
            style = Stroke(width = 4.dp.toPx())
        )

        // Draw dotted exterior angle line guide from the first vertex
        if (sides >= 3) {
            val p0 = vertexPoints[0]
            val p1 = vertexPoints[1]
            // Vector from p0 to p1
            val dx = p1.x - p0.x
            val dy = p1.y - p0.y
            // Extend the side slightly to show exterior line
            val extensionFactor = 0.5f
            val extendedPoint = Offset(p1.x + dx * extensionFactor, p1.y + dy * extensionFactor)

            drawLine(
                color = onSurfColor.copy(alpha = 0.5f),
                start = p1,
                end = extendedPoint,
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            // Draw tiny circles at outer nodes
            drawCircle(color = primColor, radius = 5.dp.toPx(), center = p1)
            drawCircle(color = tertColor, radius = 5.dp.toPx(), center = extendedPoint)
        }

        // Draw interior angles indicators at all vertices
        for (pt in vertexPoints) {
            drawCircle(
                color = primColor,
                radius = 4.dp.toPx(),
                center = pt
            )
        }
    }
}

@Composable
fun CircleDiagram(
    subtendedAngle: Float,
    modifier: Modifier = Modifier
) {
    val secColor = MaterialTheme.colorScheme.secondary
    val primColor = MaterialTheme.colorScheme.primary
    val tertColor = MaterialTheme.colorScheme.tertiary

    Canvas(
        modifier = modifier
            .size(240.dp)
            .padding(16.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width * 0.45f

        // Draw the full circle background stroke
        drawCircle(
            color = secColor.copy(alpha = 0.2f),
            radius = radius,
            center = center,
            style = Stroke(width = 4.dp.toPx())
        )

        // Shade the Sector mapping to theta
        val path = Path().apply {
            moveTo(center.x, center.y)
            arcTo(
                rect = Size(radius * 2, radius * 2).let {
                    androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
                },
                startAngleDegrees = 0f,
                sweepAngleDegrees = subtendedAngle,
                forceMoveTo = false
            )
            close()
        }

        drawPath(
            path = path,
            color = primColor.copy(alpha = 0.2f)
        )

        // Draw Arc boundary line thicker
        val arcPath = Path().apply {
            arcTo(
                rect = Size(radius * 2, radius * 2).let {
                    androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
                },
                startAngleDegrees = 0f,
                sweepAngleDegrees = subtendedAngle,
                forceMoveTo = true
            )
        }
        drawPath(
            path = arcPath,
            color = tertColor,
            style = Stroke(width = 6.dp.toPx())
        )

        // Draw radii to the arc endpoints
        val angleRadStart = 0.0
        val angleRadEnd = Math.toRadians(subtendedAngle.toDouble())

        val ptStart = Offset(
            center.x + radius * cos(angleRadStart).toFloat(),
            center.y + radius * sin(angleRadStart).toFloat()
        )
        val ptEnd = Offset(
            center.x + radius * cos(angleRadEnd).toFloat(),
            center.y + radius * sin(angleRadEnd).toFloat()
        )

        // Draw radiating lines
        drawLine(color = primColor, start = center, end = ptStart, strokeWidth = 3.dp.toPx())
        drawLine(color = primColor, start = center, end = ptEnd, strokeWidth = 3.dp.toPx())

        // Draw center dot
        drawCircle(color = primColor, radius = 5.dp.toPx(), center = center)

        // Draw chord represented as direct line between pts
        drawLine(
            color = secColor.copy(alpha = 0.5f),
            start = ptStart,
            end = ptEnd,
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
        )
    }
}

@Composable
fun NetsVisualizer(shape: String, modifier: Modifier = Modifier) {
    val outlineColor = MaterialTheme.colorScheme.onSurface
    val fillCol = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
    val strokeColPrimary = MaterialTheme.colorScheme.primary

    // Breathing motion representing the 3D unfolding/folding transition
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "nets_anim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween<Float>(
                durationMillis = 1800,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            ),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "phase"
    )

    Canvas(
        modifier = modifier
            .size(240.dp)
            .padding(16.dp)
    ) {
        val w = size.width
        val h = size.height

        withTransform({
            scale(phase, phase, Offset(w / 2f, h / 2f))
        }) {
            when (shape) {
                "prism" -> {
                    // A right triangular net: 3 connected rectangles side-by-side + 2 aligned triangular foldouts
                    val path = Path().apply {
                        // Rect 1
                        addRect(androidx.compose.ui.geometry.Rect(w * 0.35f, h * 0.15f, w * 0.65f, h * 0.4f))
                        // Rect 2 (left)
                        addRect(androidx.compose.ui.geometry.Rect(w * 0.05f, h * 0.15f, w * 0.35f, h * 0.4f))
                        // Rect 3 (right)
                        addRect(androidx.compose.ui.geometry.Rect(w * 0.65f, h * 0.15f, w * 0.95f, h * 0.4f))

                        // Left Triangle
                        moveTo(w * 0.35f, h * 0.15f)
                        lineTo(w * 0.5f, h * 0.02f)
                        lineTo(w * 0.65f, h * 0.15f)
                        close()

                        // Right Triangle
                        moveTo(w * 0.35f, h * 0.4f)
                        lineTo(w * 0.5f, h * 0.53f)
                        lineTo(w * 0.65f, h * 0.4f)
                        close()
                    }
                    drawPath(path = path, color = fillCol)
                    drawPath(path = path, color = outlineColor, style = Stroke(width = 2.dp.toPx()))
                }
                "pyramid" -> {
                    // Flat net of a square pyramid: 1 central square + 4 pointing triangles
                    val path = Path().apply {
                        // Central square
                        addRect(androidx.compose.ui.geometry.Rect(w * 0.35f, h * 0.35f, w * 0.65f, h * 0.65f))

                        // Top triangle
                        moveTo(w * 0.35f, h * 0.35f)
                        lineTo(w * 0.5f, h * 0.1f)
                        lineTo(w * 0.65f, h * 0.35f)
                        close()

                        // Bottom triangle
                        moveTo(w * 0.35f, h * 0.65f)
                        lineTo(w * 0.5f, h * 0.9f)
                        lineTo(w * 0.65f, h * 0.65f)
                        close()

                        // Left triangle
                        moveTo(w * 0.35f, h * 0.35f)
                        lineTo(w * 0.1f, h * 0.5f)
                        lineTo(w * 0.35f, h * 0.65f)
                        close()

                        // Right triangle
                        moveTo(w * 0.65f, h * 0.35f)
                        lineTo(w * 0.9f, h * 0.5f)
                        lineTo(w * 0.65f, h * 0.65f)
                        close()
                    }
                    drawPath(path = path, color = fillCol)
                    drawPath(path = path, color = outlineColor, style = Stroke(width = 2.dp.toPx()))
                }
                "cylinder" -> {
                    // Flat net of cylinder: 1 large central rectangle + 2 matching circles above and below
                    val rectLeft = w * 0.25f
                    val rectRight = w * 0.75f
                    val rectTop = h * 0.3f
                    val rectBottom = h * 0.7f

                    // Rectangle
                    drawRect(
                        color = fillCol,
                        topLeft = Offset(rectLeft, rectTop),
                        size = Size(rectRight - rectLeft, rectBottom - rectTop)
                    )
                    drawRect(
                        color = outlineColor,
                        topLeft = Offset(rectLeft, rectTop),
                        size = Size(rectRight - rectLeft, rectBottom - rectTop),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Top circle
                    val rRadius = (rectRight - rectLeft) / 6.28f // 2*pi*r = length
                    drawCircle(
                        color = fillCol,
                        radius = rRadius,
                        center = Offset(w * 0.5f, rectTop - rRadius)
                    )
                    drawCircle(
                        color = outlineColor,
                        radius = rRadius,
                        center = Offset(w * 0.5f, rectTop - rRadius),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Bottom circle
                    drawCircle(
                        color = fillCol,
                        radius = rRadius,
                        center = Offset(w * 0.5f, rectBottom + rRadius)
                    )
                    drawCircle(
                        color = outlineColor,
                        radius = rRadius,
                        center = Offset(w * 0.5f, rectBottom + rRadius),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                "cone" -> {
                    // Flat net of a cone: 1 circular sector + 1 small touching circle base
                    val path = Path().apply {
                        moveTo(w * 0.5f, h * 0.45f)
                        arcTo(
                            rect = Size(w * 0.6f, w * 0.6f).let {
                                androidx.compose.ui.geometry.Rect(w * 0.2f, h * 0.15f, w * 0.8f, h * 0.75f)
                            },
                            startAngleDegrees = -135f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        close()
                    }
                    drawPath(path = path, color = fillCol)
                    drawPath(path = path, color = outlineColor, style = Stroke(width = 2.dp.toPx()))

                    // Touching circle
                    drawCircle(
                        color = fillCol,
                        radius = w * 0.12f,
                        center = Offset(w * 0.5f, h * 0.78f)
                    )
                    drawCircle(
                        color = outlineColor,
                        radius = w * 0.12f,
                        center = Offset(w * 0.5f, h * 0.78f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                "sphere" -> {
                    // A simplified net representation for a sphere (multiple standard spindle shapes aligned like orange slices)
                    // Draw multiple intersecting sine curves to simulate standard map-gores (unfolded sphere net)
                    repeat(4) { idx ->
                        val offsetMultiplier = 0.15f * (idx - 1.5f)
                        val path = Path().apply {
                            val startX = w * (0.5f + offsetMultiplier - 0.05f)
                            val endX = w * (0.5f + offsetMultiplier + 0.05f)
                            moveTo(w * 0.5f, h * 0.1f)
                            quadraticTo(startX, h * 0.5f, w * 0.5f, h * 0.9f)
                            quadraticTo(endX, h * 0.5f, w * 0.5f, h * 0.1f)
                        }
                        drawPath(path = path, color = fillCol)
                        drawPath(path = path, color = outlineColor, style = Stroke(width = 1.5.dp.toPx()))
                    }
                }
            }
        }
    }
}
