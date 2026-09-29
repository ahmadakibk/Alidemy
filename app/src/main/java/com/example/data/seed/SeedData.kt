package com.example.data.seed

import com.example.data.model.*

object SeedData {

    fun getInitialChapters(): List<Chapter> {
        return listOf(
            // --- GRADE 10 MATHEMATICS ---
            Chapter(
                id = "ch_gr10_math_1",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                chapterNumber = 1,
                title = "Quadratic Equations & Roots",
                summary = "Master standard form ax² + bx + c = 0, discriminant analysis, and quadratic formula application.",
                readTimeMinutes = 15,
                fullContent = """
                    # Quadratic Equations & Root Analysis

                    A quadratic equation in variable x is an equation of the form:
                    **ax² + bx + c = 0**, where a, b, c are real numbers and a ≠ 0.

                    ### 1. Methods of Solution
                    1. **Factorization Method**: Splitting middle term bx into two terms whose product is a * c.
                    2. **Quadratic Formula**: Discovered by ancient mathematicians, giving roots directly:
                       x = (-b ± √(b² - 4ac)) / (2a)

                    ### 2. Nature of Roots (The Discriminant D)
                    The term **D = b² - 4ac** governs the roots:
                    - **D > 0**: Two distinct real roots.
                    - **D = 0**: Two equal real roots (x = -b / 2a).
                    - **D < 0**: No real roots (two complex conjugate roots).

                    ### 3. Practical Applications
                    Quadratic equations model projectile trajectories, profit optimization, projectile velocity, and parabolic satellite dishes.
                """.trimIndent(),
                keyConcepts = listOf(
                    "Standard quadratic form: ax² + bx + c = 0",
                    "Discriminant D = b² - 4ac determines root nature",
                    "Sum of roots: α + β = -b/a",
                    "Product of roots: αβ = c/a"
                ),
                keyFormulas = listOf(
                    "x = (-b ± √(b² - 4ac)) / (2a)",
                    "D = b² - 4ac",
                    "α + β = -b/a, α * β = c/a",
                    "Vertex: (-b/2a, -D/4a)"
                ),
                isBookmarked = true,
                isOfflineSaved = true,
                isCompleted = true
            ),
            Chapter(
                id = "ch_gr10_math_2",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                chapterNumber = 2,
                title = "Trigonometry & Applications of Heights",
                summary = "Trigonometric ratios (sin, cos, tan), Pythagorean identities, and angle of elevation/depression problems.",
                readTimeMinutes = 18,
                fullContent = """
                    # Introduction to Trigonometry & Heights

                    Trigonometry studies relationships between side lengths and angles of right-angled triangles.

                    ### 1. Fundamental Ratios
                    For right triangle with acute angle θ:
                    - sin(θ) = Opposite / Hypotenuse
                    - cos(θ) = Adjacent / Hypotenuse
                    - tan(θ) = sin(θ) / cos(θ) = Opposite / Adjacent
                    - cosec(θ) = 1/sin(θ), sec(θ) = 1/cos(θ), cot(θ) = 1/tan(θ)

                    ### 2. Standard Angle Values (0°, 30°, 45°, 60°, 90°)
                    - sin(30°) = 1/2, sin(45°) = 1/√2, sin(60°) = √3/2
                    - cos(30°) = √3/2, cos(45°) = 1/√2, cos(60°) = 1/2
                    - tan(45°) = 1, tan(30°) = 1/√3, tan(60°) = √3

                    ### 3. Trigonometric Identities
                    - sin²(θ) + cos²(θ) = 1
                    - 1 + tan²(θ) = sec²(θ)
                    - 1 + cot²(θ) = cosec²(θ)
                """.trimIndent(),
                keyConcepts = listOf(
                    "Ratios: SOH-CAH-TOA relationships",
                    "Pythagorean identities in unit circle",
                    "Angle of Elevation vs Angle of Depression"
                ),
                keyFormulas = listOf(
                    "sin²θ + cos²θ = 1",
                    "sec²θ - tan²θ = 1",
                    "cosec²θ - cot²θ = 1",
                    "tan θ = sin θ / cos θ"
                ),
                isBookmarked = false,
                isOfflineSaved = true,
                isCompleted = false
            ),

            // --- GRADE 10 SCIENCE ---
            Chapter(
                id = "ch_gr10_sci_1",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                chapterNumber = 1,
                title = "Light: Reflection & Refraction",
                summary = "Spherical mirrors, Snell's law, lens formula, magnification, and power of corrective optical lenses.",
                readTimeMinutes = 20,
                fullContent = """
                    # Light: Reflection & Refraction

                    Light is an electromagnetic radiation enabling vision. Reflection and refraction explain optical phenomena.

                    ### 1. Laws of Reflection
                    1. Angle of incidence equals angle of reflection (∠i = ∠r).
                    2. Incident ray, reflected ray, and normal at point of incidence lie in the same plane.

                    ### 2. Spherical Mirrors & Lens Formula
                    - **Mirror Formula**: 1/f = 1/v + 1/u
                    - **Magnification (Mirror)**: m = -v/u = h'/h
                    - **Lens Formula**: 1/f = 1/v - 1/u
                    - **Magnification (Lens)**: m = v/u = h'/h

                    ### 3. Snell's Law & Refractive Index
                    The ratio of sine of angle of incidence to sine of angle of refraction is constant:
                    sin(i) / sin(r) = n₂ / n₁
                """.trimIndent(),
                keyConcepts = listOf(
                    "Cartesian sign convention (+ vs - focal lengths)",
                    "Snell's Law: n₁ sin(i) = n₂ sin(r)",
                    "Real vs Virtual, Inverted vs Erect images",
                    "Power of Lens P = 1 / f (in meters), unit: Dioptres (D)"
                ),
                keyFormulas = listOf(
                    "1/f = 1/v - 1/u (Lens Formula)",
                    "1/f = 1/v + 1/u (Mirror Formula)",
                    "P = 1/f(meters) [Dioptres]",
                    "m = h'/h = v/u (for lens)"
                ),
                isBookmarked = true,
                isOfflineSaved = true,
                isCompleted = false
            ),
            Chapter(
                id = "ch_gr10_sci_2",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                chapterNumber = 2,
                title = "Chemical Reactions & Catalysis",
                summary = "Balancing stoichiometric equations, redox processes, endothermic vs exothermic reactions, and corrosion.",
                readTimeMinutes = 14,
                fullContent = """
                    # Chemical Reactions & Equations

                    A chemical reaction involves breaking old chemical bonds and forming new bonds to create distinct substances.

                    ### Types of Reactions:
                    1. **Combination Reaction**: A + B → AB (e.g. CaO + H₂O → Ca(OH)₂)
                    2. **Decomposition Reaction**: AB → A + B (requires heat/light/electricity)
                    3. **Displacement Reaction**: More reactive metal displaces less reactive metal.
                    4. **Double Displacement**: Exchange of ions between reactants (precipitation).
                    5. **Redox Reactions**: Simultaneous Oxidation (loss of electrons / gain of oxygen) and Reduction (gain of electrons / loss of oxygen).
                """.trimIndent(),
                keyConcepts = listOf(
                    "Law of Conservation of Mass",
                    "Oxidizing and Reducing agents",
                    "Exothermic vs Endothermic energy diagrams",
                    "Rancidity and antioxidant prevention"
                ),
                keyFormulas = listOf(
                    "CaO(s) + H₂O(l) → Ca(OH)₂(aq) + Heat",
                    "2Pb(NO₃)₂ → 2PbO + 4NO₂ + O₂",
                    "CuO + H₂ → Cu + H₂O (Redox)"
                ),
                isBookmarked = false,
                isOfflineSaved = true,
                isCompleted = true
            ),

            // --- GRADE 10 COMPUTER SCIENCE ---
            Chapter(
                id = "ch_gr10_cs_1",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                chapterNumber = 1,
                title = "Algorithmic Thinking & Python Logic",
                summary = "Conditionals, iterative loops, lists/dictionaries, function scopes, and computational complexity basics.",
                readTimeMinutes = 16,
                fullContent = """
                    # Algorithmic Thinking & Python Logic

                    Computational thinking breaks problems down into discrete, algorithmic steps easily translated to software.

                    ### Key Topics:
                    - **Variables & Data Types**: int, float, str, bool, list, tuple, dict.
                    - **Control Structures**: if-elif-else branching.
                    - **Loops**: while loops and for-in loops with range().
                    - **Functions**: Def block, parameters, return values, and variable scope.
                    - **Complexity**: Big-O notation introduction (O(1), O(n), O(n²)).
                """.trimIndent(),
                keyConcepts = listOf(
                    "Algorithmic decomposition & pseudo-code",
                    "Immutable tuples vs mutable lists",
                    "Key-value hashing in dictionaries",
                    "Recursion and base condition termination"
                ),
                keyFormulas = listOf(
                    "Binary Search: O(log n)",
                    "Linear Search: O(n)",
                    "List comprehension: [x**2 for x in nums if x % 2 == 0]"
                ),
                isBookmarked = true,
                isOfflineSaved = true,
                isCompleted = true
            ),

            // --- GRADE 12 MATHEMATICS ---
            Chapter(
                id = "ch_gr12_math_1",
                gradeLevel = 12,
                subject = Subject.MATHEMATICS,
                chapterNumber = 1,
                title = "Differential Calculus & Rate of Change",
                summary = "Limits, continuity, differentiability, chain rule, product rule, and maximum/minimum optimization.",
                readTimeMinutes = 22,
                fullContent = """
                    # Differential Calculus & Derivatives

                    Calculus is the mathematical study of continuous change. The derivative f'(x) represents the instantaneous rate of change.

                    ### 1. Fundamental Rules of Differentiation
                    - Power Rule: d/dx(xⁿ) = n * xⁿ⁻¹
                    - Product Rule: d/dx(u * v) = u'v + uv'
                    - Quotient Rule: d/dx(u/v) = (u'v - uv') / v²
                    - Chain Rule: d/dx(f(g(x))) = f'(g(x)) * g'(x)

                    ### 2. Applications of Derivatives
                    - Tangent and Normal lines: Slope m = dy/dx at (x₀, y₀)
                    - Maxima and Minima: First derivative test (f'(x) = 0) and Second derivative test (f''(x) < 0 for local maximum, f''(x) > 0 for local minimum).
                """.trimIndent(),
                keyConcepts = listOf(
                    "Instantaneous rate of change as tangent slope",
                    "Chain rule for composite trigonometric/exponential functions",
                    "Second derivative concavity test",
                    "L'Hôpital's rule for indeterminate forms (0/0, ∞/∞)"
                ),
                keyFormulas = listOf(
                    "d/dx [sin x] = cos x, d/dx [cos x] = -sin x",
                    "d/dx [eˣ] = eˣ, d/dx [ln x] = 1/x",
                    "d/dx [u/v] = (v du/dx - u dv/dx) / v²",
                    "Critical points: dy/dx = 0"
                ),
                isBookmarked = true,
                isOfflineSaved = true,
                isCompleted = false
            ),

            // --- GRADE 12 SCIENCE (PHYSICS/CHEM) ---
            Chapter(
                id = "ch_gr12_sci_1",
                gradeLevel = 12,
                subject = Subject.SCIENCE,
                chapterNumber = 1,
                title = "Electromagnetic Waves & Quantum Dualism",
                summary = "Maxwell's equations, photoelectric effect, De Broglie wavelength, and wave-particle duality.",
                readTimeMinutes = 18,
                fullContent = """
                    # Electromagnetic Waves & Modern Physics

                    Modern physics bridges classical Maxwellian electrodynamics with quantum mechanics initiated by Planck and Einstein.

                    ### 1. Einstein's Photoelectric Equation
                    When photons of frequency ν strike metal with work function Φ:
                    **E_kinetic_max = hν - Φ = h(ν - ν₀)**
                    Where h is Planck's constant (6.626 × 10⁻³⁴ J·s).

                    ### 2. De Broglie Wavelength
                    Matter possesses dual wave-particle character:
                    **λ = h / p = h / (m * v)**
                """.trimIndent(),
                keyConcepts = listOf(
                    "Threshold frequency ν₀ requirement",
                    "Independence of kinetic energy from light intensity",
                    "De Broglie matter wavelength λ = h/p",
                    "Energy of photon E = hc/λ"
                ),
                keyFormulas = listOf(
                    "K_max = hν - W₀",
                    "λ = h / (m * v)",
                    "c = 1 / √(μ₀ ε₀) = 3 × 10⁸ m/s",
                    "E = m c²"
                ),
                isBookmarked = false,
                isOfflineSaved = true,
                isCompleted = false
            ),

            // --- GRADE 8 SCIENCE ---
            Chapter(
                id = "ch_gr8_sci_1",
                gradeLevel = 8,
                subject = Subject.SCIENCE,
                chapterNumber = 1,
                title = "Cell Structure & Microorganisms",
                summary = "Prokaryotic vs eukaryotic cells, plant vs animal cells, organelles, and beneficial vs pathogenic microbes.",
                readTimeMinutes = 12,
                fullContent = """
                    # Cell Structure & Functions

                    The cell is the basic structural and functional unit of all living organisms.

                    ### 1. Discovery & Theory
                    - Robert Hooke observed cork cells in 1665.
                    - Cell Theory: All organisms are composed of cells; cells arise from pre-existing cells.

                    ### 2. Plant vs Animal Cells
                    - Plant cells possess a rigid cellulose **cell wall** and photosynthetic **chloroplasts**.
                    - Animal cells have centrioles and smaller, multiple vacuoles.
                """.trimIndent(),
                keyConcepts = listOf(
                    "Mitochondria: Powerhouse of the cell",
                    "Nucleus: Genetic information controller",
                    "Cell membrane: Selectively permeable barrier"
                ),
                keyFormulas = listOf(
                    "Photosynthesis: 6CO₂ + 6H₂O + sunlight → C₆H₁₂O₆ + 6O₂",
                    "Magnification = Image size / Actual size"
                ),
                isBookmarked = true,
                isOfflineSaved = true,
                isCompleted = true
            ),

            // --- GRADE 8 SOCIAL STUDIES ---
            Chapter(
                id = "ch_gr8_soc_1",
                gradeLevel = 8,
                subject = Subject.SOCIAL_STUDIES,
                chapterNumber = 1,
                title = "Our Constitution & Fundamental Rights",
                summary = "The preamble, key features of democratic federalism, separation of powers, and civic responsibilities.",
                readTimeMinutes = 14,
                fullContent = """
                    # The Constitution & Civil Rights

                    A constitution provides the fundamental legal framework and moral compass for the governance of a nation.

                    ### Key Pillars:
                    1. **Federalism**: Division of powers between national and state jurisdictions.
                    2. **Separation of Powers**: Legislature, Executive, and Judiciary.
                    3. **Fundamental Rights**: Right to Equality, Freedom of Speech, Freedom of Religion, Constitutional Remedies.
                """.trimIndent(),
                keyConcepts = listOf(
                    "Sovereign, Socialist, Secular, Democratic Republic",
                    "Independent Judiciary and Rule of Law",
                    "Directive Principles of State Policy"
                ),
                keyFormulas = listOf(
                    "Three Branches: Legislative, Executive, Judicial checks & balances"
                ),
                isBookmarked = false,
                isOfflineSaved = true,
                isCompleted = false
            )
        )
    }

