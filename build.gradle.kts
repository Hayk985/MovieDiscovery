// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(Plugins.buildGradlePlugin)
        classpath(Plugins.kotlinGradlePlugin)
        classpath(Plugins.daggerHiltPlugin)
        classpath(Plugins.navSafeArgsPlugin)
        classpath(Plugins.dependenciesUpdatePlugin)
    }

    extra.apply {
        set(LocalProperties.PROPS_API_KEY, file("api_key.properties"))
        set(LocalProperties.PROPS_KEYSTORE, file("key/keystore.properties"))
        set(LocalProperties.PROPS_KEYSTORE_FILE, file("key/keystore.jks"))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

/** To run this task, run this command in the terminal - ./gradlew DependencyUpdates */
tasks.create(
    "DependencyUpdates",
    com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask::class
) {
    rejectVersionIf {
        candidate.version.contains("alpha") or
            candidate.version.contains("beta") or
            candidate.version.contains("RC", true)
    }

    dependencyUpdates()
}