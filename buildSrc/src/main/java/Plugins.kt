object Plugins {
    private const val gradleVersion = "7.1.3"
    private const val kotlinVersion = "1.7.0"
    private const val dependenciesUpdateVersion = "0.42.0"

    const val buildGradlePlugin =
        "com.android.tools.build:gradle:${gradleVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}"
    const val daggerHiltPlugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHilt}"
    const val navSafeArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val dependenciesUpdatePlugin =
        "com.github.ben-manes:gradle-versions-plugin:${dependenciesUpdateVersion}"
}