buildscript {
    apply("config.gradle.kts")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${project.extra["gradleVersion"]}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}