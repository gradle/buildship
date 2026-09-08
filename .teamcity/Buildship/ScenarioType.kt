package Buildship

enum class ScenarioType(val gradleTasks: String, val runsTests: Boolean = true) {
    SANITY_CHECK("assemble checkstyleMain", runsTests = false),
    BASIC_COVERAGE("clean eclipseTest"),
    FULL_COVERAGE("clean build"),
    CROSS_VERSION("clean crossVersionEclipseTest")
}
