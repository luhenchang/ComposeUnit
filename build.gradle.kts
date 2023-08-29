buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.build.gradle)
        classpath(libs.kotlin.gradle.plugin)
    }
}
plugins{
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}
task<Delete>("clean") {
    delete(rootProject.buildDir)
}