    fun getInitialQuizzes(): List<Quiz> {
        return listOf(
            Quiz(
                id = "quiz_gr10_math_quad",
                title = "Quadratic Equations Mastery Test",
                subject = Subject.MATHEMATICS,
                gradeLevel = 10,
                durationMinutes = 10,
                questions = listOf(
                    QuizQuestion(
                        id = "q1",
                        questionText = "If the roots of ax² + bx + c = 0 are real and equal, what is the value of the discriminant (b² - 4ac)?",
                        options = listOf("Greater than zero (> 0)", "Equal to zero (= 0)", "Less than zero (< 0)", "Equal to 1"),
                        correctIndex = 1,
                        explanation = "When b² - 4ac = 0, both roots evaluate to -b/(2a), meaning there are two real and coincident (equal) roots."
                    ),
                    QuizQuestion(
                        id = "q2",
                        questionText = "What are the roots of the equation x² - 5x + 6 = 0?",
                        options = listOf("x = 2, 3", "x = -2, -3", "x = 1, 6", "x = -1, 6"),
                        correctIndex = 0,
                        explanation = "Factoring (x - 2)(x - 3) = 0 gives roots x = 2 and x = 3."
                    ),
                    QuizQuestion(
                        id = "q3",
                        questionText = "For the equation 2x² - 8x + 3 = 0, what is the sum of the roots?",
                        options = listOf("8", "-8", "4", "-4"),
                        correctIndex = 2,
                        explanation = "Sum of roots α + β = -b/a = -(-8)/2 = 8/2 = 4."
                    ),
                    QuizQuestion(
                        id = "q4",
                        questionText = "If one root of quadratic equation kx² - 14x + 8 = 0 is six times the other, what is k?",
                        options = listOf("1", "3", "5", "7"),
                        correctIndex = 1,
                        explanation = "Let roots be α and 6α. Sum = 7α = 14/k ⇒ α = 2/k. Product = 6α² = 8/k ⇒ 6(4/k²) = 8/k ⇒ 24/k = 8 ⇒ k = 3."
                    )
                )
            ),
            Quiz(
                id = "quiz_gr10_sci_light",
                title = "Light: Reflection & Refraction Quiz",
                subject = Subject.SCIENCE,
                gradeLevel = 10,
                durationMinutes = 8,
                questions = listOf(
                    QuizQuestion(
                        id = "qs1",
                        questionText = "A concave mirror produces a magnification of -1 for an object. Where is the object located?",
                        options = listOf("Between pole and focus", "At the focus (F)", "At the center of curvature (C)", "At infinity"),
                        correctIndex = 2,
                        explanation = "When the object is placed at the center of curvature C, a real, inverted image of the exact same size (magnification m = -1) is formed at C."
                    ),
                    QuizQuestion(
                        id = "qs2",
                        questionText = "The power of a convex lens with focal length 50 cm is:",
                        options = listOf("+2.0 D", "-2.0 D", "+0.5 D", "+5.0 D"),
                        correctIndex = 0,
                        explanation = "Power P = 1 / f(in meters). Since 50 cm = 0.5 m, Power P = 1 / 0.5 = +2.0 Dioptres."
                    ),
                    QuizQuestion(
                        id = "qs3",
                        questionText = "Which optical phenomenon is primarily responsible for the twinkling of stars?",
                        options = listOf("Atmospheric refraction", "Internal reflection", "Light dispersion", "Optical interference"),
                        correctIndex = 0,
                        explanation = "Starlight passes through fluctuating layers of the Earth's atmosphere with varying refractive indices, causing continuous atmospheric refraction."
                    )
                )
            ),
            Quiz(
                id = "quiz_gr12_math_calc",
                title = "Differential Calculus Challenge",
                subject = Subject.MATHEMATICS,
                gradeLevel = 12,
                durationMinutes = 12,
                questions = listOf(
                    QuizQuestion(
                        id = "qc1",
                        questionText = "What is the derivative of f(x) = x * ln(x)?",
                        options = listOf("1/x", "ln(x)", "ln(x) + 1", "x + ln(x)"),
                        correctIndex = 2,
                        explanation = "Applying the product rule: d/dx[x * ln x] = (1)*ln(x) + x*(1/x) = ln(x) + 1."
                    ),
                    QuizQuestion(
                        id = "qc2",
                        questionText = "At which value of x does the function f(x) = x² - 6x + 5 have a local minimum?",
                        options = listOf("x = 0", "x = 3", "x = 5", "x = -3"),
                        correctIndex = 1,
                        explanation = "f'(x) = 2x - 6. Setting f'(x) = 0 yields x = 3. Since f''(x) = 2 > 0, it represents a local minimum."
                    ),
                    QuizQuestion(
                        id = "qc3",
                        questionText = "Evaluate the limit lim(x→0) [sin(3x) / x]:",
                        options = listOf("0", "1", "3", "Does not exist"),
                        correctIndex = 2,
                        explanation = "lim(x→0) [sin(3x) / (3x) * 3] = 1 * 3 = 3."
                    )
                )
            ),
            Quiz(
                id = "quiz_gr10_cs_python",
                title = "Python Data Structures & Logic",
                subject = Subject.COMPUTER_SCIENCE,
                gradeLevel = 10,
                durationMinutes = 10,
                questions = listOf(
                    QuizQuestion(
                        id = "qp1",
                        questionText = "What will be the output of `len([1, 2, [3, 4]])` in Python?",
                        options = listOf("4", "3", "2", "TypeError"),
                        correctIndex = 1,
                        explanation = "The list has three elements: integer 1, integer 2, and the nested sublist [3, 4]."
                    ),
                    QuizQuestion(
                        id = "qp2",
                        questionText = "Which Python data structure is defined with curly braces `{}` and contains unique keys?",
                        options = listOf("Tuple", "List", "Dictionary", "Array"),
                        correctIndex = 2,
                        explanation = "Dictionaries `{key: value}` store key-value pairs with unique, hashable keys."
                    )
                )
            )
        )
    }

