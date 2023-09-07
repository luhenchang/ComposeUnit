plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}
android {
    val sdkVersion = libs.versions.sdkVersion.get().toInt()
    val minSdkVersion = libs.versions.minSdkVersion.get().toInt()
    compileSdk = sdkVersion

    defaultConfig {
        applicationId = "com.example.composeunit"
        minSdk = minSdkVersion
        targetSdk = sdkVersion

        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    val javaVersion = libs.versions.javaVersion.get()
    kotlinOptions {
        jvmTarget = javaVersion
    }
    namespace = "com.example.composeunit"
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
        }
    }
    @Suppress("UnstableApiUsage")
    buildFeatures.compose = true
    @Suppress("UnstableApiUsage")
    composeOptions {
        val composeVersion = libs.versions.composeVersion.get()
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":base"))
    implementation(project(":highlight"))
    //room 数据库
    ksp(libs.room.compiler)
}
