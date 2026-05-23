package com.example.math

data class AlgebraStep(
    val equation: String,
    val explanationEn: String,
    val explanationMs: String,
    val highlightedPart: String = ""
)

data class FormulaTemplate(
    val id: String,
    val formulaNameEn: String,
    val formulaNameMs: String,
    val originalFormula: String,
    val variables: List<String>,
    val generateSteps: (target: String) -> List<AlgebraStep>
)

object AlgebraEngine {
    val templates = listOf(
        // x = 3y/2 + z (Expressly requested!)
        FormulaTemplate(
            id = "expressive_requested",
            formulaNameEn = "Change of Subject: Tingkatan 2 Standard",
            formulaNameMs = "Tukar Perkara Rumus: Tingkatan 2 Standard",
            originalFormula = "x = (3 × y) ÷ 2 + z",
            variables = listOf("y", "z", "x"),
            generateSteps = { target ->
                when (target) {
                    "x" -> listOf(
                        AlgebraStep(
                            equation = "x = (3 × y) ÷ 2 + z",
                            explanationEn = "The formula is already isolated for x. It is the subject.",
                            explanationMs = "Rumus ini sudah pun berasingan untuk x. Ia adalah perkara rumus.",
                            highlightedPart = "x"
                        )
                    )
                    "y" -> listOf(
                        AlgebraStep(
                            equation = "x = (3 × y) ÷ 2 + z",
                            explanationEn = "We want to isolate y. Let's start with our original formula.",
                            explanationMs = "Kita mahu mengasingkan y. Mari kita mulakan dengan rumus asal.",
                            highlightedPart = "y"
                        ),
                        AlgebraStep(
                            equation = "x − z = (3 × y) ÷ 2",
                            explanationEn = "Subtract z from both sides to move it away from the y term.",
                            explanationMs = "Tolak z dari kedua-dua belah untuk menggerakkannya menjauhi sebutan y.",
                            highlightedPart = "− z"
                        ),
                        AlgebraStep(
                            equation = "2 × (x − z) = 3 × y",
                            explanationEn = "Multiply both sides by 2 to cancel the division by 2.",
                            explanationMs = "Darab kedua-dua belah dengan 2 untuk membatalkan pembahagian dengan 2.",
                            highlightedPart = "2 ×"
                        ),
                        AlgebraStep(
                            equation = "[2 × (x − z)] ÷ 3 = y",
                            explanationEn = "Divide both sides by 3 to isolate y fully.",
                            explanationMs = "Bahagi kedua-dua belah dengan 3 untuk mengasingkan y sepenuhnya.",
                            highlightedPart = "÷ 3"
                        ),
                        AlgebraStep(
                            equation = "y = [2 × (x − z)] ÷ 3",
                            explanationEn = "Rearrange the formula to write the subject y on the left side. Solved!",
                            explanationMs = "Susun semula rumus untuk menulis perkara rumus y di sebelah kiri. Selesai!",
                            highlightedPart = "y ="
                        )
                    )
                    "z" -> listOf(
                        AlgebraStep(
                            equation = "x = (3 × y) ÷ 2 + z",
                            explanationEn = "We want to isolate z. Let's look at the original formula.",
                            explanationMs = "Kita mahu mengasingkan z. Mari lihat rumus asal.",
                            highlightedPart = "z"
                        ),
                        AlgebraStep(
                            equation = "x − [(3 × y) ÷ 2] = z",
                            explanationEn = "Subtract the entire term (3 × y) ÷ 2 from both sides to isolate z.",
                            explanationMs = "Tolak keseluruhan sebutan (3 × y) ÷ 2 dari kedua-dua belah untuk mengasingkan z.",
                            highlightedPart = "− [(3 × y) ÷ 2]"
                        ),
                        AlgebraStep(
                            equation = "z = x − [(3 × y) ÷ 2]",
                            explanationEn = "Rearrange the formula to write the subject z on the left side. Solved!",
                            explanationMs = "Susun semula rumus untuk menulis perkara rumus z di sebelah kiri. Selesai!",
                            highlightedPart = "z ="
                        )
                    )
                    else -> emptyList()
                }
            }
        ),

        // v = u + at (Velocity Formula)
        FormulaTemplate(
            id = "physics_velocity",
            formulaNameEn = "Linear Motion: v = u + at",
            formulaNameMs = "Gerakan Linear: v = u + at",
            originalFormula = "v = u + a × t",
            variables = listOf("t", "a", "u", "v"),
            generateSteps = { target ->
                when (target) {
                    "v" -> listOf(
                        AlgebraStep(
                            equation = "v = u + a × t",
                            explanationEn = "The formula is already isolated for v.",
                            explanationMs = "Rumus sememangnya sudah pun berasingan untuk v.",
                            highlightedPart = "v"
                        )
                    )
                    "u" -> listOf(
                        AlgebraStep(
                            equation = "v = u + a × t",
                            explanationEn = "To isolate u, search for extra terms added to it.",
                            explanationMs = "Untuk mengasingkan u, cari sebutan tambahan yang ditambah kepadanya.",
                            highlightedPart = "u"
                        ),
                        AlgebraStep(
                            equation = "v − a × t = u",
                            explanationEn = "Subtract the term (a × t) from both sides.",
                            explanationMs = "Tolak sebutan (a × t) dari kedua-dua belah.",
                            highlightedPart = "− a × t"
                        ),
                        AlgebraStep(
                            equation = "u = v − a × t",
                            explanationEn = "Rearrange the equation to put the subject u on the left.",
                            explanationMs = "Susun semula persamaan untuk meletakkan perkara rumus u di kiri.",
                            highlightedPart = "u ="
                        )
                    )
                    "a" -> listOf(
                        AlgebraStep(
                            equation = "v = u + a × t",
                            explanationEn = "To isolate a, first separate the term containing a.",
                            explanationMs = "Untuk mengasingkan a, mula-mula asingkan sebutan yang mengandungi a.",
                            highlightedPart = "a"
                        ),
                        AlgebraStep(
                            equation = "v − u = a × t",
                            explanationEn = "Subtract u from both sides.",
                            explanationMs = "Tolak u dari kedua-dua belah.",
                            highlightedPart = "− u"
                        ),
                        AlgebraStep(
                            equation = "(v − u) ÷ t = a",
                            explanationEn = "Since a is multiplied by t, divide both sides by t to isolate a.",
                            explanationMs = "Mengenangkan a didarab dengan t, bahagi kedua-dua belah dengan t untuk mengasingkan a.",
                            highlightedPart = "÷ t"
                        ),
                        AlgebraStep(
                            equation = "a = (v − u) ÷ t",
                            explanationEn = "Rearrange to write subject a on the left. Solved!",
                            explanationMs = "Susun semula untuk menulis perkara rumus a di sebelah kiri. Selesai!",
                            highlightedPart = "a ="
                        )
                    )
                    "t" -> listOf(
                        AlgebraStep(
                            equation = "v = u + a × t",
                            explanationEn = "To isolate t, we first move u away.",
                            explanationMs = "Untuk mengasingkan t, kita mengalihkan u terlebih dahulu.",
                            highlightedPart = "t"
                        ),
                        AlgebraStep(
                            equation = "v − u = a × t",
                            explanationEn = "Subtract u from both sides.",
                            explanationMs = "Tolak u dari kedua-dua belah.",
                            highlightedPart = "− u"
                        ),
                        AlgebraStep(
                            equation = "(v − u) ÷ a = t",
                            explanationEn = "Divide both sides by a to leave t isolated.",
                            explanationMs = "Bahagi kedua-dua belah dengan a untuk membiarkan t terasing sahaja.",
                            highlightedPart = "÷ a"
                        ),
                        AlgebraStep(
                            equation = "t = (v − u) ÷ a",
                            explanationEn = "Rearrange compiling t on the left side. Solved!",
                            explanationMs = "Susun semula dengan meletakkan t di sebelah kiri. Selesai!",
                            highlightedPart = "t ="
                        )
                    )
                    else -> emptyList()
                }
            }
        ),

        // A = 1/2 * (a + b) * h (Trapezium Area)
        FormulaTemplate(
            id = "trapezium_area",
            formulaNameEn = "Area of Trapezium",
            formulaNameMs = "Luas Trapezium",
            originalFormula = "A = 1 ÷ 2 × (a + b) × h",
            variables = listOf("h", "a", "b", "A"),
            generateSteps = { target ->
                when (target) {
                    "A" -> listOf(
                        AlgebraStep(
                            equation = "A = (a + b) × h ÷ 2",
                            explanationEn = "The formula is already isolated for surface area A.",
                            explanationMs = "Rumus ini sudah pun berasingan untuk luas tapak A.",
                            highlightedPart = "A"
                        )
                    )
                    "h" -> listOf(
                        AlgebraStep(
                            equation = "A = (a + b) × h ÷ 2",
                            explanationEn = "Let's isolate h. Start by clearing the fraction.",
                            explanationMs = "Mari kita asingkan h. Mula dengan menghapuskan pecahan.",
                            highlightedPart = "h"
                        ),
                        AlgebraStep(
                            equation = "2 × A = (a + b) × h",
                            explanationEn = "Multiply both sides by 2 to clear the fraction (division by 2).",
                            explanationMs = "Darab kedua-dua belah dengan 2 untuk membuang pecahan (bahagi dengan 2).",
                            highlightedPart = "2 ×"
                        ),
                        AlgebraStep(
                            equation = "(2 × A) ÷ (a + b) = h",
                            explanationEn = "Divide both sides by the group (a + b) to isolate h.",
                            explanationMs = "Bahagi kedua-dua belah dengan kumpulan (a + b) untuk mengasingkan h.",
                            highlightedPart = "÷ (a + b)"
                        ),
                        AlgebraStep(
                            equation = "h = (2 × A) ÷ (a + b)",
                            explanationEn = "Rearrange with h on the left side. Complete!",
                            explanationMs = "Susun semula dengan h di sebelah kiri. Selesai!",
                            highlightedPart = "h ="
                        )
                    )
                    "a" -> listOf(
                        AlgebraStep(
                            equation = "A = (a + b) × h ÷ 2",
                            explanationEn = "We want to isolate a. Let's multiply both sides by 2.",
                            explanationMs = "Kita mahu mengasingkan a. Mari darab kedua-dua belah dengan 2.",
                            highlightedPart = "a"
                        ),
                        AlgebraStep(
                            equation = "2 × A = (a + b) × h",
                            explanationEn = "Multiplying by 2 eliminates the division by 2 on the right.",
                            explanationMs = "Mendarab dengan 2 menghapuskan pembahagian dengan 2 di sebelah kanan.",
                            highlightedPart = "2 ×"
                        ),
                        AlgebraStep(
                            equation = "(2 × A) ÷ h = a + b",
                            explanationEn = "Divide both sides by h to open up the brackets group.",
                            explanationMs = "Bahagi kedua-dua belah dengan h untuk membuka kumpulan tanda kurung.",
                            highlightedPart = "÷ h"
                        ),
                        AlgebraStep(
                            equation = "((2 × A) ÷ h) − b = a",
                            explanationEn = "Now, subtract b from both sides to leave variable a fully isolated.",
                            explanationMs = "Kini, tolak b dari kedua-dua belah untuk membiarkan pemboleh ubah a terasing sepenuhnya.",
                            highlightedPart = "− b"
                        ),
                        AlgebraStep(
                            equation = "a = ((2 × A) ÷ h) − b",
                            explanationEn = "Rearrange to write a on the left. Solved!",
                            explanationMs = "Susun semula untuk menulis a di sebelah kiri. Selesai!",
                            highlightedPart = "a ="
                        )
                    )
                    "b" -> listOf(
                        AlgebraStep(
                            equation = "A = (a + b) × h ÷ 2",
                            explanationEn = "We want to isolate b. First, clear the divider by multiplying by 2.",
                            explanationMs = "Kita mahu mengasingkan b. Mula-mula, hapus pembahagi dengan mendarab kedua-dua belah dengan 2.",
                            highlightedPart = "b"
                        ),
                        AlgebraStep(
                            equation = "2 × A = (a + b) × h",
                            explanationEn = "Both sides multiplied by 2.",
                            explanationMs = "Kedua-dua belah didarab dengan 2.",
                            highlightedPart = "2 ×"
                        ),
                        AlgebraStep(
                            equation = "(2 × A) ÷ h = a + b",
                            explanationEn = "Divide both sides by h to extract variables from the parentheses.",
                            explanationMs = "Bahagi kedua-dua belah dengan h untuk mengeluarkan pemboleh ubah dari tanda kurung.",
                            highlightedPart = "÷ h"
                        ),
                        AlgebraStep(
                            equation = "((2 × A) ÷ h) − a = b",
                            explanationEn = "Subtract a from both sides to finish isolating b.",
                            explanationMs = "Tolak a dari kedua-dua belah untuk selesai mengasingkan b.",
                            highlightedPart = "− a"
                        ),
                        AlgebraStep(
                            equation = "b = ((2 × A) ÷ h) − a",
                            explanationEn = "Rearrange with b as the subject on the left-hand side.",
                            explanationMs = "Susun semula dengan b sebagai perkara rumus di sebelah kiri persidangan.",
                            highlightedPart = "b ="
                        )
                    )
                    else -> emptyList()
                }
            }
        ),

        // V = pi * r^2 * h (Cylinder Volume)
        FormulaTemplate(
            id = "cylinder_volume",
            formulaNameEn = "Volume of Cylinder",
            formulaNameMs = "Isipadu Silinder",
            originalFormula = "V = π × r² × h",
            variables = listOf("h", "r", "V"),
            generateSteps = { target ->
                when (target) {
                    "V" -> listOf(
                        AlgebraStep(
                            equation = "V = π × r² × h",
                            explanationEn = "V is already isolated. It stands as the subject.",
                            explanationMs = "V sememangnya sedia terasing. Ia adalah perkara rumus asal.",
                            highlightedPart = "V"
                        )
                    )
                    "h" -> listOf(
                        AlgebraStep(
                            equation = "V = π × r² × h",
                            explanationEn = "To solve for h, note that h is multiplied by the compound (π × r²).",
                            explanationMs = "Untuk menyelesaikan h, perhatikan bahawa h didarab dengan sebatian (π × r²).",
                            highlightedPart = "h"
                        ),
                        AlgebraStep(
                            equation = "V ÷ (π × r²) = h",
                            explanationEn = "Divide both sides by the quantity (π × r²) to clear multiplication.",
                            explanationMs = "Bahagi kedua-dua belah dengan kuantiti (π × j²) untuk menghapuskan pendaraban.",
                            highlightedPart = "÷ (π × r²)"
                        ),
                        AlgebraStep(
                            equation = "h = V ÷ (π × r²)",
                            explanationEn = "Rearrange so the subject h is written on the LHS. Done!",
                            explanationMs = "Susun semula supaya perkara rumus h diletakkan di sebelah kiri. Selesai!",
                            highlightedPart = "h ="
                        )
                    )
                    "r" -> listOf(
                        AlgebraStep(
                            equation = "V = π × r² × h",
                            explanationEn = "To solve for r, let's first isolate r².",
                            explanationMs = "Untuk menyelesaikan r, pertama sekali mari mengasingkan r².",
                            highlightedPart = "r"
                        ),
                        AlgebraStep(
                            equation = "V ÷ (π × h) = r²",
                            explanationEn = "Divide both sides by (π × h) so only the exponential term r² remains on the right.",
                            explanationMs = "Bahagi kedua-dua belah dengan (π × t) supaya hanya sebutan kuadratik j² kekal di sebelah kanan.",
                            highlightedPart = "÷ (π × h)"
                        ),
                        AlgebraStep(
                            equation = "√[V ÷ (π × h)] = r",
                            explanationEn = "Take the square root (punca kuasa dua) of both sides to cancel out the square.",
                            explanationMs = "Ambil punca kuasa dua dari kedua-dua belah untuk membatalkan kuasa dua.",
                            highlightedPart = "√"
                        ),
                        AlgebraStep(
                            equation = "r = √[V ÷ (π × h)]",
                            explanationEn = "Rearrange formatting with r on the LHS as the subject.",
                            explanationMs = "Susun semula meletakkan j di sebelah kiri sebagai perkara rumus baharu.",
                            highlightedPart = "r ="
                        )
                    )
                    else -> emptyList()
                }
            }
        )
    )
}
