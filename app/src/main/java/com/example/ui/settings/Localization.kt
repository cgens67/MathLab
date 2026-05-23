package com.example.ui.settings

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object Localization {
    private var appContext: Context? = null

    fun init(context: Context) {
        this.appContext = context.applicationContext
    }

    fun get(key: String, language: String): String {
        // Prefer static translation map first to avoid falling back to English from default XML
        val staticMap = translations[language]
        if (staticMap != null && staticMap.containsKey(key)) {
            return staticMap[key] ?: key
        }

        val context = appContext
        if (context != null) {
            try {
                // Determine the locale based on the selected language
                val locale = when (language) {
                    "Bahasa Melayu" -> Locale("ms")
                    "French" -> Locale("fr")
                    "German" -> Locale("de")
                    "Russian" -> Locale("ru")
                    "Japanese" -> Locale("ja")
                    "Chinese" -> Locale("zh")
                    else -> Locale("en")
                }
                
                val config = Configuration(context.resources.configuration)
                config.setLocale(locale)
                val localizedContext = context.createConfigurationContext(config)
                
                val resId = localizedContext.resources.getIdentifier(key, "string", context.packageName)
                if (resId != 0) {
                    return localizedContext.getString(resId)
                }
            } catch (e: Exception) {
                // Fallback to static mapping if anything fails
            }
        }
        val strings = translations[language] ?: translations["English"] ?: emptyMap()
        return strings[key] ?: translations["English"]?.get(key) ?: key
    }

    private val translations = mapOf(
        "English" to mapOf(
            "app_title" to "Math Expressive",
            "subtitle" to "Tingkatan 2 Mathematics Made Visual",
            "search_lang" to "Search language...",
            "app_lang" to "Application language",
            "appearance" to "Appearance",
            "theme_sec" to "THEME",
            "enable_dynamic" to "Enable dynamic theme",
            "dark_theme" to "Dark theme",
            "disable_blurs" to "Disable blur effects",
            "disable_blurs_desc" to "Disable blur effects throughout the app",
            "use_sys_font" to "Use system font",
            "use_sys_font_desc" to "Use the device font instead of the app font",
            "off" to "Off",
            "on" to "On",
            "search_placeholder" to "Search language...",
            "solved_subject" to "Solved for subject:",
            "steps_title" to "Transformation Steps",
            "select_subject" to "Choose variable to isolate (subject):",
            "back" to "Back",
            "welcome" to "Select a Mathematics Topic",
            "topic_patterns" to "Patterns and Sequences",
            "topic_algebra" to "Algebraic Formulae",
            "topic_polygons" to "Polygons",
            "topic_circles" to "Circles",
            "topic_shapes" to "Three-Dimensional (3D) Shapes",
            "solve" to "Solve",
            "calculate" to "Calculate",
            "result" to "Result",
            "explanation" to "Explanation",
            "hcf_lcm" to "Determine HCF & LCM",
            "expansion" to "Expansion",
            "factorisation" to "Factorisation",
            "subject_title" to "Change Subject of Formula",
            "pola_desc" to "Analyze number sequences, find the pattern, and write down the n-th term general formula easily.",
            "formulae_desc" to "Change the subject of a formula step-by-step, expand products of expressions, or factorise quadratic terms.",
            "polygons_desc" to "Explore interior and exterior angles of regular polygons and calculate sum of angles with diagrams.",
            "circles_desc" to "Calculate chord lengths, circumference, area, arc length and sector area with a visual circle diagram.",
            "shapes_desc" to "Calculate total surface area and volume of 3D shapes (cylinders, cones, spheres) and explore their flat net patterns.",
            "enter_seq" to "Enter number sequence, separated by commas (e.g. 5, 12, 19, 26):",
            "pattern_is" to "Pattern identified:",
            "general_term" to "General term formula:",
            "seq_error" to "Please enter at least 3 valid numbers separated by commas.",
            "factor_title" to "Quadratic Factorisation",
            "factor_desc" to "Factorise ax² + bx + c using cross multiplication (pendaraban silang)",
            "expand_title" to "Expansion Solver",
            "expand_desc" to "Expand (ax + b)(cx + d) step-by-step",
            "hcf_title" to "HCF & LCM Solver",
            "hcf_desc" to "Using repeated division method (pembahagian berulang)",
            "num_list_prompt" to "Enter positive integers separated by commas (e.g. 12, 18, 30):",
            "divide_step" to "Divide by",
            "polygon_sides" to "Number of sides (polygon n-sided):",
            "interior_sum" to "Sum of Interior Angles:",
            "each_interior" to "Each Interior Angle:",
            "each_exterior" to "Each Exterior Angle:",
            "polygon_math_step1" to "Sum of interior angles = (n ÷ 2) × 180° in general, or correctly written as (n - 2) × 180°",
            "circle_radius" to "Circle radius (r):",
            "circle_angle" to "Subtended angle at center (θ°):",
            "circumference" to "Circumference (2 × π × r):",
            "circle_area" to "Circle Area (π × r²):",
            "arc_length" to "Arc Length (θ/360 × 2 × π × r):",
            "sector_area" to "Sector Area (θ/360 × π × r²):",
            "shape_type" to "Select 3D Shape:",
            "volume" to "Volume",
            "surface_area" to "Surface Area",
            "nets_visualizer" to "Nets Visualizer",
            "prism" to "Triangular Prism",
            "pyramid" to "Square Pyramid",
            "cylinder" to "Cylinder",
            "cone" to "Cone",
            "sphere" to "Sphere",
            "h_val" to "Height (h):",
            "r_val" to "Radius (r):",
            "w_val" to "Width (w):",
            "l_val" to "Length (l):",
            "s_val" to "Slant height (s):",
            "student_tip" to "Student Tip: Remember that in school textbooks and formula sheets, a fraction line represents division (÷), and side-by-side variables like 'ab' mean 'a multiplied by b' (a × b).",
            "swipe_to_see" to "Swipe the carousel below to see how each step moves components across the equals (=) sign!"
        ),
        "Bahasa Melayu" to mapOf(
            "app_title" to "Math Expressive",
            "subtitle" to "Matematik Tingkatan 2 Secara Visual",
            "search_lang" to "Cari bahasa...",
            "app_lang" to "Bahasa aplikasi",
            "appearance" to "Rupa Paras",
            "theme_sec" to "TEMA",
            "enable_dynamic" to "Dayakan tema dinamik",
            "dark_theme" to "Tema gelap",
            "disable_blurs" to "Nyahdayakan kesan kabur",
            "disable_blurs_desc" to "Nyahdayakan kesan kabur di seluruh aplikasi",
            "use_sys_font" to "Guna fon sistem",
            "use_sys_font_desc" to "Guna fon peranti berbanding fon aplikasi",
            "off" to "Tutup",
            "on" to "Buka",
            "search_placeholder" to "Cari bahasa...",
            "solved_subject" to "Selesai untuk subjek:",
            "steps_title" to "Langkah-Langkah Transformasi",
            "select_subject" to "Pilih pemboleh ubah untuk diasingkan (subjek):",
            "back" to "Kembali",
            "welcome" to "Pilih Tajuk Matematik",
            "topic_patterns" to "Pola dan Jujukan",
            "topic_algebra" to "Ungkapan & Rumus Algebra",
            "topic_polygons" to "Poligon",
            "topic_circles" to "Bulatan",
            "topic_shapes" to "Bentuk Tiga Dimensi (3D)",
            "solve" to "Selesaikan",
            "calculate" to "Kira",
            "result" to "Keputusan",
            "explanation" to "Penerangan",
            "hcf_lcm" to "Cari FSTB & GSTK",
            "expansion" to "Kembangan",
            "factorisation" to "Pemfaktoran",
            "subject_title" to "Tukar Perkara Rumus (Subject)",
            "pola_desc" to "Analisis jujukan nombor, cari pola, dan tulis rumus sebutan ke-n (general term) dengan mudah.",
            "formulae_desc" to "Tukar perkara rumus langkah demi langkah, kembangkan hasil darab ungkapan, atau faktorkan sebutan kuadratik.",
            "polygons_desc" to "Terokai sudut pedalaman dan peluaran poligon sekata serta kira hasil tambah sudut dengan rajah interaktif.",
            "circles_desc" to "Kira panjang perentas, lilitan, luas, panjang lengkok, dan luas sektor berserta gambar rajah bulatan.",
            "shapes_desc" to "Kira jumlah luas permukaan dan isipadu bentuk 3D (silinder, kon, sfera) serta lihat bentangan (nets).",
            "enter_seq" to "Masukkan jujukan nombor, dipisahkan oleh koma (contoh: 5, 12, 19, 26):",
            "pattern_is" to "Pola yang dikesan:",
            "general_term" to "Rumus sebutan ke-n (Tn):",
            "seq_error" to "Sila masukkan sekurang-kurangnya 3 nombor sah yang dipisahkan oleh koma.",
            "factor_title" to "Pemfaktoran Kuadratik",
            "factor_desc" to "Faktorkan ax² + bx + c menerusi kaedah pendaraban silang",
            "expand_title" to "Penyelesai Kembangan",
            "expand_desc" to "Kembangkan (ax + b)(cx + d) langkah demi langkah",
            "hcf_title" to "Penyelesai FSTB & GSTK",
            "hcf_desc" to "Menggunakan kaedah pembahagian berulang",
            "num_list_prompt" to "Masukkan integer positif dipisahkan dengan koma (contoh: 12, 18, 30):",
            "divide_step" to "Bahagi dengan",
            "polygon_sides" to "Bilangan sisi (nilai n):",
            "interior_sum" to "Hasil Tambah Sudut Pedalaman:",
            "each_interior" to "Setiap Sudut Pedalaman:",
            "each_exterior" to "Setiap Sudut Peluaran:",
            "polygon_math_step1" to "Hasil tambah sudut pedalaman = (n - 2) × 180°",
            "circle_radius" to "Jejari bulatan (j):",
            "circle_angle" to "Sudut tercakup pada pusat (θ°):",
            "circumference" to "Lilitan Bulatan (2 × π × j):",
            "circle_area" to "Luas Bulatan (π × j²):",
            "arc_length" to "Panjang Lengkok (θ/360 × 2 × π × j):",
            "sector_area" to "Luas Sektor (θ/360 × π × j²):",
            "shape_type" to "Pilih Bentuk 3D:",
            "volume" to "Isipadu",
            "surface_area" to "Luas Permukaan",
            "nets_visualizer" to "Visual Bentangan (Net)",
            "prism" to "Prisma Segi Tiga",
            "pyramid" to "Piramid Tapak Segi Empat",
            "cylinder" to "Silinder",
            "cone" to "Kon",
            "sphere" to "Sfera",
            "h_val" to "Tinggi (t):",
            "r_val" to "Jejari (j):",
            "w_val" to "Lebar (l):",
            "l_val" to "Panjang (p):",
            "s_val" to "Tinggi serong (s):",
            "student_tip" to "Tip Pelajar: Ingat bahawa dalam buku teks sekolah, garisan pecahan bermaksud bahagi (÷), dan pemboleh ubah bersebelahan seperti 'ab' bermaksud 'a didarab dengan b' (a × b).",
            "swipe_to_see" to "Leret karusel di bawah untuk melihat bagaimana setiap langkah memindahkan komponen merentasi tanda sama dengan (=)!"
        ),
        "Chinese" to mapOf(
            "app_title" to "MathExpressive 数学实验室",
            "subtitle" to "初二数学（Form 2）直观学习",
            "search_lang" to "搜索语言...",
            "app_lang" to "应用语言",
            "appearance" to "外观设置",
            "theme_sec" to "主题",
            "enable_dynamic" to "启用动态主题",
            "dark_theme" to "深色模式",
            "disable_blurs" to "禁用模糊效果",
            "disable_blurs_desc" to "全局禁用模糊特效",
            "use_sys_font" to "使用系统字体",
            "use_sys_font_desc" to "使用设备内置字体而非应用自带字体",
            "off" to "关闭",
            "on" to "开启",
            "search_placeholder" to "搜索语言...",
            "solved_subject" to "求得公式主项：",
            "steps_title" to "公式变形步骤",
            "select_subject" to "选择要孤立的变量（公式主项）：",
            "back" to "返回",
            "welcome" to "选择数学主题",
            "topic_patterns" to "规律与数列 (Patterns & Sequences)",
            "topic_algebra" to "代数式与公式 (Algebraic Formulae)",
            "topic_polygons" to "多边形 (Polygons)",
            "topic_circles" to "圆 (Circles)",
            "topic_shapes" to "三维几何立体 (3D Shapes)",
            "solve" to "求解",
            "calculate" to "计算",
            "result" to "计算结果",
            "explanation" to "详细解析",
            "hcf_lcm" to "求最大公因数 (HCF) 和最小公倍数 (LCM)",
            "expansion" to "整式展开 (Expansion)",
            "factorisation" to "因式分解 (Factorisation)",
            "subject_title" to "公式主项变换",
            "pola_desc" to "分析数字序列，探索规律并轻松写出第 n 项的通项公式。",
            "formulae_desc" to "逐步变换公式主项、展开多项式或进行二次三项式因式分解。",
            "polygons_desc" to "探索正多边形的内角和外角，通过直观图示计算角度之和。",
            "circles_desc" to "通过直观的圆形图表计算弦长、周长、面积、弧长和扇形面积。",
            "shapes_desc" to "计算三维几何体（圆柱、圆锥、球体）的表面积和体积，并查看其平面展开图。",
            "enter_seq" to "输入数字序列（用逗号隔开，例如 5, 12, 19, 26）：",
            "pattern_is" to "检测到规律：",
            "general_term" to "通项公式 (Tn)：",
            "seq_error" to "请输入至少3个有效的数字且用逗号隔开。",
            "factor_title" to "二次多项式因式分解",
            "factor_desc" to "使用十字相乘法（Cross Multiplication）分解 ax² + bx + c",
            "expand_title" to "整式展开求解器",
            "expand_desc" to "第一步到最后一步详细展开 (ax + b)(cx + d)",
            "hcf_title" to "HCF & LCM 求解器",
            "hcf_desc" to "采用短除法 / 重复相除法（Repeated Division）",
            "num_list_prompt" to "输入正整数（用逗号隔开，例如 12, 18, 30）：",
            "divide_step" to "除以",
            "polygon_sides" to "边数 (n 边形)：",
            "interior_sum" to "内角和：",
            "each_interior" to "每个内角：",
            "each_exterior" to "每个外角：",
            "polygon_math_step1" to "多边形内角和公式：(n - 2) × 180°",
            "circle_radius" to "圆半径 (r)：",
            "circle_angle" to "圆心角 (θ°)：",
            "circumference" to "圆周长 (2 × π × r)：",
            "circle_area" to "圆面积 (π × r²)：",
            "arc_length" to "弧长 (θ/360 × 2 × π × r)：",
            "sector_area" to "扇形面积 (θ/360 × π × r²)：",
            "shape_type" to "选择三维立体：",
            "volume" to "体积",
            "surface_area" to "表面积",
            "nets_visualizer" to "展开图预览",
            "prism" to "三棱柱",
            "pyramid" to "四棱锥",
            "cylinder" to "圆柱体",
            "cone" to "圆锥体",
            "sphere" to "球体",
            "h_val" to "高 (h)：",
            "r_val" to "半径 (r)：",
            "w_val" to "宽 (w)：",
            "l_val" to "长 (l)：",
            "s_val" to "母线长/斜高 (s)：",
            "student_tip" to "学习小贴士：请记住，在教科书和公式表中，分度线代表除法（÷），而如 'ab' 的连写变量表示 'a 乘以 b'（a × b）。",
            "swipe_to_see" to "左右滑动下方的卡片，查看每个变换步骤如何将项移过等号（=）！"
        )
    )
}