    fun getInitialVideoLectures(): List<VideoLecture> {
        return listOf(
            VideoLecture(
                id = "vid_gr10_math_quad",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                title = "Quadratic Equations in Real-World Physics",
                instructorName = "Dr. Marcus Vance",
                instructorTitle = "Senior Mathematics Faculty",
                durationSeconds = 720, // 12 mins
                keyTimestamps = listOf(
                    VideoTimestamp(0, "Introduction & Standard Form"),
                    VideoTimestamp(140, "Splitting the Middle Term"),
                    VideoTimestamp(360, "Deriving Quadratic Formula"),
                    VideoTimestamp(540, "Trajectory & Peak Height Problems")
                ),
                summary = "An intuitive visual breakdown of how parabolic trajectories form quadratic curves and how to rapidly determine roots.",
                transcript = "Welcome students to Alidemy! Today we explore quadratic curves. Notice how a basketball in flight describes a parabola. Let's write the mathematical equation: y = -16t² + vt + h...",
                isDownloaded = true,
                watchProgressPercent = 0.65f
            ),
            VideoLecture(
                id = "vid_gr10_sci_optics",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                title = "Ray Optics: Ray Diagrams & Snell's Law",
                instructorName = "Prof. Elena Rostova",
                instructorTitle = "Dept of Applied Physics",
                durationSeconds = 960, // 16 mins
                keyTimestamps = listOf(
                    VideoTimestamp(0, "Optical Reflection Basics"),
                    VideoTimestamp(210, "Concave vs Convex Ray Diagrams"),
                    VideoTimestamp(500, "Refraction Through Glass Slabs"),
                    VideoTimestamp(750, "Magnification & Lens Calculations")
                ),
                summary = "Step-by-step ray tracing demonstrations with live optical bench simulations showing focal point convergence.",
                transcript = "Hello learners! Today on Alidemy Physics, we're tracing rays of light. When parallel rays hit a convex lens, they converge precisely at the principal focus...",
                isDownloaded = false,
                watchProgressPercent = 0.20f
            ),
            VideoLecture(
                id = "vid_gr12_math_deriv",
                gradeLevel = 12,
                subject = Subject.MATHEMATICS,
                title = "Calculus: Geometric Intuition of Derivatives",
                instructorName = "Dr. Marcus Vance",
                instructorTitle = "Senior Mathematics Faculty",
                durationSeconds = 1080, // 18 mins
                keyTimestamps = listOf(
                    VideoTimestamp(0, "Secant Lines to Tangent Lines"),
                    VideoTimestamp(280, "The Chain Rule Decoded"),
                    VideoTimestamp(620, "Optimization Problems: Maximizing Area"),
                    VideoTimestamp(900, "Summary & Board Exam Tips")
                ),
                summary = "Visual geometric explanation of dx and dy approaching zero, unlocking effortless calculus intuition.",
                transcript = "Derivative is simply the slope of the curve at a microscopic point. As Δx zooms to zero, secant merges into a tangent...",
                isDownloaded = true,
                watchProgressPercent = 0.85f
            ),
            VideoLecture(
                id = "vid_gr10_cs_algo",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                title = "Data Structures & Computational Complexity",
                instructorName = "Sarah Jenkins",
                instructorTitle = "Lead Software Engineer & Mentor",
                durationSeconds = 840,
                keyTimestamps = listOf(
                    VideoTimestamp(0, "Why Complexity Matters"),
                    VideoTimestamp(180, "Linear vs Binary Search in Action"),
                    VideoTimestamp(450, "Stacks & Queues in Operating Systems"),
                    VideoTimestamp(690, "Hands-on Python Implementation")
                ),
                summary = "Learn how computers scale search algorithms from milliseconds to seconds, and build your first binary search in Python.",
                transcript = "Imagine searching a phone book of 1 million names. Linear search checks 1 million times, but binary search finishes in just 20 checks!",
                isDownloaded = false,
                watchProgressPercent = 0.0f
            )
        )
    }

