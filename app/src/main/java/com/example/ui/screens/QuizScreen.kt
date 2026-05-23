package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.example.ui.theme.appRoundedCornerShape as RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.settings.Localization

data class QuizQuestion(
    val id: Int,
    val questionEn: String,
    val questionMs: String,
    val optionsEn: List<String>,
    val optionsMs: List<String>,
    val correctIndex: Int,
    val explanationEn: List<String>,
    val explanationMs: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    currentLanguage: String,
    onBack: () -> Unit
) {
    val questions = remember {
        listOf(
            QuizQuestion(
                1,
                "Complete the arithmetic number sequence below:\n3, 10, 17, 24, [  ], 38",
                "Lengkapkan jujukan nombor aritmetik di bawah:\n3, 10, 17, 24, [  ], 38",
                listOf("A) 31", "B) 30", "C) 32", "D) 29"),
                listOf("A) 31", "B) 30", "C) 32", "D) 29"),
                0,
                listOf(
                    "Step 1: Find the common difference by subtracting term 1 from term 2.",
                    "Common difference (d) = 10 − 3 = 7.",
                    "Step 2: Check with other consecutive terms: 17 − 10 = 7, 24 − 17 = 7.",
                    "Step 3: Add the common difference to the preceding term of the blank: 24 + 7 = 31.",
                    "Step 4: Verify with the next term: 31 + 7 = 38 (Correct!)."
                ),
                listOf(
                    "Langkah 1: Cari beza sepunya dengan menolak sebutan 1 daripada sebutan 2.",
                    "Beza sepunya (d) = 10 − 3 = 7.",
                    "Langkah 2: Periksa sebutan berturutan yang lain: 17 − 10 = 7, 24 − 17 = 7.",
                    "Langkah 3: Tambah beza sepunya kepada sebutan sebelum ruang kosong: 24 + 7 = 31.",
                    "Langkah 4: Pengesahan dengan sebutan seterusnya: 31 + 7 = 38 (Betul!)."
                )
            ),
            QuizQuestion(
                2,
                "Raiyan went to see a doctor because he had been unwell for more than three days. The doctor prescribed three types of medicines: Fever medication (every 4 hours), Antibiotics (every 6 hours), and Flu medication (every 8 hours). If Raiyan takes all three medicines together at 8:30 a.m. on Monday, when is the next time he will take all of them together again?",
                "Raiyan pergi berjumpa doktor kerana kurang sihat selama lebih tiga hari. Doktor menetapkan tiga jenis ubat: Ubat demam (setiap 4 jam), Antibiotik (setiap 6 jam), dan Ubat selesema (setiap 8 jam). Jika Raiyan mula mengambil ketiga-tiga ubat serentak pada 8:30 pagi Isnin, bilakah masa seterusnya dia akan mengambil semua ubat itu bersama-sama?",
                listOf(
                    "A) 12 hours later (at 8:30 p.m. Monday)",
                    "B) 24 hours later (at 8:30 a.m. Tuesday / next day)",
                    "C) 16 hours later (at 12:30 a.m. Tuesday)",
                    "D) 18 hours later (at 2:30 a.m. Tuesday)"
                ),
                listOf(
                    "A) 12 jam kemudian (pada 8:30 p.m. Isnin)",
                    "B) 24 jam kemudian (pada 8:30 a.m. Selasa / keesokan hari)",
                    "C) 16 jam kemudian (pada 12:30 a.m. Selasa)",
                    "D) 18 jam kemudian (pada 2:30 a.m. Selasa)"
                ),
                1,
                listOf(
                    "This is a real-world application of Least Common Multiple (LCM) / GSTK.",
                    "Step 1: Find the LCM of the medication intervals: 4, 6, and 8 hours.",
                    "• Multiples of 4: 4, 8, 12, 16, 20, 24, 28...",
                    "• Multiples of 6: 6, 12, 18, 24, 30...",
                    "• Multiples of 8: 8, 16, 24, 32...",
                    "Step 2: Identify the smallest common multiple, which is 24.",
                    "Therefore, LCM(4, 6, 8) = 24 hours.",
                    "Step 3: Add 24 hours to the starting time (8:30 a.m. Monday).",
                    "8:30 a.m. Monday + 24 hours = 8:30 a.m. Tuesday (Keesokan hari!)."
                ),
                listOf(
                    "Ini merupakan aplikasi dunia sebenar bagi Gandaan Sepunya Terkecil (GSTK / LCM).",
                    "Langkah 1: Cari GSTK bagi selang masa pengambilan ubat: 4, 6, dan 8 jam.",
                    "• Gandaan 4: 4, 8, 12, 16, 20, 24, 28...",
                    "• Gandaan 6: 6, 12, 18, 24, 30...",
                    "• Gandaan 8: 8, 16, 24, 32...",
                    "Langkah 2: Kenal pasti gandaan sepunya terkecil, iaitu 24.",
                    "Maka, GSTK(4, 6, 8) = 24 jam.",
                    "Langkah 3: Tambah 24 jam kepada masa mula (8:30 a.m. Isnin).",
                    "8:30 a.m. Isnin + 24 jam = 8:30 a.m. Selasa (Keesokan hari!)."
                )
            ),
            QuizQuestion(
                3,
                "What is the part of a circle that is a straight line from the centre of the circle to any point on the circumference?",
                "Apakah bahagian bulatan yang merupakan satu garis lurus dari pusat bulatan ke sebarang titik pada lilitan?",
                listOf("A) Chord (Perentas)", "B) Diameter (Diameter)", "C) Radius (Jejari)", "D) Segment (Tembereng)"),
                listOf("A) Perentas", "B) Diameter", "C) Jejari", "D) Tembereng"),
                2,
                listOf(
                    "A circle consists of key visual elements described in geometry textbooks:",
                    "1. Center: The central point equidistant from outer points.",
                    "2. Radius: A straight line from the center to any point on the boundary.",
                    "3. Diameter: A straight line passing through the center connecting two boundary points.",
                    "4. Chord: A straight line connecting two boundary points directly (not necessarily through the center)."
                ),
                listOf(
                    "Sebuah bulatan terdiri daripada bahagian-bahagian utama seperti di dalam buku teks geometri:",
                    "1. Pusat: Titik tengah yang sama jarak dari semua titik luar bulatan.",
                    "2. Jejari: Garis lurus dari pusat ke sebarang titik pada lilitan.",
                    "3. Diameter: Garis lurus yang melalui pusat bulatan untuk menyambung dua titik pada lilitan.",
                    "4. Perentas: Garis lurus yang menyambungkan sebarang dua titik pada lilitan."
                )
            ),
            QuizQuestion(
                4,
                "Factorise the algebraic expression: 9m² − 100",
                "Faktorkan ungkapan algebra: 9m² − 100",
                listOf(
                    "A) (3m − 10)(3m − 10)",
                    "B) (3m − 10)(3m + 10)",
                    "C) (9m − 10)(m + 10)",
                    "D) (3m − 50)(3m + 50)"
                ),
                listOf(
                    "A) (3m − 10)(3m − 10)",
                    "B) (3m − 10)(3m + 10)",
                    "C) (9m − 10)(m + 10)",
                    "D) (3m − 50)(3m + 50)"
                ),
                1,
                listOf(
                    "This uses the Textbook 'Difference of Squares' expansion model: a² − b² = (a − b)(a + b).",
                    "Step 1: Rewrite both terms as perfect squares:",
                    "• 9m² = (3m)²",
                    "• 100 = 10²",
                    "Step 2: Apply the difference of squares model with a = 3m and b = 10:",
                    "(3m)² − 10² = (3m − 10)(3m + 10)."
                ),
                listOf(
                    "Ini menggunakan kaedah kembangan 'Beza Kuasa Dua Sempurna' buku teks: a² − b² = (a − b)(a + b).",
                    "Langkah 1: Tulis semula kedua-dua sebutan sebagai kuasa dua sempurna:",
                    "• 9m² = (3m)²",
                    "• 100 = 10²",
                    "Langkah 2: Gunakan formula beza kuasa dua dengan a = 3m dan b = 10:",
                    "(3m)² − 10² = (3m − 10)(3m + 10)."
                )
            ),
            QuizQuestion(
                5,
                "An open cylinder has a radius of 7 cm and a vertical height of 10 cm. Find the volume of the cylinder. (Use π = 22/7)",
                "Sebuah silinder terbuka mempunyai jejari 7 cm dan tinggi tegak 10 cm. Cari isipadu silinder tersebut. (Gunakan π = 22/7)",
                listOf("A) 154 cm³", "B) 770 cm³", "C) 1,540 cm³", "D) 3,080 cm³"),
                listOf("A) 154 cm³", "B) 770 cm³", "C) 1,540 cm³", "D) 3,080 cm³"),
                2,
                listOf(
                    "Step 1: Recall the standard volume formula for a cylinder: Volume = Base Area × Height = π × r² × h.",
                    "Step 2: Substitute the dimensions: radius r = 7 cm, height h = 10 cm, and π = 22 ÷ 7.",
                    "Volume = (22 ÷ 7) × (7)² × 10",
                    "Volume = (22 ÷ 7) × 49 × 10",
                    "Volume = 22 × 7 × 10 = 1,540 cm³."
                ),
                listOf(
                    "Langkah 1: Buku teks menyatakan formula isipadu silinder: Isipadu = Luas Tapak × Tinggi = π × j² × t.",
                    "Langkah 2: Gunakan nilai: jejari j = 7 cm, tinggi t = 10 cm, dan π = 22 ÷ 7.",
                    "Isipadu = (22 ÷ 7) × (7)² × 10",
                    "Isipadu = (22 ÷ 7) × 49 × 10",
                    "Isipadu = 22 × 7 × 10 = 1,540 cm³."
                )
            )
        )
    }

    var currentQIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var quizFinished by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentLanguage == "English") "Textbook Interactive Challenge" else "Cabaran Interaktif Buku Teks") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("quiz_back")) {
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
            if (!quizFinished) {
                val q = questions[currentQIndex]

                // Progress Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Question ${currentQIndex + 1} of ${questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "Score: $score",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                LinearProgressIndicator(
                    progress = (currentQIndex + 1).toFloat() / questions.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary
                )

                // Question Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(24.dp)
                    )
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = if (currentLanguage == "Bahasa Melayu") q.questionMs else q.questionEn,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 24.sp,
                            modifier = Modifier.testTag("quiz_question_text")
                        )
                    }
                }

                // Answer Options list
                val options = if (currentLanguage == "Bahasa Melayu") q.optionsMs else q.optionsEn
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    options.forEachIndexed { index, option ->
                        val isCorrect = index == q.correctIndex
                        val isSelected = selectedAnswerIndex == index
                        val borderCol = when {
                            selectedAnswerIndex == null -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            isCorrect -> Color(0xFF4CAF50) // Correct option green outline
                            isSelected -> Color(0xFFF44336) // Selected but wrong option red
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        }
                        val containerCol = when {
                            selectedAnswerIndex == null -> MaterialTheme.colorScheme.surface
                            isCorrect -> Color(0xFFE8F5E9)
                            isSelected -> Color(0xFFFFEBEE)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = containerCol,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = selectedAnswerIndex == null) {
                                    selectedAnswerIndex = index
                                    if (isCorrect) score += 1
                                }
                                .border(2.dp, borderCol, RoundedCornerShape(16.dp))
                                .testTag("quiz_option_$index")
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    color = if (isSelected || (selectedAnswerIndex != null && isCorrect)) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (selectedAnswerIndex != null) {
                                    if (isCorrect) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF4CAF50))
                                    } else if (isSelected) {
                                        Icon(imageVector = Icons.Default.Block, contentDescription = "Wrong", tint = Color(0xFFF44336))
                                    }
                                }
                            }
                        }
                    }
                }

                // Interactive explanation card visible after selection
                AnimatedVisibility(
                    visible = selectedAnswerIndex != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth().testTag("quiz_explanation_panel")
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = "Concept Info", tint = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = if (currentLanguage == "English") "Textbook Math Explanation:" else "Penjelasan Teori Buku Teks:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            val explanations = if (currentLanguage == "Bahasa Melayu") q.explanationMs else q.explanationEn
                            explanations.forEach { exp ->
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text("• ", fontWeight = FontWeight.Black)
                                    Text(exp, style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (currentQIndex + 1 < questions.size) {
                                        currentQIndex += 1
                                        selectedAnswerIndex = null
                                    } else {
                                        quizFinished = true
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.align(Alignment.End).testTag("quiz_next_button")
                            ) {
                                Text(
                                    if (currentQIndex + 1 < questions.size) {
                                        if (currentLanguage == "English") "Next Question" else "Soalan Seterusnya"
                                    } else {
                                        if (currentLanguage == "English") "Finish Quiz" else "Selesai Kuiz"
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                // Quiz completed dashboard / result summary
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).testTag("quiz_finish_card")
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Success",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(80.dp)
                        )

                        Text(
                            text = if (currentLanguage == "English") "Challenge Complete!" else "Cabaran Selesai!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = if (currentLanguage == "English") "You scored $score out of ${questions.size}!"
                                   else "Anda mendapat skor $score daripada ${questions.size}!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (currentLanguage == "English") "Great job reviewing the Form 2 national curriculum textbooks. Keep up the amazing work!"
                                   else "Tahniah kerana berjaya mengulangkaji buku teks kurikulum Matematik Tingkatan 2. Teruskan usaha cemerlang!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    currentQIndex = 0
                                    score = 0
                                    selectedAnswerIndex = null
                                    quizFinished = false
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (currentLanguage == "English") "Restart" else "Mula Semula")
                            }
                            OutlinedButton(
                                onClick = onBack,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (currentLanguage == "English") "Return to Topics" else "Kembali")
                            }
                        }
                    }
                }
            }
        }
    }
}