    fun getInitialForumPosts(): List<ForumPost> {
        val now = System.currentTimeMillis()
        val oneHour = 3600 * 1000L
        val oneDay = 24 * 3600 * 1000L

        return listOf(
            ForumPost(
                id = "post_1",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                authorName = "Rohan Sharma",
                authorRole = "Grade 10 Student",
                title = "How to quickly identify whether a quadratic has real roots without full calculation?",
                content = "I usually calculate the whole formula, which takes time during timed tests. Is checking b² - 4ac alone sufficient, and are there shortcuts when b is even?",
                upvotes = 14,
                replyCount = 3,
                isSolved = true,
                isUpvotedByMe = true,
                tags = listOf("Algebra", "ExamShortcuts", "Math"),
                timestamp = now - 2 * oneHour,
                syncStatus = "SYNCED"
            ),
            ForumPost(
                id = "post_2",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                authorName = "Amina K.",
                authorRole = "Grade 10 Peer Tutor",
                title = "Sign convention confusion in Lens Formula (1/f = 1/v - 1/u)",
                content = "When solving concave lens questions, is the focal length always negative? What about the image distance v when the image is virtual?",
                upvotes = 9,
                replyCount = 2,
                isSolved = true,
                isUpvotedByMe = false,
                tags = listOf("Physics", "Optics", "Formulas"),
                timestamp = now - 5 * oneHour,
                syncStatus = "SYNCED"
            ),
            ForumPost(
                id = "post_3",
                gradeLevel = 12,
                subject = Subject.MATHEMATICS,
                authorName = "David Kim",
                authorRole = "Grade 12 Student",
                title = "Chain Rule with composite trigonometric powers: d/dx [sin³(4x²)]",
                content = "Can someone check my steps? I got 24x * sin²(4x²) * cos(4x²). Did I apply outer-inner-innermost properly?",
                upvotes = 11,
                replyCount = 1,
                isSolved = true,
                isUpvotedByMe = false,
                tags = listOf("Calculus", "Differentiation"),
                timestamp = now - oneDay,
                syncStatus = "SYNCED"
            ),
            ForumPost(
                id = "post_4",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                authorName = "Maya Patel",
                authorRole = "Grade 10 Student",
                title = "Difference between `==` and `is` in Python with lists",
                content = "Why does `a = [1,2]; b = [1,2]; a == b` return True, but `a is b` return False?",
                upvotes = 7,
                replyCount = 2,
                isSolved = false,
                isUpvotedByMe = false,
                tags = listOf("Python", "Memory", "Coding"),
                timestamp = now - 2 * oneDay,
                syncStatus = "SYNCED"
            )
        )
    }

    fun getInitialForumReplies(): List<ForumReply> {
        val now = System.currentTimeMillis()
        val oneHour = 3600 * 1000L

        return listOf(
            ForumReply(
                id = "rep_1",
                postId = "post_1",
                authorName = "Prof. Elena Rostova",
                authorRole = "Verified Mentor",
                content = "Yes! Always calculate the discriminant D = b² - 4ac first. If b is even, say b = 2k, use the reduced discriminant: D/4 = k² - ac. If k² - ac >= 0, real roots exist! This saves huge computation time.",
                upvotes = 18,
                isAcceptedSolution = true,
                timestamp = now - 1 * oneHour
            ),
            ForumReply(
                id = "rep_2",
                postId = "post_1",
                authorName = "Alex Chen",
                authorRole = "Grade 10 Student",
                content = "Also remember that if a and c have opposite signs, ac < 0, so -4ac is strictly positive. That means D is guaranteed to be positive (> 0) without any math!",
                upvotes = 6,
                isAcceptedSolution = false,
                timestamp = now - 45 * 60 * 1000L
            ),
            ForumReply(
                id = "rep_3",
                postId = "post_2",
                authorName = "Dr. Marcus Vance",
                authorRole = "Verified Mentor",
                content = "For a concave lens: focal length f is ALWAYS negative. Because it always forms a virtual and erect image on the same side as the object, v is also ALWAYS negative!",
                upvotes = 12,
                isAcceptedSolution = true,
                timestamp = now - 4 * oneHour
            ),
            ForumReply(
                id = "rep_4",
                postId = "post_3",
                authorName = "Prof. Elena Rostova",
                authorRole = "Verified Mentor",
                content = "Spot on! Step 1 (power): 3*sin²(4x²). Step 2 (trig): cos(4x²). Step 3 (inner argument): 8x. Multiplying them together gives exactly 24x * sin²(4x²) * cos(4x²). Perfect execution!",
                upvotes = 8,
                isAcceptedSolution = true,
                timestamp = now - 20 * oneHour
            )
        )
    }

    fun getInitialDeadlines(): List<ExamDeadline> {
        val now = System.currentTimeMillis()
        val oneDay = 24 * 3600 * 1000L
        val oneHour = 3600 * 1000L

        return listOf(
            ExamDeadline(
                id = "dl_1",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                title = "Grade 10 Mid-Term Board Mathematics Exam",
                dueTimestamp = now + 2 * oneDay + 4 * oneHour,
                reminderHoursBefore = 24,
                notes = "Covers Chapters 1-5: Quadratic Equations, Arithmetic Progressions, Trigonometry, and Circles. Bring geometry box.",
                isCompleted = false,
                type = DeadlineType.EXAM
            ),
            ExamDeadline(
                id = "dl_2",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                title = "Physics Lab Journal & Ray Optics Assignment",
                dueTimestamp = now + 1 * oneDay + 6 * oneHour,
                reminderHoursBefore = 12,
                notes = "Submit ray diagrams for concave/convex lenses and power calculation worksheet.",
                isCompleted = false,
                type = DeadlineType.ASSIGNMENT
            ),
            ExamDeadline(
                id = "dl_3",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                title = "Python Coding Challenge: Mini Project",
                dueTimestamp = now + 4 * oneDay,
                reminderHoursBefore = 24,
                notes = "Implement student grade manager with dictionaries and exception handling.",
                isCompleted = false,
                type = DeadlineType.PROJECT
            ),
            ExamDeadline(
                id = "dl_4",
                gradeLevel = 10,
                subject = Subject.ENGLISH,
                title = "World Literature Rhetorical Essay",
                dueTimestamp = now + 5 * oneDay + 8 * oneHour,
                reminderHoursBefore = 24,
                notes = "500-word argumentative essay on ethical themes in contemporary drama.",
                isCompleted = true,
                type = DeadlineType.ASSIGNMENT
            ),
            ExamDeadline(
                id = "dl_5",
                gradeLevel = 12,
                subject = Subject.MATHEMATICS,
                title = "Pre-College Calculus & Derivatives Mock Exam",
                dueTimestamp = now + 3 * oneDay,
                reminderHoursBefore = 24,
                notes = "Timed 3-hour mock paper covering Limits, Chain Rule, and Maxima/Minima optimization.",
                isCompleted = false,
                type = DeadlineType.EXAM
            )
        )
    }

    fun getInitialFlashcards(): List<Flashcard> {
        return listOf(
            // Mathematics Flashcards
            Flashcard(
                id = "fc_math_1",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                deckTitle = "Algebra & Quadratic Equations",
                term = "Quadratic Formula",
                definition = "x = (-b ± √(b² - 4ac)) / (2a)\nUsed to determine the roots of any quadratic equation in the form ax² + bx + c = 0.",
                hint = "Think about the discriminant under the square root",
                masteryLevel = 1
            ),
            Flashcard(
                id = "fc_math_2",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                deckTitle = "Algebra & Quadratic Equations",
                term = "Discriminant (D)",
                definition = "D = b² - 4ac.\n• D > 0: Two distinct real roots\n• D = 0: Two real and equal roots\n• D < 0: No real roots (complex roots)",
                hint = "The value inside the radical in the quadratic formula",
                masteryLevel = 2
            ),
            Flashcard(
                id = "fc_math_3",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                deckTitle = "Trigonometry Essentials",
                term = "Fundamental Pythagorean Identity",
                definition = "sin²(θ) + cos²(θ) = 1\nDerived directly from the Pythagorean theorem on the unit circle (x² + y² = r²).",
                hint = "Relates sine and cosine squares",
                masteryLevel = 0
            ),
            Flashcard(
                id = "fc_math_4",
                gradeLevel = 10,
                subject = Subject.MATHEMATICS,
                deckTitle = "Trigonometry Essentials",
                term = "SOH-CAH-TOA",
                definition = "• sin(θ) = Opposite / Hypotenuse\n• cos(θ) = Adjacent / Hypotenuse\n• tan(θ) = Opposite / Adjacent",
                hint = "Mnemonic for right-triangle trig ratios",
                masteryLevel = 2
            ),

            // Science Flashcards
            Flashcard(
                id = "fc_sci_1",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                deckTitle = "Optics & Light",
                term = "Snell's Law of Refraction",
                definition = "n₁·sin(θ₁) = n₂·sin(θ₂)\nThe ratio of the sine of the angle of incidence to the sine of the angle of refraction equals the ratio of refractive indices.",
                hint = "Governs bending of light at boundary",
                masteryLevel = 1
            ),
            Flashcard(
                id = "fc_sci_2",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                deckTitle = "Optics & Light",
                term = "Lens Formula vs Mirror Formula",
                definition = "• Thin Lens Formula: 1/f = 1/v - 1/u\n• Mirror Formula: 1/f = 1/v + 1/u\n(where f = focal length, v = image distance, u = object distance)",
                hint = "Minus for lens, plus for mirror",
                masteryLevel = 0
            ),
            Flashcard(
                id = "fc_sci_3",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                deckTitle = "Optics & Light",
                term = "Optical Power of a Lens (P)",
                definition = "P = 1 / f (in meters)\nMeasured in Dioptres (D). Convex lenses have positive power (+D), concave lenses have negative power (-D).",
                hint = "Reciprocal of focal length in meters",
                masteryLevel = 2
            ),
            Flashcard(
                id = "fc_sci_4",
                gradeLevel = 10,
                subject = Subject.SCIENCE,
                deckTitle = "Cell Biology & Genetics",
                term = "Mitochondria",
                definition = "The 'Powerhouse of the Cell'. Double-membraned organelle where ATP (adenosine triphosphate) is synthesized via cellular respiration.",
                hint = "Organelle that produces ATP",
                masteryLevel = 2
            ),

            // Computer Science Flashcards
            Flashcard(
                id = "fc_cs_1",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                deckTitle = "Algorithms & Data Structures",
                term = "Binary Search Complexity",
                definition = "Time Complexity: O(log n)\nDivides search interval in half each step. Requires the input collection to be sorted.",
                hint = "Logarithmic time search",
                masteryLevel = 1
            ),
            Flashcard(
                id = "fc_cs_2",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                deckTitle = "Algorithms & Data Structures",
                term = "Hash Map / Dictionary",
                definition = "A collection of key-value pairs where each key maps to a value via a hashing function, allowing average O(1) lookup, insertion, and deletion.",
                hint = "Key-value pair data structure",
                masteryLevel = 2
            ),
            Flashcard(
                id = "fc_cs_3",
                gradeLevel = 10,
                subject = Subject.COMPUTER_SCIENCE,
                deckTitle = "Algorithms & Data Structures",
                term = "Recursion & Base Case",
                definition = "A function that solves a problem by calling itself with smaller sub-problems. Must have a 'base case' to terminate and prevent infinite stack overflow.",
                hint = "Self-calling function mechanism",
                masteryLevel = 0
            )
        )
    }
}
